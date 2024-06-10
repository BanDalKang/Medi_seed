package com.mediseed.mediseed.ui.ui.home

import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentHomeBinding
import com.mediseed.mediseed.ui.ui.bottomSheet.BottomSheetFragment
import com.mediseed.mediseed.ui.ui.home.model.ViewModel.HomeViewModel
import com.mediseed.mediseed.ui.ui.home.model.ViewModel.HomeViewModelFactory
import com.mediseed.mediseed.ui.ui.home.model.PharmacyItem.PharmacyItem
import com.mediseed.mediseed.ui.ui.home.model.UiState.UiState
import com.mediseed.mediseed.ui.ui.main.MainActivity
import com.mediseed.mediseed.ui.ui.home.model.ViewModel.SharedViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels { HomeViewModelFactory() }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var pharmacyInfo = mutableListOf<PharmacyItem.PharmacyInfo>()

    private  var userLatitude: Double = 0.0

    private  var userLongitude: Double = 0.0

    private var userAndMarkerDistance = mutableListOf<Float>()

    private var markerList = mutableListOf<Marker>()

    private val mainActivity by lazy {
        activity as? MainActivity
    }

    private lateinit var naverMap: NaverMap

    // 네이버 api
    private lateinit var fusedLocationSource: FusedLocationSource

    // 구글 api
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationOverlay: LocationOverlay




    companion object {
        fun newInstance() = HomeFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationSource =
            FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModelEvent()
        registerMap()
    }


    private fun registerViewModelEvent() = with(binding) {
        homeViewModel.getDaejeonSeoguLocation()
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.daejeonSeoguUiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                when (uiState) {
                    is UiState.PharmacyAddList -> {
                        pharmacyInfo = uiState.daejeonSeoguLocation as MutableList<PharmacyItem.PharmacyInfo>
                        /**지도 객체가 생성되었을 때, 데이터가 최신화될 때마다 마커를 생성합니다.*/
                        if ( this@HomeFragment::naverMap.isInitialized) {
                            // 현재위치로 카메라 이동
                            moveToCurrentLocation()
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    /**권한처리 요청*/
    private fun registerMap() {
        if (!hasPermission()) {
            requestPermissionLauncher.launch(PERMISSIONS)
        } else {
            initMapView()
        }
    }

    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    /**권한처리 결과*/
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var allGranted = permissions.all { it.value }
        if (allGranted) {
            Toast.makeText(
                requireContext(),
                R.string.location_permission_granted,
                Toast.LENGTH_SHORT
            ).show()
            initMapView()
        } else {
            Toast.makeText(
                requireContext(),
                R.string.location_permission_required,
                Toast.LENGTH_SHORT
            ).show()
            showSettingsDialog()
        }
    }

    private fun showSettingsDialog() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts(
            "package",
            requireContext().packageName,
            null
        )
        intent.data = uri
        intent.putExtra(
            ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSIONS,
            PERMISSIONS
        )
        settingLauncher.launch(intent)
    }

    private val settingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        registerMap()
    }

    /**Naver Map 객체 얻기 */
    private fun initMapView() {
        val fragmentManager = childFragmentManager
        val naverMapFragment = fragmentManager.findFragmentById(R.id.naver_map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.naver_map_view, it).commit()
            }
        /**콜백 메서드 onMapReady를 구현하고 있는 OnMapReadyCallback의 인스턴스를 인자로하여, 비동기로 NaverMap 객체를 얻어옵니다. NaverMap 객체가 준비되면 NaverMap 파라미터로하여 OnMapReadyCallback.onMapReady(NaverMap)함수 호출(객체 초기화될 때 한번만 호출) */
        naverMapFragment.getMapAsync(this)
    }

    /**Naver Map 객체 활용*/
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        locationOverlay = naverMap.locationOverlay
        // 현재 위치 관련 정보
        naverMap.apply {
            // 현재 위치
            locationSource = fusedLocationSource
            // 현재 위치, 나침반 버튼 기능
            uiSettings.apply {
                isLocationButtonEnabled = true
                isCompassEnabled = true
            }
            // 실시간 위치 추적하며 카메라 이동 + 위치 변경시 콜백함수
            locationTrackingMode = LocationTrackingMode.Follow
            addOnLocationChangeListener { location ->
                userLatitude = location.latitude
                userLongitude = location.longitude
                updateDistance()
            }
        }

        // 사용자 위치 아이콘 커스텀
        locationOverlay.apply {
            isVisible = true
            icon = OverlayImage.fromResource(R.drawable.userlocation)
            iconWidth = 90
            iconHeight = 90
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(callback: (Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                callback(location)
            }
    }

    private fun moveToCurrentLocation() {
       getLocation{ location: Location? ->
                location?.let {
                    userLatitude = it.latitude
                    userLongitude = it.longitude
                    moveCamera(userLatitude, userLongitude)
                    registerMarker(pharmacyInfo)
                }
            }
    }

    private fun registerMarker(pharmacyInfoList: MutableList<PharmacyItem.PharmacyInfo>) {
        pharmacyInfoList.forEach { info ->
            val markerLatitude = info.latitude?.toDoubleOrNull()
            val markerLongitude = info.longitude?.toDoubleOrNull()

            if (markerLatitude != null && markerLongitude != null) {
                val markerName = info.collectionLocationName
                val markerClassificationName = info.collectionLocationClassificationName
                val markerAddress = info.streetNameAddress
                val markerUpdate = info.dataDate

                Marker().apply {
                    position = LatLng(markerLatitude, markerLongitude)
                    captionText = markerName.toString()
                    icon = OverlayImage.fromResource(R.drawable.userlocation)
                    //icon = MarkerIcons.BLACK
                    //icon = iconTintColor = Color.RED
                    width = 90
                    height = 90
                    map = naverMap
                    markerList.add(this)


                    val distance = calculateDistance(userLatitude,userLongitude,markerLatitude,markerLongitude)
                    userAndMarkerDistance.add(distance)
                    userAndMarkerDistance.forEachIndexed { index, distance ->
                        if (pharmacyInfo.size > index) {
                            pharmacyInfo[index] = pharmacyInfo[index].copy(distance = distance)
                        }
                    }

                    setOnClickListener {
                        val markerInfo = PharmacyItem.PharmacyInfo(
                            latitude = null,
                            longitude = null,
                            distance = distance,
                            collectionLocationName = markerName,
                            collectionLocationClassificationName = markerClassificationName,
                            dataDate = markerUpdate,
                            streetNameAddress = markerAddress
                        )
                        moveCamera(markerLatitude, markerLongitude)
                        showBottomSheet(markerInfo)
                    }
                }

            } else {
                Log.e("markerError", "Invalid latitude or longitude for marker: $info")
            }
        }
    }


    private fun calculateDistance(userLat: Double, userLon: Double, markerLat: Double, markerLon: Double): Float {
        val userLocation = Location("UserLocation").apply {
            latitude = userLat
            longitude = userLon
        }

        val markerLocation = Location("MarkerLocation").apply {
            latitude = markerLat
            longitude = markerLon
        }

        return userLocation.distanceTo(markerLocation)
    }

    private fun setData(data: Boolean) {
        sharedViewModel.setData(data)
    }

    private fun updateDistance() {
        userAndMarkerDistance.clear()
        pharmacyInfo.forEachIndexed { index, info ->
            val markerLatitude = info.latitude?.toDoubleOrNull()
            val markerLongitude = info.longitude?.toDoubleOrNull()
            if (markerLatitude != null && markerLongitude != null) {
                val distance =
                    calculateDistance(userLatitude, userLongitude, markerLatitude, markerLongitude)
                userAndMarkerDistance.add(distance)
                pharmacyInfo[index] = pharmacyInfo[index].copy(distance = distance)
            }
            if (userAndMarkerDistance.any { it <= 20 }) setData(true) else setData(false)
        }
    }


    // 이름순 -> 거리순 정렬 알고리즘
    fun updateSuggestions(query: String) {
        val pharmacyNameList = pharmacyInfo.map { it.collectionLocationName }
        val filterList = if (query.isNotEmpty()) {
            pharmacyNameList.filter { suggestion ->
                suggestion?.startsWith(query, ignoreCase = true) == true
            }
        } else {
            emptyList<Any>()
        }
        val suggestionList = pharmacyInfo.filter { item ->
            filterList.contains(item.collectionLocationName)
        }

        val sortedSuggestionList = suggestionList.sortedBy { it.distance }

        if (sortedSuggestionList.isNotEmpty()) {
            mainActivity?.suggestionRecyclerView?.visibility = View.VISIBLE
        } else {
            mainActivity?.suggestionRecyclerView?.visibility = View.INVISIBLE
        }

        mainActivity?.suggestionAdapter?.updateItem(sortedSuggestionList)

    }

    private fun showBottomSheet(markerInfo: PharmacyItem.PharmacyInfo): Boolean {
        val bottomSheetFragment = BottomSheetFragment.newInstance(markerInfo)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        return true
    }


    fun performSearch(query: String) {
        var foundpharmacyName = pharmacyInfo.filter { it.collectionLocationName == query }
        for (pharmacyName in foundpharmacyName) {
            val markerLatitude = pharmacyName.latitude?.toDoubleOrNull()
            val markerLongitude = pharmacyName.longitude?.toDoubleOrNull()
            if (markerLatitude != null && markerLongitude != null) {
                moveCamera(markerLatitude, markerLongitude)
            }
        }

    }

    fun moveCamera(latitude: Double, longitude: Double) {
        val currentLocation = CameraUpdate.scrollTo(LatLng(latitude, longitude))
        naverMap.moveCamera(currentLocation)

    }

    override fun onResume() {
        super.onResume()
        mainActivity?.showBar()
        moveToCurrentLocation()
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    /**onPause 되었을 때, 실시간 위치 업데이트를 중지합니다.*/
    override fun onPause() {
        super.onPause()
        naverMap.locationTrackingMode = LocationTrackingMode.None
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
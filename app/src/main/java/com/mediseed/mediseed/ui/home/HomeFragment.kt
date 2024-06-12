package com.mediseed.mediseed.ui.home

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
import com.mediseed.mediseed.ui.bottomSheet.BottomSheetFragment
import com.mediseed.mediseed.ui.home.model.viewModel.HomeViewModel
import com.mediseed.mediseed.ui.home.model.viewModel.HomeViewModelFactory
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem
import com.mediseed.mediseed.ui.home.model.uiState.UiState
import com.mediseed.mediseed.ui.home.model.viewModel.SharedViewModel
import com.mediseed.mediseed.ui.main.MainActivity
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
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    private val markerList = mutableListOf<Marker>()
    private val mainActivity by lazy { activity as? MainActivity }

    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationSource: FusedLocationSource
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
        fusedLocationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE)
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
                        if (this@HomeFragment::naverMap.isInitialized) {
                            moveToCurrentLocation()
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun registerMap() {
        if (!hasPermission()) {
            requestPermissionLauncher.launch(PERMISSIONS)
        } else {
            initMapView()
        }
    }

    private fun hasPermission(): Boolean {
        return PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            Toast.makeText(requireContext(), R.string.location_permission_granted, Toast.LENGTH_SHORT).show()
            initMapView()
        } else {
            Toast.makeText(requireContext(), R.string.location_permission_required, Toast.LENGTH_SHORT).show()
            showSettingsDialog()
        }
    }

    private fun showSettingsDialog() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        settingLauncher.launch(intent)
    }

    private val settingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        registerMap()
    }

    private fun initMapView() {
        val fragmentManager = childFragmentManager
        val naverMapFragment = fragmentManager.findFragmentById(R.id.naver_map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.naver_map_view, it).commit()
            }
        naverMapFragment.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        locationOverlay = naverMap.locationOverlay
        configureNaverMap()
    }

    private fun configureNaverMap() {
        naverMap.apply {
            locationSource = fusedLocationSource
            uiSettings.apply {
                isLocationButtonEnabled = true
                isCompassEnabled = true
            }
            locationTrackingMode = LocationTrackingMode.Follow
            addOnLocationChangeListener { location ->
                userLatitude = location.latitude
                userLongitude = location.longitude
                updateDistance()
            }
        }

        locationOverlay.apply {
            isVisible = true
            icon = OverlayImage.fromResource(R.drawable.usermarker)
            iconWidth = 80
            iconHeight = 90
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(callback: (Location?) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            callback(location)
        }
    }

    private fun moveToCurrentLocation() {
        getLocation { location ->
            location?.let {
                userLatitude = it.latitude
                userLongitude = it.longitude
                moveCamera(userLatitude, userLongitude)
                registerMarkers(pharmacyInfo)
            }
        }
    }

    private fun registerMarkers(pharmacyInfoList: List<PharmacyItem.PharmacyInfo>) {
        pharmacyInfoList.forEach { info ->
            val markerLatitude = info.latitude?.toDoubleOrNull()
            val markerLongitude = info.longitude?.toDoubleOrNull()

            if (markerLatitude != null && markerLongitude != null) {
                val markerName = info.collectionLocationName
                val markerClassificationName = info.collectionLocationClassificationName
                val markerAddress = info.streetNameAddress
                val markerUpdate = info.dataDate
                val markerPhoneNumber = info.phoneNumber

                Marker().apply {
                    position = LatLng(markerLatitude, markerLongitude)
                    captionText = markerName.toString()
                    icon = OverlayImage.fromResource(R.drawable.mapmarker)
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
                            streetNameAddress = markerAddress,
                            phoneNumber = markerPhoneNumber
                        )
                        moveCamera(markerLatitude, markerLongitude)
                        showBottomSheet(markerInfo)
                    }
                }

                val marker = createMarker(info, markerLatitude, markerLongitude)
                markerList.add(marker)
                marker.map = naverMap
            } else {
                Log.e("markerError", "Invalid latitude or longitude for marker: $info")
            }
        }
    }

    private fun createMarker(info: PharmacyItem.PharmacyInfo, latitude: Double, longitude: Double): Marker {
        return Marker().apply {
            position = LatLng(latitude, longitude)
            captionText = info.collectionLocationName.toString()
            icon = OverlayImage.fromResource(R.drawable.userlocation)
            width = 90
            height = 90

            val distance = calculateDistance(userLatitude, userLongitude, latitude, longitude)
            info.distance = distance

            setOnClickListener {
                showBottomSheet(info)
                moveCamera(latitude, longitude)
                true
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

    private fun setAddress(address: String) {
        sharedViewModel.setAddress(address)
    }

    private fun updateDistance() {
        pharmacyInfo.forEachIndexed { index, info ->
            val markerLatitude = info.latitude?.toDoubleOrNull()
            val markerLongitude = info.longitude?.toDoubleOrNull()
            if (markerLatitude != null && markerLongitude != null) {
                val distance = calculateDistance(userLatitude, userLongitude, markerLatitude, markerLongitude)
                info.distance = distance
            }
        }

        val closestPharmacy = getClosestPharmacy()
        closestPharmacy?.let {
            if (it.distance!! <= 20) {
                setData(true)
                it.streetNameAddress?.let { address -> setAddress(address) }
            } else setData(false)
        }
    }

    private fun getClosestPharmacy(): PharmacyItem.PharmacyInfo? {
        return pharmacyInfo.minByOrNull { it.distance ?: Float.MAX_VALUE }
    }

    // 정렬 알고리즘: 첫글자 > 해당글자 포함 > 거리순 정렬 알고리즘
    fun updateSuggestions(query: String) {
        val pharmacyNameList = pharmacyInfo.map { it.collectionLocationName }
        val filterList = if (query.isNotEmpty()) {
            val startsWithQuery = pharmacyNameList.filter { suggestion ->
                suggestion?.startsWith(query, ignoreCase = true) == true
            }
            val containsQuery = pharmacyNameList.filter { suggestion ->
                suggestion?.contains(query, ignoreCase = true) == true && suggestion !in startsWithQuery
            }
            startsWithQuery + containsQuery
        } else {
            emptyList()
        }

        val suggestionList = pharmacyInfo.filter { item ->
            filterList.contains(item.collectionLocationName)
    fun updateSuggestions(query: String) {
        val suggestionList = if (query.isNotEmpty()) {
            pharmacyInfo.filter { it.collectionLocationName?.startsWith(query, ignoreCase = true) == true }
        } else {
            emptyList()
        }

        val sortedSuggestionList = suggestionList.sortedBy { it.distance }
        mainActivity?.suggestionRecyclerView?.visibility = if (sortedSuggestionList.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        mainActivity?.suggestionAdapter?.updateItem(sortedSuggestionList)
    }

    private fun showBottomSheet(markerInfo: PharmacyItem.PharmacyInfo): Boolean {
        val bottomSheetFragment = BottomSheetFragment.newInstance(markerInfo)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        return true
    }

    fun performSearch(query: String) {
        val foundPharmacies = pharmacyInfo.filter { it.collectionLocationName == query }
        foundPharmacies.forEach { pharmacy ->
            val latitude = pharmacy.latitude?.toDoubleOrNull()
            val longitude = pharmacy.longitude?.toDoubleOrNull()
            if (latitude != null && longitude != null) {
                moveCamera(latitude, longitude)
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

    override fun onPause() {
        super.onPause()
        naverMap.locationTrackingMode = LocationTrackingMode.None
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
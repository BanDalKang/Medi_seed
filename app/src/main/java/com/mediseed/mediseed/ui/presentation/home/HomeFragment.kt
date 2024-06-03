package com.mediseed.mediseed.ui.presentation.home

import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentHomeBinding
import com.mediseed.mediseed.ui.presentation.bottomSheet.BottomSheetFragment
import com.mediseed.mediseed.ui.presentation.home.model.HomeViewModel
import com.mediseed.mediseed.ui.presentation.home.model.HomeViewModelFactory
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyItem
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyUiState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.MarkerInfo
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

    private var pharmacyInfo = mutableListOf<PharmacyItem.PharmacyInfo>()

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
        homeViewModel.getLocation()
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                when (uiState) {
                    is PharmacyUiState.PharmacyAddList -> pharmacyInfo =
                        uiState.pharmacyLocation as MutableList<PharmacyItem.PharmacyInfo>

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
        ) /// package:com.mediseed.mediseed
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
            // 현재 위치로 카메라 이동
            moveToCurrentLocation()
            // 현재 위치 버튼 기능
            uiSettings.isLocationButtonEnabled = true
            // 실시간 위치 추적하며 카메라 움직임
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
        // 사용자 위치 아이콘 커스텀
        locationOverlay.apply {
            isVisible = true
            icon = OverlayImage.fromResource(R.drawable.marker_pill)
            iconWidth = 50
            iconHeight = 50
        }
        // 마커표시 및 클릭이벤트
        registerMarker(pharmacyInfo)

    }

    //LastLocation은 구글 api가 더 빠름
    @SuppressLint("MissingPermission")
    private fun moveToCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val currentLocation = CameraUpdate.scrollTo(LatLng(latitude, longitude))
                    naverMap.moveCamera(currentLocation)
                }
            }
    }

    private fun registerMarker(pharmacyInfo: MutableList<PharmacyItem.PharmacyInfo>) {

        pharmacyInfo.forEach { info ->
            val markerLatitude = info.latitude?.toDouble()
            val markerLongitude = info.longitude?.toDouble()
            val markerName = info.CollectionLocationName
            val markerClassificationName = info.CollectionLocationClassificationName
            val markerPhoneNumber = info.PhoneNumber
            val markerAddress = info.StreetNameAddress
            val markerUpdate = info.DataDate

            Marker().apply {
                position = LatLng(markerLatitude!!, markerLongitude!!)
                captionText = markerName.toString()
                captionColor = Color.MAGENTA
               // icon = OverlayImage.fromResource(R.drawable.ic_sprout)
                map = naverMap

                setOnClickListener {
                    val markerInfo = PharmacyItem.PharmacyInfo(
                        latitude = null,
                        longitude = null,
                        CollectionLocationName = markerName,
                        CollectionLocationClassificationName = markerClassificationName,
                        PhoneNumber = markerPhoneNumber,
                        StreetNameAddress = markerAddress,
                        DataDate = markerUpdate
                    )
                    CameraUpdate.scrollTo(LatLng(markerLatitude!!, markerLongitude!!))
                    addFragment(markerInfo)
                }
            }
        }
    }

private fun addFragment(markerInfo: PharmacyItem.PharmacyInfo): Boolean{
    val bottomSheetFragment = BottomSheetFragment.newInstance(markerInfo)
    bottomSheetFragment.show(childFragmentManager,bottomSheetFragment.tag)
     return true
}


override fun onResume() {
    super.onResume()
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
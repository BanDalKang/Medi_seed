package com.mediseed.mediseed.ui.presentation.home

import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.appcompat.widget.WithHint
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentHomeBinding
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

    private var pharmacyLocation = mutableListOf<PharmacyItem.PharmacyLocation>()

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var naverMap: NaverMap

    private lateinit var fusedLocationSource: FusedLocationSource

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationOverlay: LocationOverlay

    companion object {
        fun newInstance() = HomeFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
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
        homeViewModel.getLocation()
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                when (uiState) {
                    is PharmacyUiState.PharmacyAddList -> pharmacyLocation =
                        uiState.pharmacyLocation as MutableList<PharmacyItem.PharmacyLocation>

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
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            // 모든 위치 권한이 허용된 경우
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
            registerMap()
        }
    }

    /**Naver Map 객체 얻기 */
    private fun initMapView() {
        val fragmentManager = childFragmentManager
        val naverMapFragment = fragmentManager.findFragmentById(R.id.naver_map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.naver_map_view, it).commit()
            }
        /**비동기로 NaverMap 객체를 얻어옵니다. NaverMap 객체가 준비되면 NaverMap 파라미터로하여 onMapReady(NaverMap) 콜백함수 호출 */
        naverMapFragment.getMapAsync(this)
    }

    /**Naver Map 객체 활용*/
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        locationOverlay = naverMap.locationOverlay
        var marker = Marker()
        var latitude = pharmacyLocation.map { it.latitude!!.toDouble() }
        var longitude = pharmacyLocation.map { it.longitude!!.toDouble() }


        // 현재 위치 관련 정보
        naverMap.apply {
            // 현재 위치
            locationSource = fusedLocationSource
            // 현재 위치로 카메라 이동
            moveToCurrentLocation()
            // 현재 위치 버튼 기능
            uiSettings.isLocationButtonEnabled = true
            // 위치 추적하며 카메라 움직임
            locationTrackingMode = LocationTrackingMode.Follow
        }
        // 사용자 위치 아이콘 커스텀
        locationOverlay.apply {
            isVisible = true
            icon = OverlayImage.fromResource(R.drawable.ic_sprout)
            iconWidth = 50
            iconHeight = 50
            //circleRadius = 300
            //circleColor = 0x40FFD700
        }
        // 마커표시
        latitude.zip(longitude).forEach { (latitude, longitude) ->
            marker.apply {
                position = LatLng(latitude, longitude)
                map = naverMap
            }
        }
        // 마커 클릭시 카메라 이동
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
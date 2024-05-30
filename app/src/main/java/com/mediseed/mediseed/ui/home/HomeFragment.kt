package com.mediseed.mediseed.ui.home

import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentHomeBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class HomeFragment : Fragment(), OnMapReadyCallback {


    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationOverlay: LocationOverlay

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    companion object {
        fun newInstance() = HomeFragment()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
            initMapView()
        } else {
            initMapView()
        }
    }

    /**위치 권한이 있을 경우 true, 없을 경우 false를 반환*/
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(),permission)!= PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**비동기로  Naver Map 객체얻기 */
    private fun initMapView() {
        
        val fragmentManager = childFragmentManager
        val naverMapFragment = fragmentManager.findFragmentById(R.id.naver_map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.naver_map_view, it).commit()
            }
        /**비동기로 NaverMap 객체를 얻어옴. NaverMap 객체가 준비되면 NaverMap 파라미터로하여 onMapReady(NaverMap) 콜백함수 호출 */
        naverMapFragment.getMapAsync(this)
        fusedLocationSource = FusedLocationSource(requireActivity(),LOCATION_PERMISSION_REQUEST_CODE)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    /**Naver Map 객체 활용*/
    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        locationOverlay = naverMap.locationOverlay

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
        }
    }

    @SuppressLint("MissingPermission")
    private fun moveToCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude

                    val currentLocation = CameraUpdate.scrollTo(LatLng(latitude,longitude))
                    naverMap.moveCamera(currentLocation)
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
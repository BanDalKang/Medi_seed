package com.mediseed.mediseed.ui.home

import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentHomeBinding
import com.mediseed.mediseed.ui.bottomSheet.BottomSheetFragment
import com.mediseed.mediseed.ui.home.model.pharmacyItem.GeoCode
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
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels { HomeViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var pharmacyInfo = mutableListOf<PharmacyItem.PharmacyInfo>()
    private lateinit var streetNameAddress: MutableList<String?>
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    private val mainActivity by lazy { activity as? MainActivity }

    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationOverlay: LocationOverlay

    private var daejeonSeoguCircle = CircleOverlay()
    private var daejeonYuseongguGeoLatLng = mutableListOf<GeoCode.GeoLatLng>()
    private var daejeonYuseongguCircle = CircleOverlay()

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
        registerMap()
    }

    private fun initSearchAlgorithm() {
        homeViewModel.apply {
            setPharmacyInfo(pharmacyInfo)
            filteredSuggestions.observe(viewLifecycleOwner, Observer { suggestionList ->
                updateWithSuggetstions(suggestionList)
            })
        }
    }

    private fun updateWithSuggetstions(sortedSuggestionList: List<PharmacyItem.PharmacyInfo>?) {
        mainActivity?.suggestionRecyclerView?.visibility =
            if (sortedSuggestionList?.isNotEmpty() == true) View.VISIBLE else View.INVISIBLE
        if (sortedSuggestionList != null) {
            mainActivity?.suggestionAdapter?.updateItem(sortedSuggestionList)
        }
    }

    private fun DaejeonSeoguViewModelEvent() = with(binding) {
        homeViewModel.getDaejeonSeoguLocation()
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.uiState.flowWithLifecycle(lifecycle)
                .collectLatest { uiState ->
                    when (uiState) {
                        is UiState.AddList -> {
                            pharmacyInfo =
                                uiState.daejeonSeoguLocation as MutableList<PharmacyItem.PharmacyInfo>
                            registerMarkers(pharmacyInfo)
                            initSearchAlgorithm()
                        }

                        else -> {}
                    }
                }
        }
    }

    private fun DaejeonYuseongguViewModelEvent() = with(binding) {
        homeViewModel.getDaejeonYuseongguLocation()
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.uiState.flowWithLifecycle(lifecycle)
                .collectLatest { uiState ->
                    when (uiState) {
                        is UiState.AddList -> {
                            pharmacyInfo =
                                uiState.daejeonYuseongguLocation as MutableList<PharmacyItem.PharmacyInfo>
                            registerMarkers(pharmacyInfo)
                            initSearchAlgorithm()
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
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
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
        daejeonSeoguArea()
        configureNaverMap()
    }

    private fun configureNaverMap() {
        var isUserLocationUpdate = true
        naverMap.apply {
            locationSource = fusedLocationSource

            uiSettings.apply {
                isLocationButtonEnabled = true
                isCompassEnabled = true
            }
            if (this@HomeFragment::naverMap.isInitialized) {
            locationTrackingMode = LocationTrackingMode.Follow }
            addOnLocationChangeListener { location ->
                userLatitude = location.latitude
                userLongitude = location.longitude
                val userLatLng = LatLng(userLatitude, userLongitude)
                if (isUserLocationUpdate) {
                    checkUserArea(userLatLng)
                    isUserLocationUpdate = false
                }
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

    private fun daejeonSeoguArea() {
        daejeonSeoguCircle.apply {
            center = LatLng(36.3321170228103, 127.374576568879)
            radius = 6000.0
            color = 0x00FFFFFF
            outlineWidth = 2
            outlineColor = 0xCC008000.toInt()
            map = naverMap
        }
    }

    private fun daejeonYuseongguArea() {
        daejeonYuseongguCircle.apply {
            radius = 6000.0
            color = 0x00FFFFFF
            outlineWidth = 2
            outlineColor = 0xCC008000.toInt()
            map = naverMap
        }
    }

    private fun checkUserArea(userLatLng: LatLng) {
        val DaejeonSeoguInside = homeViewModel.isInsideArea(
            userLatLng,
            daejeonSeoguCircle.center,
            daejeonSeoguCircle.radius
        )
        if (DaejeonSeoguInside) {
            DaejeonSeoguViewModelEvent()
        }
    }

    private fun registerMarkers(pharmacyInfoList: List<PharmacyItem.PharmacyInfo>) {
        pharmacyInfoList.forEach { info ->
            val markerLatitude = info.latitude?.toDoubleOrNull()
            val markerLongitude = info.longitude?.toDoubleOrNull()

            if (markerLatitude != null && markerLongitude != null) {
                val markerName = info.collectionLocationName
                val distance = calculateDistance(
                    userLatitude,
                    userLongitude,
                    markerLatitude,
                    markerLongitude
                )

                info.distance = distance

                Marker().apply {
                    position = LatLng(markerLatitude, markerLongitude)
                    captionText = markerName.toString()
                    icon = OverlayImage.fromResource(R.drawable.pharmacymarker)
                    width = 80
                    height = 100
                    map = naverMap

                    setOnClickListener(
                        onMarkerClick(
                            markerLatitude,
                            markerLongitude,
                            info
                        )
                    )
                }
            }
        }
    }

    private fun onMarkerClick(
        markerLatitude: Double?,
        markerLongitude: Double?,
        pharmacyInfoList: PharmacyItem.PharmacyInfo
    ): Overlay.OnClickListener {
        return Overlay.OnClickListener {
            if (markerLatitude != null && markerLongitude != null) {
                moveCamera(markerLatitude, markerLongitude)
            }
            showBottomSheet(pharmacyInfoList)
            true
        }
    }

    private fun calculateDistance(
        userLat: Double,
        userLon: Double,
        markerLat: Double,
        markerLon: Double
    ): Float {
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
        pharmacyInfo.forEach { info ->
            val markerLatitude = info.latitude?.toDoubleOrNull()
            val markerLongitude = info.longitude?.toDoubleOrNull()
            if (markerLatitude != null && markerLongitude != null) {
                val distance =
                    calculateDistance(
                        userLatitude,
                        userLongitude,
                        markerLatitude,
                        markerLongitude
                    )
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

    private fun showBottomSheet(markerInfo: PharmacyItem.PharmacyInfo): Boolean {
        val bottomSheetFragment = BottomSheetFragment.newInstance(markerInfo)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        return true
    }

    fun moveCamera(latitude: Double, longitude: Double) {
        val currentLocation = CameraUpdate.scrollTo(LatLng(latitude, longitude))
        val zoomLocation = CameraUpdate.zoomTo(18.0)
        naverMap.moveCamera(currentLocation)
        naverMap.moveCamera(zoomLocation)
    }

    override fun onResume() {
        super.onResume()
        mainActivity?.showBar()
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.updateSuggestions(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
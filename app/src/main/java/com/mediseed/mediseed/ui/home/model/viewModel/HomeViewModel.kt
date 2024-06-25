package com.mediseed.mediseed.ui.home.model.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediseed.mediseed.domain.model.DaejeonSeoguEntity
import com.mediseed.mediseed.domain.model.DaejeonYuseongguEntity
import com.mediseed.mediseed.domain.model.GeoCodeEntity
import com.mediseed.mediseed.domain.usecase.GeoCodeUseCase
import com.mediseed.mediseed.domain.usecase.PharmacyUseCase
import com.mediseed.mediseed.ui.home.model.pharmacyItem.GeoCode
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem
import com.mediseed.mediseed.ui.home.model.uiState.UiState
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pharmacyUseCase: PharmacyUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.ResultEmpty)
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    private val _filteredSuggestions: MutableLiveData<List<PharmacyItem.PharmacyInfo>> = MutableLiveData()
    val filteredSuggestions: LiveData<List<PharmacyItem.PharmacyInfo>> get() = _filteredSuggestions

    private var pharmacyInfo: List<PharmacyItem.PharmacyInfo> = emptyList()

    fun getDaejeonSeoguLocation() = viewModelScope.launch {
        pharmacyUseCase.getPharmacyDaejeonSeogu().onSuccess { pharmacyEntity ->
            val pharmacyLocation = createDaejeonSeoguLocation(pharmacyEntity)
            _uiState.update {
                if (pharmacyLocation.isEmpty()) {
                    UiState.ResultEmpty
                } else {
                    UiState.AddList(daejeonSeoguLocation = pharmacyLocation)
                }
            }
        }.onFailure {
        }
    }

    fun getDaejeonYuseongguLocation() = viewModelScope.launch {
        pharmacyUseCase.getPharmacyDaejeonYuseonggu().onSuccess { pharmacyEntity ->
            val pharmacyLocation = createDaejeonYuseongguLocation(pharmacyEntity)
            _uiState.update {
                if (pharmacyLocation.isEmpty()) {
                    UiState.ResultEmpty
                } else {
                    UiState.AddList(daejeonYuseongguLocation = pharmacyLocation)
                }
            }
        }.onFailure {
        }
    }

    private fun createGeoLatLng(entity: GeoCodeEntity): List<GeoCode.GeoLatLng> {
        return entity.addresses.map {
            GeoCode.GeoLatLng(
                it.y,
                it.x
            )
        }
    }

    private fun createDaejeonSeoguLocation(entity: DaejeonSeoguEntity): List<PharmacyItem.PharmacyInfo> {
        return entity.data.map {
            PharmacyItem.PharmacyInfo(
                it.latitude,
                it.longitude,
                0f,
                it.collectionLocationName,
                it.collectionLocationClassificationName,
                it.dataDate,
                it.lotNumberAddress,
                it.phoneNumber
            )
        }
    }

    private fun createDaejeonYuseongguLocation(entity: DaejeonYuseongguEntity): List<PharmacyItem.PharmacyInfo> {
        return entity.data.map {
            PharmacyItem.PharmacyInfo(
                "",
                "",
                0f,
                "",
                "",
                "",
                it.streetNameAddress,
                ""
            )
        }
    }

    fun isInsideArea(userLatLng: LatLng, centerLatLng: LatLng, radius: Double): Boolean {
        val userLocation = computeDistanceBetween(userLatLng, centerLatLng)
        return userLocation <= radius
    }

    private fun computeDistanceBetween(userLatLng: LatLng, centerLatLng: LatLng): Double {
        val userLat = Math.toRadians(userLatLng.latitude)
        val userLon = Math.toRadians(userLatLng.longitude)
        val centerLat = Math.toRadians(centerLatLng.latitude)
        val centerLon = Math.toRadians(centerLatLng.longitude)

        val earthRadius = 6371

        val dLat = centerLat - userLat
        val dLon = centerLon - userLon

        val a = Math.sin(dLat / 2).pow(2) + Math.cos(userLat) * Math.cos(centerLat) * Math.sin(dLon / 2)
            .pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c * 1000
    }

    fun setPharmacyInfo(infoList: List<PharmacyItem.PharmacyInfo>) {
        pharmacyInfo = infoList
    }

    fun updateSuggestions(query: String?) {
        val pharmacyNameList = pharmacyInfo.map { it.collectionLocationName }
        val filterList = if (!query.isNullOrEmpty()) {
            val startsWithQuery = pharmacyNameList.filter { suggestion ->
                suggestion?.startsWith(query, ignoreCase = true) == true
            }
            val containsQuery = pharmacyNameList.filter { suggestion ->
                suggestion?.contains(
                    query,
                    ignoreCase = true
                ) == true && suggestion !in startsWithQuery
            }
            (startsWithQuery + containsQuery).take(20)
        } else {
            emptyList()
        }

        val suggestionList = pharmacyInfo.filter { item ->
            filterList.contains(item.collectionLocationName)
        }
        val sortedSuggestionList = suggestionList.sortedBy { it.distance }

        _filteredSuggestions.value = sortedSuggestionList
    }
}
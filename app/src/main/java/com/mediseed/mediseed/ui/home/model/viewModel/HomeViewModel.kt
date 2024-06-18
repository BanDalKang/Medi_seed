package com.mediseed.mediseed.ui.home.model.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.mediseed.mediseed.domain.model.PharmacyDaejeonSeoguEntity
import com.mediseed.mediseed.domain.usecase.PharmacyUseCase
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem
import com.mediseed.mediseed.ui.home.model.uiState.UiState
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.pow

class HomeViewModel(
    private val pharmacyUseCase: PharmacyUseCase
) : ViewModel() {

    private val _daejeonSeoguUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.ResultEmpty)
    val daejeonSeoguUiState: StateFlow<UiState> get() = _daejeonSeoguUiState.asStateFlow()

    private val _filteredSuggestions: MutableLiveData<List<PharmacyItem.PharmacyInfo>> = MutableLiveData()
    val filteredSuggestions: LiveData<List<PharmacyItem.PharmacyInfo>> get() = _filteredSuggestions

    private var pharmacyInfo: List<PharmacyItem.PharmacyInfo> = emptyList()

    //  거리 계산 알고리즘
     fun isInsideArea(userLatLng: LatLng, centerLatLng: LatLng, radius: Double): Boolean {
        val userLocation = computeDistanceBetween(userLatLng, centerLatLng) // 원의 중심과 사용자 사이의 거리를 통해 사용자의 위치를 계산합니다.
        return userLocation <= radius // 사용자의 위치가 반지름 보다 안쪽에 있으면 true를 반환합니다.
    }

    private fun computeDistanceBetween(userLatLng: LatLng, centerLatLng: LatLng): Double {
        val userLat = Math.toRadians(userLatLng.latitude)
        val userLon = Math.toRadians(userLatLng.longitude)
        val centerLat = Math.toRadians(centerLatLng.latitude)
        val centerLon = Math.toRadians(centerLatLng.longitude)

        val earthRadius = 6371 // 지구의 반지름(킬로미터)

        val dLat = centerLat - userLat
        val dLon = centerLon - userLon

        val a = Math.sin(dLat / 2).pow(2) + Math.cos(userLat) * Math.cos(centerLat) * Math.sin(dLon / 2)
            .pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c * 1000 // 결과를 미터로 변환
    }


    // 정렬 알고리즘: 첫글자 > 해당글자 포함 > 거리순 정렬 알고리즘 (최대 20개)
    fun setPharmacyInfo(infoList: List<PharmacyItem.PharmacyInfo>) {
        pharmacyInfo = infoList
    }
    fun updateSuggestions(query: String) {
        val pharmacyNameList = pharmacyInfo.map { it.collectionLocationName }
        val filterList = if (query.isNotEmpty()) {
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




    /**사용자의 현재 위치를 실시간으로 받고, 반경 300m 안에 있는 약국의 위치를 마커로 표시합니다.*/
    fun getDaejeonSeoguLocation() = viewModelScope.launch {
        pharmacyUseCase.getPharmacyDaejeonSeogu().onSuccess { pharmacyEntity ->
            val pharmacyLocation = createPharmacyLocation(pharmacyEntity)
            _daejeonSeoguUiState.update {
                if (pharmacyLocation.isEmpty()) {
                    UiState.ResultEmpty
                } else {
                    UiState.PharmacyAddList(pharmacyLocation)
                }
            }
        }.onFailure {
        }
    }

    fun getDaejeonYuseongguLocation() = viewModelScope.launch {
        pharmacyUseCase.getPharmacyDaejeonYuseonggu().onSuccess { pharmacyEntity ->
        }
    }

    private fun createPharmacyLocation(entity: PharmacyDaejeonSeoguEntity): List<PharmacyItem.PharmacyInfo> {
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
}
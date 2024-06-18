package com.mediseed.mediseed.ui.home.model.viewModel

import androidx.lifecycle.ViewModel
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

class HomeViewModel(
    private val pharmacyUseCase: PharmacyUseCase
) : ViewModel() {

    private val _daejeonSeoguUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.ResultEmpty)
  //  private val _daejeonYuseongguUiState: MutableStateFlow<UiState>
    val daejeonSeoguUiState: StateFlow<UiState> get() = _daejeonSeoguUiState.asStateFlow()



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
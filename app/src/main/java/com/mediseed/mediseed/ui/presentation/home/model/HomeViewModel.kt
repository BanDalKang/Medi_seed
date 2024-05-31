package com.mediseed.mediseed.ui.presentation.home.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediseed.mediseed.ui.domain.model.PharmacyEntity
import com.mediseed.mediseed.ui.domain.usecase.PharmacyUseCase
import com.naver.maps.map.CameraUpdate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val pharmacyUseCase: PharmacyUseCase
) : ViewModel() {

   private val _uiState: MutableStateFlow<PharmacyUiState> = MutableStateFlow(PharmacyUiState.ResultEmpty)

    val uiState: StateFlow<PharmacyUiState> get() = _uiState.asStateFlow()

    /**사용자의 현재 위치를 실시간으로 받고, 반경 300m 안에 있는 약국의 위치를 마커로 표시합니다.*/
    fun getLocation() = viewModelScope.launch {
        pharmacyUseCase().onSuccess { pharmacyEntity ->
            val pharmacyLocation = createPharmacyLocation(pharmacyEntity)
            _uiState.update {
                if (pharmacyLocation.isEmpty()) {
                    PharmacyUiState.ResultEmpty
                } else {
                    PharmacyUiState.PharmacyAddList(pharmacyLocation)
                }
            }
        }.onFailure {

        }
    }



    private fun createPharmacyLocation(entity: PharmacyEntity): List<PharmacyItem> {
        return entity.data.map {
            PharmacyItem.PharmacyLocation(
                it.Latitude,
                it.longitude,
                it.CollectionLocationName,
                it.CollectionLocationClassificationName,
                it.StreetNameAddress,
                it.PhoneNumber,
                it.DataDate,
                it.lotNumberAddress
            )
        }

    }


}
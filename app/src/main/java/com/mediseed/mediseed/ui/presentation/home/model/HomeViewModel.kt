package com.mediseed.mediseed.ui.presentation.home.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediseed.mediseed.ui.domain.model.PharmacyEntity
import com.mediseed.mediseed.ui.domain.usecase.PharmacyUseCase
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

    fun getLocationAndMarking(
        isLoading: Boolean = true
    ) = viewModelScope.launch {
        if (isLoading) {
            setLoading()
        }
        pharmacyUseCase().onSuccess { pharmacyEntity ->
            val items = createPharmacyLocation(pharmacyEntity)
        }
    }

    private fun setLoading() {
        _uiState.update {
            PharmacyUiState.Loading
        }
    }

    private fun createPharmacyLocation(entity: PharmacyEntity): List<PharmacyItem> {
        return entity.data.map {
            PharmacyItem.PharmacyLocation(
                it.Latitude,
                it.longitude
            )
        }

    }


}
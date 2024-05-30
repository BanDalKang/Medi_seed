package com.mediseed.mediseed.ui.presentation.home.model

sealed interface PharmacyUiState {

    open class Notice(
        val message: String
    ) : PharmacyUiState

    data object Loading : PharmacyUiState

    data object ResultEmpty : Notice("결과 없음")

    sealed interface ResultList: PharmacyUiState {
        val pharmacyItems: List<PharmacyItem>
}

    data class PharmacyAddList(
        override val pharmacyItems: List<PharmacyItem> = emptyList()
    ) : ResultList

}

package com.mediseed.mediseed.ui.presentation.home.model

sealed interface PharmacyUiState {

    open class Notice(
        val message: String
    ) : PharmacyUiState


    data object ResultEmpty : Notice("결과 없음")

    sealed interface ResultList: PharmacyUiState {
        val pharmacyLocation: List<PharmacyItem>
}

    data class PharmacyAddList(
        override val pharmacyLocation: List<PharmacyItem> = emptyList()
    ) : ResultList

}

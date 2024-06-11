package com.mediseed.mediseed.ui.home.model.uiState

import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem

sealed interface UiState {

    open class Notice(
        val message: String
    ) : UiState

    data object ResultEmpty : Notice("결과 없음")

    sealed interface ResultList: UiState {
        val daejeonSeoguLocation: List<PharmacyItem.PharmacyInfo>
        val daejeonYuseongguLocation: List<PharmacyItem.PharmacyInfo>
}
    data class PharmacyAddList(
        override val daejeonSeoguLocation: List<PharmacyItem.PharmacyInfo> = emptyList(),
        override val daejeonYuseongguLocation: List<PharmacyItem.PharmacyInfo> = emptyList()
    ) : ResultList

}

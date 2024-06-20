package com.mediseed.mediseed.ui.home.model.uiState

import com.mediseed.mediseed.ui.home.model.pharmacyItem.GeoCode
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem

sealed interface UiState {

    open class Notice(
        val message: String
    ) : UiState

    data object ResultEmpty : Notice("결과 없음")

    sealed interface ResultList: UiState {
        val geoLatLng: List<GeoCode.GeoLatLng>
        val daejeonSeoguLocation: List<PharmacyItem.PharmacyInfo>
        val daejeonYuseongguLocation: List<PharmacyItem.PharmacyInfo>
}
    data class AddList(
        override val geoLatLng : List<GeoCode.GeoLatLng> = emptyList(),
        override val daejeonSeoguLocation: List<PharmacyItem.PharmacyInfo> = emptyList(),
        override val daejeonYuseongguLocation: List<PharmacyItem.PharmacyInfo> = emptyList()
    ) : ResultList

}

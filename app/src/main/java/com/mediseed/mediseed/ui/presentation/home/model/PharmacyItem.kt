package com.mediseed.mediseed.ui.presentation.home.model

import android.os.Parcelable
import com.mediseed.mediseed.ui.share.MarkerItem
import kotlinx.parcelize.Parcelize

interface PharmacyItem {

    @Parcelize
    data class PharmacyLocation(
        val latitude: String?,
        val longitude: String?,
        override val CollectionLocationName: String?,
        override val CollectionLocationClassificationName: String?,
        override val StreetNameAddress: String?,
        override val PhoneNumber: String?,
        override val DataDate: String?,
        override val lotNumberAddress: String?,
    ) : PharmacyItem, MarkerItem

}
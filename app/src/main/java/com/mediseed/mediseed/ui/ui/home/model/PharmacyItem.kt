package com.mediseed.mediseed.ui.ui.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface PharmacyItem {

    @Parcelize
    data class PharmacyInfo(
        val latitude: String?,
        val longitude: String?,
        val distance: Float?,
        val CollectionLocationName: String?,
        val CollectionLocationClassificationName: String?,
        val PhoneNumber: String?,
        val DataDate: String?,
        val StreetNameAddress: String?,
    ) : PharmacyItem, Parcelable

}
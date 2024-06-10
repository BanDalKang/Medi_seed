package com.mediseed.mediseed.ui.ui.home.model.PharmacyItem

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface PharmacyItem {

    @Parcelize
    data class PharmacyInfo(
        val latitude: String?,
        val longitude: String?,
        val distance: Float?,
        val collectionLocationName: String?,
        val collectionLocationClassificationName: String?,
        val dataDate: String?,
        val streetNameAddress: String?,
    ) : PharmacyItem, Parcelable


}
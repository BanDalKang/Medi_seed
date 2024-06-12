package com.mediseed.mediseed.ui.home.model.pharmacyItem

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface PharmacyItem {
    @Parcelize
    data class PharmacyInfo(
        val latitude: String?,
        val longitude: String?,
        var distance: Float?,
        val collectionLocationName: String?,
        val collectionLocationClassificationName: String?,
        val dataDate: String?,
        val streetNameAddress: String?,
        val phoneNumber: String?,
        ) : PharmacyItem, Parcelable
}
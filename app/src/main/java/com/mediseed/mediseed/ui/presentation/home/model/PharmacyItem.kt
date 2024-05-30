package com.mediseed.mediseed.ui.presentation.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface PharmacyItem {

    @Parcelize
    data class PharmacyLocation(
        val Latitude: String?,
        val longitude: String?,
    ) : PharmacyItem, Parcelable

}
package com.mediseed.mediseed.ui.home.model.pharmacyItem

import android.os.Parcelable
import com.naver.maps.geometry.LatLng
import kotlinx.parcelize.Parcelize
import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng

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
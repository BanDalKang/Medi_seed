package com.mediseed.mediseed.ui.domain.model

import com.google.gson.annotations.SerializedName

data class DataEntity(
    val EstablisherName: String?,
    val longitude: String?,
    val DataDate: String?,
    val StreetNameAddress: String?,
    val BeopJeondDongName: String?,
    val BeopJeondDongCode: Int?,
    val note: String?,
    val CollectionLocationClassificationName: String?,
    val CollectionLocationName: String?,
    val turn: Int?,
    val Latitude: String?,
    val PhoneNumber: String?,
    val lotNumberAddress: String?,
    val haengJeongDongName: String?,
    val haengJeongDongCode: Int?
)
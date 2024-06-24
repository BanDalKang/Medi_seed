package com.mediseed.mediseed.domain.model

data class DaejeonSeoguDataEntity(
    val latitude: String?,
    val longitude: String?,
    val dataDate: String?,
    val collectionLocationClassificationName: String?,
    val collectionLocationName: String?,
    val phoneNumber: String?,
    val lotNumberAddress: String?,
    val establisherName: String?,
    val streetNameAddress: String?,
    val beopJeondDongName: String?,
    val beopJeondDongCode: Long?,
    val note: String?,
    val turn: Int?,
    val haengJeongDongName: String?,
    val haengJeongDongCode: Long?
)
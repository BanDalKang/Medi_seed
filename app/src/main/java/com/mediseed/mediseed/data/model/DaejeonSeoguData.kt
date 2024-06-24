package com.mediseed.mediseed.data.model

import com.google.gson.annotations.SerializedName

data class DaejeonSeoguData(
    @SerializedName("개설자명")
    val EstablisherName: String?,
    @SerializedName("위도")
    val Latitude: String?,
    @SerializedName("경도")
    val longitude: String?,
    @SerializedName("데이터기준일자")
    val dataDate: String?,
    @SerializedName("도로명주소")
    val streetNameAddress: String?,
    @SerializedName("법정동명")
    val beopJeondDongName: String?,
    @SerializedName("법정동코드")
    val beopJeondDongCode: Long?,
    @SerializedName("비고")
    val note: String?,
    @SerializedName("수거장소구분명")
    val collectionLocationClassificationName: String?,
    @SerializedName("수거장소명")
    val collectionLocationName: String?,
    @SerializedName("순번")
    val turn: Int?,
    @SerializedName("전화번호")
    val phoneNumber: String?,
    @SerializedName("지번주소")
    val lotNumberAddress: String?,
    @SerializedName("행정동명")
    val haengJeongDongName: String?,
    @SerializedName("행정동코드")
    val haengJeongDongCode: Long?
)
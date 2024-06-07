package com.mediseed.mediseed.ui.data.model


import com.google.gson.annotations.SerializedName

data class DataResponce(
    @SerializedName("개설자명")
    val EstablisherName: String?,
    @SerializedName("위도")
    val Latitude: String?,
    @SerializedName("경도")
    val longitude: String?,
    @SerializedName("데이터기준일자")
    val DataDate: String?,
    @SerializedName("도로명주소")
    val StreetNameAddress: String?,
    @SerializedName("법정동명")
    val BeopJeondDongName: String?,
    @SerializedName("법정동코드")
    val BeopJeondDongCode: Long?,
    @SerializedName("비고")
    val note: String?,
    @SerializedName("수거장소구분명")
    val CollectionLocationClassificationName: String?,
    @SerializedName("수거장소명")
    val CollectionLocationName: String?,
    @SerializedName("순번")
    val turn: Int?,
    @SerializedName("전화번호")
    val PhoneNumber: String?,
    @SerializedName("지번주소")
    val lotNumberAddress: String?,
    @SerializedName("행정동명")
    val haengJeongDongName: String?,
    @SerializedName("행정동코드")
    val haengJeongDongCode: Long?
)
package com.mediseed.mediseed.data.model

import com.google.gson.annotations.SerializedName

data class DaejeonYuseongguData(
    @SerializedName("도로명주소")
    val streetNameAddress: String?,
    @SerializedName("시군구명")
    val siGunGuName: String?,
    @SerializedName("시도명")
    val siDoName: String?,
    @SerializedName("위치명")
    val locationName: String?,
    @SerializedName("지번주소")
    val lotNumberAddress: String?
)
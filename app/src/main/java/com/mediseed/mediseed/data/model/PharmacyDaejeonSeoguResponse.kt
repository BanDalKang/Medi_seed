package com.mediseed.mediseed.data.model


import com.google.gson.annotations.SerializedName

data class PharmacyDaejeonSeoguResponse(
    @SerializedName("currentCount")
    val currentCount: Int?,
    @SerializedName("data")
    val data: List<DaejeonSeoguData>,
    @SerializedName("matchCount")
    val matchCount: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("perPage")
    val perPage: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?
)
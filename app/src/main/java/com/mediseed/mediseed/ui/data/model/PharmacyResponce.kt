package com.mediseed.mediseed.ui.data.model


import com.google.gson.annotations.SerializedName

data class PharmacyResponce(
    @SerializedName("currentCount")
    val currentCount: Int?,
    @SerializedName("data")
    val data: List<DataResponce>,
    @SerializedName("matchCount")
    val matchCount: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("perPage")
    val perPage: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?
)
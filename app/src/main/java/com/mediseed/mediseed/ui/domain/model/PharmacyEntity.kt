package com.mediseed.mediseed.ui.domain.model

import com.google.gson.annotations.SerializedName

data class PharmacyEntity(
    val currentCount: Int?,
    val data: List<DataEntity>,
    val matchCount: Int?,
    val page: Int?,
    val perPage: Int?,
    val totalCount: Int?
)
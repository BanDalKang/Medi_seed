package com.mediseed.mediseed.domain.model

import com.google.gson.annotations.SerializedName
import com.mediseed.mediseed.data.model.DaejeonYuseongguData

data class PharmacyDaejeonYuseongguEntity(
    val currentCount: Int?,
    val data: List<DaejeonYuseongguDataEntity>,
    val matchCount: Int?,
    val page: Int?,
    val perPage: Int?,
    val totalCount: Int?
)
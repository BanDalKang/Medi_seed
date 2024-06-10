package com.mediseed.mediseed.ui.domain.model

import com.google.gson.annotations.SerializedName
import com.mediseed.mediseed.ui.data.model.DaejeonYuseongguData

data class PharmacyDaejeonYuseongguEntity(
    val currentCount: Int?,
    val data: List<DaejeonYuseongguDataEntity>,
    val matchCount: Int?,
    val page: Int?,
    val perPage: Int?,
    val totalCount: Int?
)
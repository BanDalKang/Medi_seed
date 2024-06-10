package com.mediseed.mediseed.ui.domain.model

data class PharmacyDaejeonSeoguEntity(
    val currentCount: Int?,
    val data: List<DaejeonSeoguDataEntity>,
    val matchCount: Int?,
    val page: Int?,
    val perPage: Int?,
    val totalCount: Int?
)
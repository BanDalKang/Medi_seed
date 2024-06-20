package com.mediseed.mediseed.domain.model

data class DaejeonSeoguEntity(
     val currentCount: Int?,
     val data: List<DaejeonSeoguDataEntity>,
     val matchCount: Int?,
     val page: Int?,
     val perPage: Int?,
     val totalCount: Int?
)
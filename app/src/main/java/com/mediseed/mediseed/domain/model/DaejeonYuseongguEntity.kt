package com.mediseed.mediseed.domain.model

data class DaejeonYuseongguEntity(
     val currentCount: Int?,
     val data: List<DaejeonYuseongguDataEntity>,
     val matchCount: Int?,
     val page: Int?,
     val perPage: Int?,
     val totalCount: Int?
)
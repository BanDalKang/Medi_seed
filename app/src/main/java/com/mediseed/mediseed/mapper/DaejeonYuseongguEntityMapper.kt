package com.mediseed.mediseed.mapper

import com.mediseed.mediseed.data.model.DaejeonYuseongguData
import com.mediseed.mediseed.data.model.DaejeonYuseongguResponse
import com.mediseed.mediseed.domain.model.DaejeonYuseongguDataEntity
import com.mediseed.mediseed.domain.model.DaejeonYuseongguEntity

fun DaejeonYuseongguResponse.toEntity(): DaejeonYuseongguEntity {
    return DaejeonYuseongguEntity(
        currentCount = currentCount,
        data = data.map(DaejeonYuseongguData::toEntity),
        matchCount = matchCount,
        page = page,
        perPage = perPage,
        totalCount = totalCount
    )
}

fun DaejeonYuseongguData.toEntity(): DaejeonYuseongguDataEntity {
    return DaejeonYuseongguDataEntity(
        streetNameAddress = streetNameAddress,
        siGunGuName = siGunGuName,
        siDoName = siDoName,
        locationName = locationName,
        lotNumberAddress = lotNumberAddress
    )
}


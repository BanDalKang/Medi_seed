package com.mediseed.mediseed.ui.mapper

import com.mediseed.mediseed.ui.data.model.DaejeonYuseongguData
import com.mediseed.mediseed.ui.data.model.PharmacyDaejeonYuseongguResponse
import com.mediseed.mediseed.ui.domain.model.DaejeonYuseongguDataEntity
import com.mediseed.mediseed.ui.domain.model.PharmacyDaejeonYuseongguEntity

fun PharmacyDaejeonYuseongguResponse.toEntity(): PharmacyDaejeonYuseongguEntity {
    return PharmacyDaejeonYuseongguEntity(
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


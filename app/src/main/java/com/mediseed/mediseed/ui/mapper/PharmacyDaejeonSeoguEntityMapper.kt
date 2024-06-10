package com.mediseed.mediseed.ui.mapper

import com.mediseed.mediseed.ui.data.model.DaejeonSeoguData
import com.mediseed.mediseed.ui.data.model.PharmacyDaejeonSeoguResponse
import com.mediseed.mediseed.ui.domain.model.DaejeonSeoguDataEntity
import com.mediseed.mediseed.ui.domain.model.PharmacyDaejeonSeoguEntity

fun PharmacyDaejeonSeoguResponse.toEntity(): PharmacyDaejeonSeoguEntity {
    return PharmacyDaejeonSeoguEntity(
        currentCount = currentCount,
        data = data.map(DaejeonSeoguData::toEntity),
        matchCount = matchCount,
        page = page,
        perPage = perPage,
        totalCount = totalCount
    )
}

fun DaejeonSeoguData.toEntity(): DaejeonSeoguDataEntity {
    return DaejeonSeoguDataEntity(
        establisherName = EstablisherName,
        longitude = longitude,
        dataDate = dataDate,
        streetNameAddress = streetNameAddress,
        beopJeondDongName = beopJeondDongName,
        beopJeondDongCode = beopJeondDongCode,
        note = note,
        collectionLocationClassificationName = collectionLocationClassificationName,
        collectionLocationName = collectionLocationName,
        turn = turn,
        latitude = Latitude,
        phoneNumber = phoneNumber,
        lotNumberAddress = lotNumberAddress,
        haengJeongDongName = haengJeongDongName,
        haengJeongDongCode = haengJeongDongCode
    )

}
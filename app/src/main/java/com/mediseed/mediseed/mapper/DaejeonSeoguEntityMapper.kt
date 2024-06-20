package com.mediseed.mediseed.mapper

import com.mediseed.mediseed.data.model.DaejeonSeoguData
import com.mediseed.mediseed.data.model.DaejeonSeoguResponse
import com.mediseed.mediseed.domain.model.DaejeonSeoguDataEntity
import com.mediseed.mediseed.domain.model.DaejeonSeoguEntity

fun DaejeonSeoguResponse.toEntity(): DaejeonSeoguEntity {
    return DaejeonSeoguEntity(
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
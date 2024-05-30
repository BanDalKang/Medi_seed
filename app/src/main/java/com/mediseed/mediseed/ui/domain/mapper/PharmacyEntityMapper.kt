package com.mediseed.mediseed.ui.domain.mapper

import com.mediseed.mediseed.ui.data.model.DataResponce
import com.mediseed.mediseed.ui.data.model.PharmacyResponce
import com.mediseed.mediseed.ui.domain.model.DataEntity
import com.mediseed.mediseed.ui.domain.model.PharmacyEntity

fun PharmacyResponce.toEntity(): PharmacyEntity {
    return PharmacyEntity(
        currentCount = currentCount,
        data = data.map(DataResponce::toEntity),
        matchCount = matchCount,
        page = page,
        perPage = perPage,
        totalCount = totalCount
    )
}

fun DataResponce.toEntity(): DataEntity {
    return DataEntity(
        EstablisherName=EstablisherName ,
        longitude=longitude,
        DataDate=DataDate,
        StreetNameAddress=StreetNameAddress,
        BeopJeondDongName=BeopJeondDongName,
        BeopJeondDongCode=BeopJeondDongCode,
        note=note,
        CollectionLocationClassificationName=CollectionLocationClassificationName,
        CollectionLocationName=CollectionLocationName,
        turn=turn,
        Latitude=Latitude,
        PhoneNumber=PhoneNumber,
        lotNumberAddress=lotNumberAddress,
        haengJeongDongName=haengJeongDongName,
        haengJeongDongCode=haengJeongDongCode
    )

}
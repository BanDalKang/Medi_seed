package com.mediseed.mediseed.mapper

import com.mediseed.mediseed.data.model.AddressElement
import com.mediseed.mediseed.data.model.Addresse
import com.mediseed.mediseed.data.model.GeoCodeResponse
import com.mediseed.mediseed.data.model.Meta
import com.mediseed.mediseed.domain.model.AddressElementEntity
import com.mediseed.mediseed.domain.model.AddresseEntity
import com.mediseed.mediseed.domain.model.GeoCodeEntity
import com.mediseed.mediseed.domain.model.MetaEntity

fun GeoCodeResponse.toEntity(): GeoCodeEntity {
    return GeoCodeEntity(
        addresses = addresses.map(Addresse::toEntity),
        errorMessage = errorMessage,
        meta = meta.toEntity(),
        status = status
    )
}

fun Addresse.toEntity(): AddresseEntity {
    return AddresseEntity(
        addressElements = addressElements.map(AddressElement::toEntity),
        distance = distance,
        englishAddress = englishAddress,
        jibunAddress = jibunAddress,
        roadAddress = roadAddress,
        x = x,
        y = y
    )
}

fun AddressElement.toEntity(): AddressElementEntity {
    return AddressElementEntity(
        code = code,
        longName = longName,
        shortName = shortName,
        types = types
    )
}

fun Meta.toEntity(): MetaEntity {
    return MetaEntity(
        count=count,
        page=page,
        totalCount=totalCount
    )
}

package com.mediseed.mediseed.domain.model

data class GeoCodeEntity(
    val addresses: List<AddresseEntity>,
    val errorMessage: String?,
    val meta: MetaEntity,
    val status: String?
)

data class AddresseEntity(
    val addressElements: List<AddressElementEntity>,
    val distance: Double?,
    val englishAddress: String?,
    val jibunAddress: String?,
    val roadAddress: String?,
    val x: String?,
    val y: String?
)

data class AddressElementEntity(
    val code: String?,
    val longName: String?,
    val shortName: String?,
    val types: List<String>
)

data class MetaEntity(
    val count: Int?,
    val page: Int?,
    val totalCount: Int?
)
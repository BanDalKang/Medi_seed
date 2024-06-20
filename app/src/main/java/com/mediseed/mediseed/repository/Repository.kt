package com.mediseed.mediseed.repository

import com.mediseed.mediseed.data.model.DaejeonSeoguResponse
import com.mediseed.mediseed.data.model.DaejeonYuseongguResponse
import com.mediseed.mediseed.data.model.GeoCodeResponse

interface PharmacyRepository {
    suspend fun getDaejeonSeogu(): DaejeonSeoguResponse
    suspend fun getDaejeonYuseonggu(): DaejeonYuseongguResponse

}

interface GeoCodeRepository {
    suspend fun getGeoCode(address: String?): GeoCodeResponse
}
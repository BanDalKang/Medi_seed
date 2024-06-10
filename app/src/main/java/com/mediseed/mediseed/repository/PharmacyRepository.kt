package com.mediseed.mediseed.repository

import com.mediseed.mediseed.data.model.PharmacyDaejeonSeoguResponse
import com.mediseed.mediseed.data.model.PharmacyDaejeonYuseongguResponse


interface PharmacyRepository {
    suspend fun getPharmacyDaejeonSeogu(): PharmacyDaejeonSeoguResponse
    suspend fun getPharmacyDaejeonYuseonggu(): PharmacyDaejeonYuseongguResponse
}
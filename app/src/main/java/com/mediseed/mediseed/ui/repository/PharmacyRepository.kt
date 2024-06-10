package com.mediseed.mediseed.ui.repository

import com.mediseed.mediseed.ui.data.model.PharmacyDaejeonSeoguResponse
import com.mediseed.mediseed.ui.data.model.PharmacyDaejeonYuseongguResponse


interface PharmacyRepository {
    suspend fun getPharmacyDaejeonSeogu(): PharmacyDaejeonSeoguResponse
    suspend fun getPharmacyDaejeonYuseonggu(): PharmacyDaejeonYuseongguResponse
}
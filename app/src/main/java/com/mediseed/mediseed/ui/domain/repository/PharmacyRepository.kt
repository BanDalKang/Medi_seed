package com.mediseed.mediseed.ui.domain.repository

import com.mediseed.mediseed.ui.data.model.PharmacyResponce

interface PharmacyRepository {
    suspend fun getPharmacy(): PharmacyResponce
}
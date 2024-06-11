package com.mediseed.mediseed.domain.usecase

import com.mediseed.mediseed.data.model.DaejeonSeoguData
import com.mediseed.mediseed.domain.model.DaejeonSeoguDataEntity
import com.mediseed.mediseed.domain.model.PharmacyDaejeonSeoguEntity
import com.mediseed.mediseed.domain.model.PharmacyDaejeonYuseongguEntity
import com.mediseed.mediseed.mapper.toEntity
import com.mediseed.mediseed.repository.PharmacyRepository

class PharmacyUseCase (
    private val pharmacyRepository: PharmacyRepository
) {
    suspend fun getPharmacyDaejeonSeogu(): Result<PharmacyDaejeonSeoguEntity> {
        return runCatching {
            pharmacyRepository.getPharmacyDaejeonSeogu().toEntity()
        }
    }
    suspend fun getPharmacyDaejeonYuseonggu(): Result<PharmacyDaejeonYuseongguEntity> {
        return runCatching {
            pharmacyRepository.getPharmacyDaejeonYuseonggu().toEntity()
        }
    }
}
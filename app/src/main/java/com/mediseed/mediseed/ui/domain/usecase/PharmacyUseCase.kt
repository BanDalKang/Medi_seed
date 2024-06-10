package com.mediseed.mediseed.ui.domain.usecase

import com.mediseed.mediseed.ui.data.model.DaejeonSeoguData
import com.mediseed.mediseed.ui.domain.model.DaejeonSeoguDataEntity
import com.mediseed.mediseed.ui.domain.model.PharmacyDaejeonSeoguEntity
import com.mediseed.mediseed.ui.domain.model.PharmacyDaejeonYuseongguEntity
import com.mediseed.mediseed.ui.mapper.toEntity
import com.mediseed.mediseed.ui.repository.PharmacyRepository

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
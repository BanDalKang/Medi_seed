package com.mediseed.mediseed.ui.domain.usecase

import com.mediseed.mediseed.ui.domain.mapper.toEntity
import com.mediseed.mediseed.ui.domain.model.PharmacyEntity
import com.mediseed.mediseed.ui.domain.repository.PharmacyRepository

class PharmacyUseCase (
    private val pharmacyRepository: PharmacyRepository
) {
    suspend operator fun invoke(): Result<PharmacyEntity> {
        return runCatching {
            pharmacyRepository.getPharmacy().toEntity()
        }
    }


}
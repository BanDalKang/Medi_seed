package com.mediseed.mediseed.domain.usecase

import com.mediseed.mediseed.domain.model.DaejeonSeoguEntity
import com.mediseed.mediseed.domain.model.DaejeonYuseongguEntity
import com.mediseed.mediseed.domain.model.GeoCodeEntity
import com.mediseed.mediseed.mapper.toEntity
import com.mediseed.mediseed.repository.GeoCodeRepository
import com.mediseed.mediseed.repository.PharmacyRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class PharmacyUseCase @Inject constructor(
    private val pharmacyRepository: PharmacyRepository
) {
    suspend fun getPharmacyDaejeonSeogu(): Result<DaejeonSeoguEntity> {
        return runCatching {
            pharmacyRepository.getDaejeonSeogu().toEntity()
        }
    }
    suspend fun getPharmacyDaejeonYuseonggu(): Result<DaejeonYuseongguEntity> {
        return runCatching {
            pharmacyRepository.getDaejeonYuseonggu().toEntity()
        }
    }
}

@ViewModelScoped
class GeoCodeUseCase @Inject constructor (
    private val geoCodeRepository: GeoCodeRepository
) {
    suspend operator fun invoke(address: String?): Result<GeoCodeEntity> {
        return runCatching {
            geoCodeRepository.getGeoCode(address).toEntity()
        }
    }
}
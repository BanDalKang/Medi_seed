package com.mediseed.mediseed.domain.usecase

import com.mediseed.mediseed.domain.model.DaejeonSeoguEntity
import com.mediseed.mediseed.domain.model.DaejeonYuseongguEntity
import com.mediseed.mediseed.domain.model.GeoCodeEntity
import com.mediseed.mediseed.mapper.toEntity
import com.mediseed.mediseed.repository.GeoCodeRepository
import com.mediseed.mediseed.repository.PharmacyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PharmacyUseCase @Inject constructor(
    @Named("PharmacyApi")private val pharmacyRepository: PharmacyRepository
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

class GeoCodeUseCase @Inject constructor (
    @Named("GeoCodeApi")private val geoCodeRepository: GeoCodeRepository
) {
    suspend operator fun invoke(address: String?): Result<GeoCodeEntity> {
        return runCatching {
            geoCodeRepository.getGeoCode(address).toEntity()
        }
    }
}
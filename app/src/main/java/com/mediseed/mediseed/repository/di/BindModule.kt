package com.mediseed.mediseed.repository.di

import com.mediseed.mediseed.repository.GeoCodeRepository
import com.mediseed.mediseed.repository.GeoCodeRepositoryImpl
import com.mediseed.mediseed.repository.PharmacyRepository
import com.mediseed.mediseed.repository.PharmacyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindModule {

    @ViewModelScoped
    @Binds
    @Named("PharmacyApi")
    abstract fun bindPharmacyRepository(
        repository: PharmacyRepositoryImpl
    ) : PharmacyRepository

    @ViewModelScoped
    @Binds
    @Named("GeoCodeApi")
    abstract fun bindGeoCodeRepository(
        repository: GeoCodeRepositoryImpl
    ) : GeoCodeRepository
}
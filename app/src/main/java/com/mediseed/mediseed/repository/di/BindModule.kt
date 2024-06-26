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
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindModule {

    @ViewModelScoped
    @Binds
    abstract fun bindPharmacyRepository(
        repository: PharmacyRepositoryImpl
    ): PharmacyRepository

    @ViewModelScoped
    @Binds
    abstract fun bindGeoCodeRepository(
        repository: GeoCodeRepositoryImpl
    ): GeoCodeRepository
}
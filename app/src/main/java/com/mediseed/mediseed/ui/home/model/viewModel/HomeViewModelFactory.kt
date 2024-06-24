package com.mediseed.mediseed.ui.home.model.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.mediseed.mediseed.domain.usecase.GeoCodeUseCase
import com.mediseed.mediseed.domain.usecase.PharmacyUseCase
import com.mediseed.mediseed.network.RetrofitClient
import com.mediseed.mediseed.repository.GeoCodeRepositoryImpl
import com.mediseed.mediseed.repository.PharmacyRepositoryImpl

class HomeViewModelFactory : ViewModelProvider.Factory {

    private val geoCodeUseCase by lazy {
        val geoCodeDataSource = RetrofitClient.geoCodeDataSource
        val geoCodeRepository = GeoCodeRepositoryImpl(geoCodeDataSource)
        GeoCodeUseCase(geoCodeRepository)
    }

    private val pharmacyUseCase by lazy {
        val pharmacyDataSource = RetrofitClient.pharmacyDataSource
        val pharmacyRepository = PharmacyRepositoryImpl(pharmacyDataSource)
        PharmacyUseCase(pharmacyRepository)
    }

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return HomeViewModel(geoCodeUseCase,pharmacyUseCase) as T
    }
}
package com.mediseed.mediseed.ui.home.model.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.mediseed.mediseed.network.PharmacyRetrofitClient
import com.mediseed.mediseed.repository.PharmacyRepositoryImpl
import com.mediseed.mediseed.domain.usecase.PharmacyUseCase

class HomeViewModelFactory : ViewModelProvider.Factory {

    private val pharmacyUseCase by lazy {
        val pharmacyDataSource = PharmacyRetrofitClient.pharmacyDataSource
        val pharmacyRepository = PharmacyRepositoryImpl(pharmacyDataSource)
        PharmacyUseCase(pharmacyRepository)
    }

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return HomeViewModel(pharmacyUseCase) as T
    }
}
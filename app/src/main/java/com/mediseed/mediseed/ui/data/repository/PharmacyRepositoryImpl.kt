package com.mediseed.mediseed.ui.data.repository

import com.mediseed.mediseed.ui.data.model.PharmacyResponce
import com.mediseed.mediseed.ui.data.remote.PharmacyDataSource
import com.mediseed.mediseed.ui.domain.exception.NetworkException
import com.mediseed.mediseed.ui.domain.exception.QuotaExceededException
import com.mediseed.mediseed.ui.domain.exception.TimeoutException
import com.mediseed.mediseed.ui.domain.exception.UnknownException
import com.mediseed.mediseed.ui.domain.exception.UnknownHttpException
import com.mediseed.mediseed.ui.domain.repository.PharmacyRepository
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PharmacyRepositoryImpl(
    private val pharmacyDataSource: PharmacyDataSource
) : PharmacyRepository {

    override suspend fun getPharmacy(): PharmacyResponce {
        return try {
            pharmacyDataSource.getPharmacy()
        } catch (e: HttpException) {
            val message = e.message
            throw when (val code = e.code()) {
                403 -> QuotaExceededException(message)
                else -> UnknownHttpException(code, message)
            }
        } catch (e: SocketTimeoutException) {
            throw TimeoutException(e.message)
        } catch (e: UnknownHostException) {
            throw NetworkException(e.message)
        } catch (e: Exception) {
            throw UnknownException(e.message)
        }
    }

}

package com.mediseed.mediseed.ui.repository

import com.mediseed.mediseed.ui.data.model.PharmacyDaejeonSeoguResponse
import com.mediseed.mediseed.ui.data.model.PharmacyDaejeonYuseongguResponse
import com.mediseed.mediseed.ui.data.remote.PharmacyDataSource
import com.mediseed.mediseed.ui.domain.exception.NetworkException
import com.mediseed.mediseed.ui.domain.exception.QuotaExceededException
import com.mediseed.mediseed.ui.domain.exception.TimeoutException
import com.mediseed.mediseed.ui.domain.exception.UnknownException
import com.mediseed.mediseed.ui.domain.exception.UnknownHttpException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PharmacyRepositoryImpl(
    private val pharmacyDataSource: PharmacyDataSource
) : PharmacyRepository {

    override suspend fun getPharmacyDaejeonSeogu(): PharmacyDaejeonSeoguResponse {
        return safeApiCall { pharmacyDataSource.getPharmacyDaejeonSeogu() }
    }

    override suspend fun getPharmacyDaejeonYuseonggu(): PharmacyDaejeonYuseongguResponse {
        return safeApiCall { pharmacyDataSource.getDaejeonYuseonggu() }
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): T {
        return try {
            apiCall()
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


package android.test.forexwatch.data.repository

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.core.utils.isStale
import android.test.forexwatch.data.local.dao.CurrencyRateDao
import android.test.forexwatch.data.local.mapper.toDomain
import android.test.forexwatch.data.local.mapper.toEntity
import android.test.forexwatch.data.remote.api.FixerApiService
import android.test.forexwatch.data.remote.enums.ApiErrorType
import android.test.forexwatch.data.remote.mapper.toDomain
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.repository.FixerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class FixerRepositoryImpl @Inject constructor(
    private val api: FixerApiService,
    private val dao: CurrencyRateDao
) : FixerRepository {


    override fun getRates(forceRefresh: Boolean): Flow<Resource<List<CurrencyRate>>> = flow {
        emit(Resource.Loading)

        val cachedRates = getCachedRates()
        val lastUpdate = dao.getLastUpdatedTimestamp()
        val shouldRefresh = forceRefresh || lastUpdate == null || isStale(lastUpdate, 15)

        if (cachedRates.isNotEmpty()) {
            emit(Resource.Success(cachedRates, isStale = shouldRefresh))
        }

        if (shouldRefresh) {
            emitAll(fetchAndCacheRatesFlow(cachedRates))
        }
    }


    override fun getTimeSeriesRates(
        targetCurrency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<CurrencyTimeseries>> = flow {

        emit(Resource.Loading)
        emit(fetchTimeSeries(startDate, endDate, targetCurrency))
    }


    private suspend fun getCachedRates(): List<CurrencyRate> {
        return dao.getCachedOnce().map { it.toDomain() }
    }

    private fun fetchAndCacheRatesFlow(
        fallback: List<CurrencyRate>
    ): Flow<Resource<List<CurrencyRate>>> = flow {
        val response = api.getLatestRates()

        if (!response.success) {
            val errorType = ApiErrorType.fromString(response.error?.type)
            val errorMessage = response.error?.type ?: "Unexpected error"
            emit(Resource.Error(errorMessage, fallback, errorType))
            return@flow
        }

        val now = System.currentTimeMillis()
        val rates = response.rates.map { CurrencyRate(it.key, it.value) }

        dao.clearAndInsertAll(rates.map { it.toEntity(now) })

        emit(Resource.Success(rates, isStale = false))
    }.catch { e ->
        emit(
            Resource.Error(
                message = e.localizedMessage ?: "Unexpected error",
                data = fallback,
                errorType = ApiErrorType.NetworkError
            )
        )
    }


    private suspend fun fetchTimeSeries(
        startDate: LocalDate,
        endDate: LocalDate,
        targetCurrency: String
    ): Resource<CurrencyTimeseries> {

        return try {
            val response = api.getTimeSeriesRates(
                startDate = startDate.toString(),
                endDate = endDate.toString(),
                symbols = targetCurrency
            )

            if (!response.success) {
                val errorType = ApiErrorType.fromString(response.error?.type)
                return Resource.Error(
                    message = response.error?.type ?: "Unexpected error",
                    errorType = errorType
                )
            }

            Resource.Success(response.toDomain(targetCurrency))

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error", errorType = ApiErrorType.NetworkError)
        }
    }

}

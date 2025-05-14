package android.test.forexwatch.data.repository

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.core.utils.isStale
import android.test.forexwatch.data.local.dao.CurrencyRateDao
import android.test.forexwatch.data.local.mapper.toDomain
import android.test.forexwatch.data.local.mapper.toEntity
import android.test.forexwatch.data.remote.api.FixerApiService
import android.test.forexwatch.data.remote.dto.TimeSeriesResponseDto
import android.test.forexwatch.data.remote.enums.ApiErrorType
import android.test.forexwatch.data.remote.mapper.toDomain
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.repository.FixerRepository
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.time.LocalDate
import javax.inject.Inject

class FixerRepositoryImpl @Inject constructor(
    private val api: FixerApiService,
    private val dao: CurrencyRateDao
) : FixerRepository {


    override fun getRates(forceRefresh: Boolean): Flow<Resource<List<CurrencyRate>>> = flow {
        emit(Resource.Loading)


        val cachedRates = dao.getAllRates().firstOrNull()?.map { it.toDomain() }
        val lastUpdate = dao.getLastUpdatedTimestamp()
        val shouldRefresh = forceRefresh || lastUpdate == null || isStale(lastUpdate, 15)

        if (!cachedRates.isNullOrEmpty()) {
            emit(Resource.Success(data = cachedRates, isStale = shouldRefresh))
        }

        if (shouldRefresh) {
            try {
                val response = api.getLatestRates()



                val rateMap = response.rates

                if (response.success.not() || response.rates == null) {
                    val errorType = ApiErrorType.fromString(response.error?.type)
                    val errorMessage = response.error?.type ?: "Unexpected error"

                    emit(
                        Resource.Error(
                            message = errorMessage,
                            data = cachedRates,
                            errorType = errorType
                        )
                    )
                    return@flow
                }


                val rates = rateMap.map { (code, rate) ->
                    CurrencyRate(currencyCode = code, rate = rate)
                }

                val now = System.currentTimeMillis()
                dao.clearAndInsertAll(rates.map { it.toEntity(now) })

                emit(Resource.Success(data = rates, isStale = false))
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        message = e.localizedMessage ?: "Unexpected error",
                        data = cachedRates,
                        errorType = ApiErrorType.NetworkError
                    )
                )
            }
        }

    }


    override fun getTimeSeriesRates(
        targetCurrency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<CurrencyTimeseries>> = flow {
        emit(Resource.Loading)

        try {
            val response: TimeSeriesResponseDto = api.getTimeSeriesRates(
                startDate = startDate.toString(),
                endDate = endDate.toString(),
                symbols = targetCurrency
            )
            if (response.success.not() ) {
                val errorType = ApiErrorType.fromString(response.error?.type)
                val errorMessage = response.error?.type ?: "Unexpected error"

                emit(
                    Resource.Error(
                        message = errorMessage,
                        data = null,
                        errorType = errorType
                    )
                )
                return@flow
            }

            val currencyTimeseries: CurrencyTimeseries = response.toDomain(targetCurrency)
            emit(Resource.Success(currencyTimeseries))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", errorType = ApiErrorType.NetworkError))
        }
    }


}

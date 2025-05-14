package android.test.forexwatch.fake

import android.content.Context
import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.data.remote.dto.FixerRatesResponseDto
import android.test.forexwatch.data.remote.dto.TimeSeriesResponseDto
import android.test.forexwatch.data.remote.enums.ApiErrorType
import android.test.forexwatch.data.remote.mapper.toDomain
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.repository.FixerRepository
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class MockFixerRepository @Inject constructor(private val context: Context) : FixerRepository {

    var shouldFail = false

    override fun getRates(forceRefresh: Boolean): Flow<Resource<List<CurrencyRate>>> =
        flow {
            if (shouldFail) {
                throw IllegalStateException("Forced failure")
            }

            emit(Resource.Loading)

            val json = context.assets.open("latest_rates.json")
                .bufferedReader().use { it.readText() }

            val gson = Gson()
            val dto = gson.fromJson(json, FixerRatesResponseDto::class.java)

            if (!dto.success) {
                throw IllegalStateException("API error: ${dto.error?.type}")
            }

            val list = dto.rates.map { CurrencyRate(it.key, it.value) }
            emit(Resource.Success(list))
        }.catch { e ->
            emit(
                Resource.Error(
                    message = "Error loading mock data: ${e.localizedMessage}",
                    data = null,
                    errorType = ApiErrorType.NetworkError
                )
            )
        }


    override fun getTimeSeriesRates(
        targetCurrency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<CurrencyTimeseries>> = flow {

        if (shouldFail) {
            throw IllegalStateException("Forced failure")
        }

        emit(Resource.Loading)

        val json = context.assets.open("timeseries_rates.json")
            .bufferedReader().use { it.readText() }

        val gson = Gson()
        val dto = gson.fromJson(json, TimeSeriesResponseDto::class.java)
        if (!dto.success) {
            throw IllegalStateException("Mock API error")
        }

        val result = dto.toDomain("UAH")
        emit(Resource.Success(result))
    }.catch { e ->
        emit(Resource.Error("Mock load error: ${e.message}", data = null, errorType = ApiErrorType.NetworkError))
    }


}
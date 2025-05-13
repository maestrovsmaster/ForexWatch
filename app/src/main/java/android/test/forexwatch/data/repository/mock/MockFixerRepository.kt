package android.test.forexwatch.data.repository.mock

import android.content.Context
import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.data.remote.dto.FixerRatesResponseDto
import android.test.forexwatch.data.remote.enums.ApiErrorType
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.repository.FixerRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockFixerRepository @Inject constructor(private val context: Context) : FixerRepository {

    override fun getRates(forceRefresh: Boolean): Flow<Resource<List<CurrencyRate>>> = flow {
        emit(Resource.Loading)

        try {
            val json = context.assets.open("latest_rates.json")
                .bufferedReader().use { it.readText() }

            val gson = Gson()
            val dto = gson.fromJson(json, FixerRatesResponseDto::class.java)

            if (!dto.success) {
                emit(Resource.Error("API error: ${dto.error?.type}", data = null, errorType = ApiErrorType.NetworkError))
                return@flow
            }

            val list = dto.rates.map { CurrencyRate(it.key, it.value) }
            emit(Resource.Success(list))
        } catch (e: Exception) {
            emit(Resource.Error("Error loading mock data: ${e.localizedMessage}", data = null, errorType = ApiErrorType.NetworkError))
        }
    }
}

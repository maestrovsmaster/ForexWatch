package android.test.forexwatch.data.repository

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.core.utils.isStale
import android.test.forexwatch.data.local.dao.CurrencyRateDao
import android.test.forexwatch.data.local.mapper.toDomain
import android.test.forexwatch.data.local.mapper.toEntity
import android.test.forexwatch.data.remote.api.FixerApiService
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.repository.FixerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FixerRepositoryImpl @Inject constructor(
    private val api: FixerApiService,
    private val dao: CurrencyRateDao
) : FixerRepository {


    override fun getRates(): Flow<Resource<List<CurrencyRate>>> = flow {
        emit(Resource.Loading())

        val cachedRates = dao.getAllRates().map { list -> list.map { it.toDomain() } }
        emitAll(cachedRates.map { Resource.Success(it) })

        val lastUpdate = dao.getLastUpdatedTimestamp()
        val shouldRefresh = lastUpdate == null || isStale(lastUpdate, 15)

        if (shouldRefresh) {
            try {
                val response = api.getLatestRates()
                val rates = response.rates.map { (code, rate) ->
                    CurrencyRate(currencyCode = code, rate = rate)
                }

                val now = System.currentTimeMillis()
                dao.clearAndInsertAll(rates.map { it.toEntity(now) })

                emit(Resource.Success(rates))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
            }
        }
    }


}

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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
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
                val rates = response.rates.map { (code, rate) ->
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
                        isNetworkError = true
                    )
                )
            }
        }
    }




}

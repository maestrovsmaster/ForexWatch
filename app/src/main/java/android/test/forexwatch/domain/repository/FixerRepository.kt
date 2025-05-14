package android.test.forexwatch.domain.repository

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.model.CurrencyTimeseries
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface FixerRepository {

    fun getRates(forceRefresh: Boolean): Flow<Resource<List<CurrencyRate>>>

    fun getTimeSeriesRates(
        targetCurrency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<CurrencyTimeseries>>

}
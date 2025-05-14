package android.test.forexwatch.fake

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.model.DailyRate
import android.test.forexwatch.domain.usecase.time_series_use_case.GetTimeSeriesUseCase
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeGetTimeSeriesUseCase : GetTimeSeriesUseCase {

    var result: Flow<Resource<CurrencyTimeseries>> = flowOf()

    override fun invoke(
        targetCurrency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<CurrencyTimeseries>> {
        return result
    }
}


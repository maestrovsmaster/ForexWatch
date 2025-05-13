package android.test.forexwatch.fake

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.model.DailyRate
import android.test.forexwatch.domain.usecase.time_series_use_case.GetTimeSeriesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class FakeGetTimeSeriesUseCase : GetTimeSeriesUseCase {
    override fun invoke(
        targetCurrency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<CurrencyTimeseries>> = flow {
        emit(Resource.Loading)

        val rates = listOf(
            DailyRate(startDate, 41.20),
            DailyRate(startDate.plusDays(1), 41.35),
            DailyRate(startDate.plusDays(2), 41.18),
            DailyRate(startDate.plusDays(3), 41.40),
            DailyRate(startDate.plusDays(4), 41.25),
        )

        val timeseries = CurrencyTimeseries(
            base = "EUR",
            target = targetCurrency,
            rates = rates
        )

        delay(300) //Simulate delay
        emit(Resource.Success(timeseries))
    }
}

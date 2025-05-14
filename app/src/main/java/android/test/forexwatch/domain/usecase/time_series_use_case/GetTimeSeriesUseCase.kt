package android.test.forexwatch.domain.usecase.time_series_use_case

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyTimeseries
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetTimeSeriesUseCase {
    operator fun invoke(
        targetCurrency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<CurrencyTimeseries>>
}

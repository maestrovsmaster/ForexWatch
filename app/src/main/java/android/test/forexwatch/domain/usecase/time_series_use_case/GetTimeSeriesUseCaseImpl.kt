package android.test.forexwatch.domain.usecase.time_series_use_case

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.repository.FixerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTimeSeriesUseCaseImpl @Inject constructor(
    private val repository: FixerRepository
) : GetTimeSeriesUseCase {

    override fun invoke(
        targetCurrency: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<CurrencyTimeseries>> {
        return repository.getTimeSeriesRates(targetCurrency, startDate, endDate)
    }
}

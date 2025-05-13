package android.test.forexwatch.domain.usecase.get_rates_use_case

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface GetRatesUseCase {
    operator fun invoke(forceRefresh: Boolean = false): Flow<Resource<List<CurrencyRate>>>
}
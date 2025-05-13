package android.test.forexwatch.fake

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.usecase.get_rates_use_case.GetRatesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetRatesUseCase : GetRatesUseCase{
    override fun invoke(forceRefresh: Boolean): Flow<Resource<List<CurrencyRate>>> {
        return flowOf(Resource.Success(
            listOf(
                CurrencyRate("USD", 1.0),
                CurrencyRate("EUR", 0.9),
                CurrencyRate("GBP", 0.8)
            )
        ))
    }

}
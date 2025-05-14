package android.test.forexwatch.domain.usecase.rates_use_case

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.repository.FixerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRatesUseCaseImpl @Inject constructor(
    private val repository: FixerRepository
) : GetRatesUseCase {

    override fun invoke(forceRefresh: Boolean): Flow<Resource<List<CurrencyRate>>> {
        return repository.getRates(forceRefresh)
    }

}

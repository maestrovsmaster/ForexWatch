package android.test.forexwatch.domain.usecase

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.repository.FixerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(
    private val repository: FixerRepository
) {
    operator fun invoke(): Flow<Resource<List<CurrencyRate>>> {
        return repository.getRates()
    }
}

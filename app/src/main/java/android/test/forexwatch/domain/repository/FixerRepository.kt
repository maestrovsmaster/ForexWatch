package android.test.forexwatch.domain.repository

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface FixerRepository {
    fun getRates(forceRefresh: Boolean): Flow<Resource<List<CurrencyRate>>>
}
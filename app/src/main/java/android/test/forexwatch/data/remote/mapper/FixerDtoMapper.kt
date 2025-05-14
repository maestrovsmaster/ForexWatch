package android.test.forexwatch.data.remote.mapper

import android.test.forexwatch.data.remote.dto.FixerRatesResponseDto
import android.test.forexwatch.domain.model.CurrencyRate

fun FixerRatesResponseDto.toDomainModel(): List<CurrencyRate> {

    return rates.map { (currency, rate) ->
        CurrencyRate(currencyCode = currency, rate = rate)
    }
}
package android.test.forexwatch.data.local.mapper
import android.test.forexwatch.data.local.entity.CurrencyRateEntity
import android.test.forexwatch.domain.model.CurrencyRate

fun CurrencyRateEntity.toDomain(): CurrencyRate {

    return CurrencyRate(
        currencyCode = currencyCode,
        rate = rate
    )

}

fun CurrencyRate.toEntity(currentTime: Long): CurrencyRateEntity {

    return CurrencyRateEntity(
        currencyCode = currencyCode,
        rate = rate,
        updatedAt = currentTime
    )

}

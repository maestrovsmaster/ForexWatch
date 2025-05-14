package android.test.forexwatch.data.local.mapper

import android.test.forexwatch.data.local.entity.CurrencyRateEntity
import android.test.forexwatch.domain.model.CurrencyRate
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyRateLocalMapperTest {

    @Test
    fun `toEntity maps correctly`() {
        val domain = CurrencyRate("USD", 1.23)
        val timestamp = 123456789L

        val entity = domain.toEntity(currentTime = timestamp)
        assertEquals("USD", entity.currencyCode)
        assertEquals(1.23, entity.rate, 0.0001)
        assertEquals(123456789L, entity.updatedAt)
    }

    @Test
    fun `toDomain maps correctly`() {
        val entity = CurrencyRateEntity("EUR", 0.89, 987654321L)

        val domain = entity.toDomain()

        assertEquals("EUR", domain.currencyCode)
        assertEquals(0.89, domain.rate, 0.0001)
    }
}

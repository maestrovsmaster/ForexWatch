package android.test.forexwatch.data.remote.mapper

import android.test.forexwatch.data.remote.dto.FixerRatesResponseDto
import junit.framework.TestCase.assertEquals
import org.junit.Test


class FixerDtoMapperTest {

    @Test
    fun `toDomainModel maps DTO to domain model correctly`() {

        val dto = FixerRatesResponseDto(
            success = true,
            timestamp = 123456L,
            base = "EUR",
            date = "2024-05-01",
            rates = mapOf(
                "USD" to 1.12,
                "GBP" to 0.87
            ),
            error = null
        )

        val result = dto.toDomainModel()

        assertEquals(2, result.size)
        assertEquals("USD", result[0].currencyCode)
        assertEquals(1.12, result[0].rate, 0.001)
        assertEquals("GBP", result[1].currencyCode)
        assertEquals(0.87, result[1].rate, 0.001)
    }
}



package android.test.forexwatch.data.remote.mapper


import android.test.forexwatch.data.remote.dto.TimeSeriesResponseDto
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class TimeSeriesMapperTest {

    @Test
    fun `toDomain maps time series data correctly`() {

        val dto = TimeSeriesResponseDto(
            success = true,
            timeseries = true,
            startDate = "2024-05-01",
            endDate = "2024-05-03",
            base = "EUR",
            rates = mapOf(
                "2024-05-01" to mapOf("USD" to 1.1),
                "2024-05-02" to mapOf("USD" to 1.12),
                "2024-05-03" to mapOf("USD" to 1.15)
            )
        )

        val result = dto.toDomain("USD")

        assertEquals("EUR", result.base)
        assertEquals("USD", result.target)
        assertEquals(3, result.rates.size)
        assertEquals(LocalDate.parse("2024-05-01"), result.rates[0].date)
        assertEquals(1.1, result.rates[0].rate, 0.001)
    }

    @Test
    fun `toDomain ignores dates with missing or unparsable data`() {

        val dto = TimeSeriesResponseDto(
            success = true,
            timeseries = true,
            startDate = "2024-05-01",
            endDate = "2024-05-03",
            base = "EUR",
            rates = mapOf(
                "2024-05-01" to mapOf("USD" to 1.1),
                "2024-05-02" to mapOf(),
                "invalid-date" to mapOf("USD" to 1.13)
            )
        )

        val result = dto.toDomain("USD")

        assertEquals(1, result.rates.size)
        assertEquals(LocalDate.parse("2024-05-01"), result.rates[0].date)
        assertEquals(1.1, result.rates[0].rate, 0.001)
    }
}

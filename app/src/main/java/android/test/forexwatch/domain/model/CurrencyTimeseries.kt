package android.test.forexwatch.domain.model

import java.time.LocalDate

data class CurrencyTimeseries(
    val base: String,
    val target: String,
    val rates: List<DailyRate>
)

data class DailyRate(
    val date: LocalDate,
    val rate: Double
)
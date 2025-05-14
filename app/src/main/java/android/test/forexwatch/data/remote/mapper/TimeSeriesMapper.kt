package android.test.forexwatch.data.remote.mapper

import android.test.forexwatch.data.remote.dto.TimeSeriesResponseDto
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.model.DailyRate
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun TimeSeriesResponseDto.toDomain(targetCurrency: String): CurrencyTimeseries {
    val parsedRates = rates.mapNotNull { (dateString, rateMap) ->
        val rate = rateMap[targetCurrency] ?: return@mapNotNull null

        try {
            val date = LocalDate.parse(dateString)
            DailyRate(date = date, rate = rate)
        } catch (e: DateTimeParseException) {
            null
        }
    }.sortedBy { it.date }


    return CurrencyTimeseries(
        base = base,
        target = targetCurrency,
        rates = parsedRates
    )
}

package android.test.forexwatch.presentation.screens.utils

import android.test.forexwatch.domain.model.DailyRate
import com.github.mikephil.charting.data.Entry
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun prepareChartData(
    allRates: List<DailyRate>,
    startDate: LocalDate,
    endDate: LocalDate
): Pair<List<Entry>, List<String>> {
    val filteredRates = allRates.filter { it.date in startDate..endDate }

    val entries = filteredRates.mapIndexed { i, rate ->
        Entry(i.toFloat(), rate.rate.toFloat())
    }

    val labels = filteredRates.map {
        it.date.format(DateTimeFormatter.ofPattern("dd.MM"))
    }

    return entries to labels
}
package android.test.forexwatch.presentation.state

import android.test.forexwatch.domain.model.CurrencyTimeseries
import java.time.LocalDate

sealed class TimeSeriesUiState {

    abstract val targetCurrency: String
    abstract val startDate: LocalDate
    abstract val endDate: LocalDate
}

data class TimeSeriesLoadingState(
    override val targetCurrency: String,
    override val startDate: LocalDate,
    override val endDate: LocalDate,
    val fromUser: Boolean = false
) : TimeSeriesUiState()

data class TimeSeriesSuccessState(
    val series: CurrencyTimeseries,
    override val targetCurrency: String,
    override val startDate: LocalDate,
    override val endDate: LocalDate
) : TimeSeriesUiState()

data class TimeSeriesErrorState(
    val message: String,
    val fallbackSeries: CurrencyTimeseries? = null,
    override val targetCurrency: String,
    override val startDate: LocalDate,
    override val endDate: LocalDate
) : TimeSeriesUiState()

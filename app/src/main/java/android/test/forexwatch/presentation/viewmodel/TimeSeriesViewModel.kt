package android.test.forexwatch.presentation.viewmodel

import android.test.forexwatch.core.logging.Logger
import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.usecase.time_series_use_case.GetTimeSeriesUseCase
import android.test.forexwatch.presentation.state.TimeSeriesErrorState
import android.test.forexwatch.presentation.state.TimeSeriesLoadingState
import android.test.forexwatch.presentation.state.TimeSeriesSuccessState
import android.test.forexwatch.presentation.state.TimeSeriesUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TimeSeriesViewModel @Inject constructor(
    private val getTimeSeriesUseCase: GetTimeSeriesUseCase,
    private val logger: Logger
) : ViewModel() {

    private val _uiState = MutableStateFlow<TimeSeriesUiState>(
        TimeSeriesLoadingState(
            targetCurrency = "UAH",
            startDate = LocalDate.now().minusDays(7),
            endDate = LocalDate.now()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadSeries()
    }

    fun loadSeries(
        targetCurrency: String = _uiState.value.targetCurrency,
        startDate: LocalDate = _uiState.value.startDate,
        endDate: LocalDate = _uiState.value.endDate,
        fromUser: Boolean = false
    ) {
        _uiState.value = TimeSeriesLoadingState(
            targetCurrency = targetCurrency,
            startDate = startDate,
            endDate = endDate,
            fromUser = fromUser
        )

        viewModelScope.launch {
            getTimeSeriesUseCase(targetCurrency, startDate, endDate).collect { result ->
                _uiState.value = when (result) {
                    is Resource.Loading -> TimeSeriesLoadingState(
                        targetCurrency = targetCurrency,
                        startDate = startDate,
                        endDate = endDate,
                        fromUser = fromUser
                    )

                    is Resource.Success -> TimeSeriesSuccessState(
                        series = result.data,
                        targetCurrency = targetCurrency,
                        startDate = startDate,
                        endDate = endDate
                    )

                    is Resource.Error -> TimeSeriesErrorState(
                        message = result.message,
                        fallbackSeries = result.data,
                        targetCurrency = targetCurrency,
                        startDate = startDate,
                        endDate = endDate
                    )
                }
            }
        }
    }

    fun updateCurrency(newCurrency: String) {
        loadSeries(
            targetCurrency = newCurrency,
            startDate = _uiState.value.startDate,
            endDate = _uiState.value.endDate,
            fromUser = true
        )
    }

    fun updateDateRange(start: LocalDate, end: LocalDate) {
        val (safeStart, safeEnd) = if (start.isAfter(end)) {
            start to start
        } else {
            start to end
        }

        loadSeries(
            targetCurrency = _uiState.value.targetCurrency,
            startDate = safeStart,
            endDate = safeEnd,
            fromUser = true
        )
    }

}

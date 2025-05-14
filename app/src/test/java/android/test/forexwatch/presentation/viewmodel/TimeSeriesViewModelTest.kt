package android.test.forexwatch.presentation.viewmodel

import android.test.forexwatch.core.connectivity.ConnectivityObserver
import android.test.forexwatch.core.logging.LogTags
import android.test.forexwatch.core.logging.Logger
import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.data.local.dao.CurrencyRateDao
import android.test.forexwatch.data.local.entity.CurrencyRateEntity
import android.test.forexwatch.data.remote.api.FixerApiService
import android.test.forexwatch.data.remote.dto.FixerErrorDto
import android.test.forexwatch.data.remote.dto.FixerRatesResponseDto
import android.test.forexwatch.data.remote.dto.TimeSeriesResponseDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import android.test.forexwatch.data.remote.enums.ApiErrorType
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.model.DailyRate
import android.test.forexwatch.domain.usecase.rates_use_case.GetRatesUseCase
import android.test.forexwatch.fake.FakeGetTimeSeriesUseCase
import android.test.forexwatch.fake.FakeLogger
import android.test.forexwatch.presentation.state.SuccessUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import android.test.forexwatch.presentation.state.ErrorUiState
import android.test.forexwatch.presentation.state.LoadingUiState
import android.test.forexwatch.presentation.state.TimeSeriesErrorState
import android.test.forexwatch.presentation.state.TimeSeriesSuccessState
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class TimeSeriesViewModelTest {

    private lateinit var viewModel: TimeSeriesViewModel
    private lateinit var fakeUseCase: FakeGetTimeSeriesUseCase
    private lateinit var fakeLogger: FakeLogger

    @Before
    fun setUp() {
        fakeUseCase = FakeGetTimeSeriesUseCase()
        fakeLogger = FakeLogger()
        viewModel = TimeSeriesViewModel(fakeUseCase, fakeLogger)
    }

    @Test
    fun `updateCurrency emits success with correct target`() = runTest {
        val today = LocalDate.now()

        val currencyData = CurrencyTimeseries(
            base = "EUR",
            target = "PLN",
            rates = listOf(DailyRate(today, 4.5))
        )

        fakeUseCase.result = flowOf(Resource.Success(currencyData))

        viewModel.updateCurrency("PLN")

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TimeSeriesSuccessState)
        state as TimeSeriesSuccessState
        assertEquals("PLN", state.targetCurrency)
    }

    @Test
    fun `updateDateRange emits success with correct dates`() = runTest {
        val today = LocalDate.now()
        val start = today.minusDays(5)
        val end = today

        val currencyData = CurrencyTimeseries(
            base = "EUR",
            target = "UAH",
            rates = listOf(DailyRate(end, 39.0))
        )

        fakeUseCase.result = flowOf(Resource.Success(currencyData))

        viewModel.updateDateRange(start, end)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TimeSeriesSuccessState)
        state as TimeSeriesSuccessState
        assertEquals(start, state.startDate)
        assertEquals(end, state.endDate)
    }


    @Test
    fun `updateDateRange swaps dates when start is after end`() = runTest {
        val today = LocalDate.now()
        val start = today
        val end = today.minusDays(3)

        val currencyData = CurrencyTimeseries(
            base = "EUR",
            target = "UAH",
            rates = listOf(DailyRate(start, 40.0))
        )

        fakeUseCase.result = flowOf(Resource.Success(currencyData))

        viewModel.updateDateRange(start, end)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TimeSeriesSuccessState)
        state as TimeSeriesSuccessState
        assertEquals(start, state.startDate)
        assertEquals(start, state.endDate) // Бо замінено на start
    }

    @Test
    fun `loadSeries emits error state with fallback data`() = runTest {
        val today = LocalDate.now()
        val fallback = CurrencyTimeseries(
            base = "EUR",
            target = "USD",
            rates = listOf(DailyRate(today, 39.0))
        )

        fakeUseCase.result = flowOf(
            Resource.Loading,
            Resource.Error(
                message = "Timeout",
                data = fallback,
                errorType = ApiErrorType.UnexpectedError
            )
        )

        viewModel.loadSeries("USD", today.minusDays(7), today)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TimeSeriesErrorState)
        state as TimeSeriesErrorState
        assertEquals("Timeout", state.message)
        assertEquals(fallback, state.fallbackSeries)
    }

    @Test
    fun `loadSeries with default parameters emits success`() = runTest {
        val today = LocalDate.now()
        val currencyData = CurrencyTimeseries(
            base = "EUR",
            target = "UAH",
            rates = listOf(DailyRate(today, 41.0))
        )

        fakeUseCase.result = flowOf(Resource.Success(currencyData))

        viewModel.loadSeries()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TimeSeriesSuccessState)
        assertEquals("UAH", (state as TimeSeriesSuccessState).targetCurrency)
    }

}

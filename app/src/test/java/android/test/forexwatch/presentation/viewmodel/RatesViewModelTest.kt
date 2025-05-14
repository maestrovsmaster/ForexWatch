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
import android.test.forexwatch.domain.usecase.rates_use_case.GetRatesUseCase
import android.test.forexwatch.presentation.state.SuccessUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import android.test.forexwatch.presentation.state.ErrorUiState
import android.test.forexwatch.presentation.state.LoadingUiState
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertFalse

@ExperimentalCoroutinesApi
class RatesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: RatesViewModel
    private lateinit var getRatesUseCase: GetRatesUseCase
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var logger: Logger

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getRatesUseCase = mockk()
        connectivityObserver = mockk()
        logger = mockk(relaxed = true)


        every { connectivityObserver.observe() } returns flowOf(true)

        viewModel = RatesViewModel(
            getRatesUseCase = getRatesUseCase,
            connectivityObserver = connectivityObserver,
            logger = logger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadRates emits Loading and then Success`() = runTest {
        val rates = listOf(CurrencyRate("USD", 1.1))
        val flow = flowOf(
            Resource.Loading,
            Resource.Success(rates, isStale = false)
        )

        every { getRatesUseCase(false) } returns flow

        viewModel.loadRates()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SuccessUiState)
        state as SuccessUiState
        assertEquals(rates, state.rates)
        assertEquals(false, state.isStale)
        assertEquals(1.0, state.baseCurrencyAmount, 0.0001)

    }

    @Test
    fun `loadRates emits Loading and then Error`() = runTest {
        val fallbackRates = listOf(CurrencyRate("USD", 1.1))
        val flow = flowOf(
            Resource.Loading,
            Resource.Error(
                message = "Failed",
                data = fallbackRates,
                errorType = ApiErrorType.UsageLimitReached
            )
        )
        every { getRatesUseCase(false) } returns flow
        every { connectivityObserver.observe() } returns flowOf(false)

        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        viewModel.loadRates()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is ErrorUiState)
        state as ErrorUiState
        assertEquals("Failed", state.message)
        assertEquals(fallbackRates, state.rates)
        assertFalse(state.isConnected)
        assertTrue(state.showLimitReached)
    }

    @Test
    fun `updateBaseAmount updates baseCurrencyAmount in SuccessUiState`() = runTest {
        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        viewModel.setTestState(
            SuccessUiState(
                rates = listOf(),
                isStale = false,
                isConnected = true,
                baseCurrencyCode = "EUR",
                baseCurrencyAmount = 1.0
            )
        )

        viewModel.updateBaseAmount(5.5)

        val state = viewModel.uiState.value
        assertTrue(state is SuccessUiState)
        state as SuccessUiState
        assertEquals(5.5, state.baseCurrencyAmount, 0.0001)
    }


    @Test
    fun `updateBaseAmount updates baseCurrencyAmount in ErrorUiState`() = runTest {
        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        viewModel.setTestState(
            ErrorUiState(
                rates = listOf(),
                message = "Network error",
                isConnected = false,
                showRefresh = true,
                showLimitReached = false,
                baseCurrencyCode = "EUR",
                baseCurrencyAmount = 1.0
            )
        )

        viewModel.updateBaseAmount(9.9)

        val state = viewModel.uiState.value
        assertTrue(state is ErrorUiState)
        state as ErrorUiState
        assertEquals(9.9, state.baseCurrencyAmount, 0.0001)
    }


    @Test
    fun `observeConnectivity logs connectivity changes`() = runTest {
        val connectivityFlow = MutableSharedFlow<Boolean>(replay = 1)
        every { connectivityObserver.observe() } returns connectivityFlow

        val messages = mutableListOf<String>()
        every { logger.d(eq(LogTags.CONNECTIVITY), capture(messages)) } just Runs

        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        advanceUntilIdle()

        connectivityFlow.emit(false)
        connectivityFlow.emit(true)

        advanceUntilIdle()

        assertEquals(
            listOf("Internet connectivity: false", "Internet connectivity: true"),
            messages
        )
    }

    @Test
    fun `updateBaseAmount updates amount in SuccessUiState`() = runTest {
        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        viewModel.setTestState(
            SuccessUiState(
                rates = emptyList(),
                isStale = false,
                isConnected = true,
                baseCurrencyCode = "EUR",
                baseCurrencyAmount = 1.0
            )
        )

        viewModel.updateBaseAmount(3.14)

        val state = viewModel.uiState.value
        assertTrue(state is SuccessUiState)
        assertEquals(3.14, (state as SuccessUiState).baseCurrencyAmount, 0.0001)
    }

    @Test
    fun `updateBaseAmount updates amount in ErrorUiState`() = runTest {
        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        viewModel.setTestState(
            ErrorUiState(
                rates = emptyList(),
                message = "error",
                isConnected = false,
                showRefresh = true,
                showLimitReached = false,
                baseCurrencyCode = "EUR",
                baseCurrencyAmount = 1.0
            )
        )

        viewModel.updateBaseAmount(7.77)

        val state = viewModel.uiState.value
        assertTrue(state is ErrorUiState)
        assertEquals(7.77, (state as ErrorUiState).baseCurrencyAmount, 0.0001)
    }

    @Test
    fun `searchRates updates query in SuccessUiState`() = runTest {
        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        viewModel.setTestState(
            SuccessUiState(
                rates = emptyList(),
                isStale = false,
                isConnected = true,
                baseCurrencyCode = "EUR",
                baseCurrencyAmount = 1.0
            )
        )

        viewModel.searchRates("usd")

        val state = viewModel.uiState.value
        assertTrue(state is SuccessUiState)
        assertEquals("usd", (state as SuccessUiState).searchQuery)
    }


    @Test
    fun `searchRates updates query in ErrorUiState`() = runTest {
        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        viewModel.setTestState(
            ErrorUiState(
                rates = emptyList(),
                message = "fail",
                isConnected = true,
                showRefresh = false,
                showLimitReached = false,
                baseCurrencyCode = "EUR",
                baseCurrencyAmount = 1.0
            )
        )

        viewModel.searchRates("btc")

        val state = viewModel.uiState.value
        assertTrue(state is ErrorUiState)
        assertEquals("btc", (state as ErrorUiState).searchQuery)
    }


    @Test
    fun `updateBaseAmount and searchRates do nothing for LoadingUiState`() = runTest {
        val viewModel = RatesViewModel(getRatesUseCase, connectivityObserver, logger)

        viewModel.setTestState(LoadingUiState(fromUser = true))

        viewModel.updateBaseAmount(42.0)
        viewModel.searchRates("eth")

        val state = viewModel.uiState.value
        assertTrue(state is LoadingUiState)
        assertEquals(true, (state as LoadingUiState).fromUser)
    }
}

package android.test.forexwatch.presentation.viewmodel


import android.test.forexwatch.core.logging.LogTags
import android.test.forexwatch.core.logging.Logger
import android.test.forexwatch.core.connectivity.ConnectivityObserver
import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.data.remote.enums.ApiErrorType
import android.test.forexwatch.domain.usecase.rates_use_case.GetRatesUseCase
import android.test.forexwatch.fake.FakeConnectivityObserver
import android.test.forexwatch.fake.FakeGetRatesUseCase
import android.test.forexwatch.fake.FakeLogger
import android.test.forexwatch.presentation.state.ErrorUiState
import android.test.forexwatch.presentation.state.LoadingUiState
import android.test.forexwatch.presentation.state.RatesUiState
import android.test.forexwatch.presentation.state.SuccessUiState
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val logger: Logger,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RatesUiState>(LoadingUiState(fromUser = false))
    val uiState = _uiState.asStateFlow()

    private var isConnected = true

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { connected ->
                isConnected = connected
                logger.d(LogTags.CONNECTIVITY, "Internet connectivity: $connected")
            }
        }
    }

    fun loadRates(forceRefresh: Boolean = false) {

        viewModelScope.launch {
            getRatesUseCase(forceRefresh).collect { result ->

                _uiState.value = when (result) {
                    is Resource.Loading -> LoadingUiState(fromUser = forceRefresh)

                    is Resource.Success -> SuccessUiState(
                        rates = result.data,
                        isStale = result.isStale,
                        isConnected = isConnected,
                        baseCurrencyCode = "EUR",
                        baseCurrencyAmount = 1.0
                    )

                    is Resource.Error -> ErrorUiState(
                        rates = result.data,
                        message = result.message,
                        isConnected = isConnected,
                        showRefresh = true,
                        showLimitReached = result.errorType == ApiErrorType.UsageLimitReached,
                        baseCurrencyCode = "EUR",
                        baseCurrencyAmount = 1.0
                    )
                }
            }
        }
    }

    fun updateBaseAmount(amount: Double) {
        val currentUiState = _uiState.value
        _uiState.value = when (currentUiState) {
            is SuccessUiState -> currentUiState.copy(baseCurrencyAmount = amount)
            is ErrorUiState -> currentUiState.copy(baseCurrencyAmount = amount)
            is LoadingUiState -> currentUiState.copy()
        }
    }

    fun searchRates(query: String) {
        val currentUiState = _uiState.value
        _uiState.value = when (currentUiState) {
            is SuccessUiState -> currentUiState.copy(searchQuery = query)
            is ErrorUiState -> currentUiState.copy(searchQuery = query)
            is LoadingUiState -> currentUiState.copy()
        }
    }



    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setTestState(state: RatesUiState) {
        _uiState.value = state
    }
}

package android.test.forexwatch.presentation.viewmodel


import android.test.forexwatch.core.logging.LogTags
import android.test.forexwatch.core.logging.Logger
import android.test.forexwatch.core.connectivity.ConnectivityObserver
import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.data.remote.enums.ApiErrorType
import android.test.forexwatch.domain.usecase.GetRatesUseCase
import android.test.forexwatch.presentation.state.RatesUiState
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
    private val logger: Logger
) : ViewModel() {

    private val _uiState = MutableStateFlow<RatesUiState>(RatesUiState.Loading(fromUser = false))
    val uiState = _uiState.asStateFlow()

    private var isConnected = true

    init {
        observeConnectivity()
        //loadRates()
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
                    is Resource.Loading -> RatesUiState.Loading(fromUser = forceRefresh)

                    is Resource.Success -> RatesUiState.Success(
                        rates = result.data,
                        isStale = result.isStale,
                        isConnected = isConnected
                    )

                    is Resource.Error -> RatesUiState.Error(
                        rates = result.data,
                        message = result.message,
                        isConnected = isConnected,
                        showRefresh = true,
                        showLimitReached = result.errorType == ApiErrorType.UsageLimitReached
                    )
                }
            }
        }
    }
}

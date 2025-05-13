package android.test.forexwatch.presentation.state

import android.test.forexwatch.domain.model.CurrencyRate

sealed class RatesUiState {
    data object Loading : RatesUiState()
    data class Success(
        val rates: List<CurrencyRate>,
        val isStale: Boolean,
        val isConnected: Boolean
    ) : RatesUiState()
    data class Error(
        val message: String,
        val isConnected: Boolean,
        val showRefresh: Boolean
    ) : RatesUiState()
}

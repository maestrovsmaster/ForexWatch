package android.test.forexwatch.presentation.state

import android.test.forexwatch.domain.model.CurrencyRate

sealed class RatesUiState {
    data class Loading(val fromUser: Boolean = false) : RatesUiState()
    data class Success(
        val rates: List<CurrencyRate>,
        val isStale: Boolean,
        val isConnected: Boolean
    ) : RatesUiState()
    data class Error(
        val rates: List<CurrencyRate>?,
        val message: String,
        val isConnected: Boolean,
        val showRefresh: Boolean,
        val showLimitReached: Boolean,
    ) : RatesUiState()
}

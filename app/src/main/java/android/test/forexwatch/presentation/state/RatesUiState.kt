package android.test.forexwatch.presentation.state

import android.test.forexwatch.domain.model.CurrencyRate

sealed class RatesUiState {
    abstract val rates: List<CurrencyRate>?
    abstract val baseCurrencyCode: String
    abstract val baseCurrencyAmount: Double
    abstract val searchQuery: String?
}

data class LoadingUiState(val fromUser: Boolean = false) : RatesUiState() {
    override val rates: List<CurrencyRate>? = null
    override val baseCurrencyCode: String = ""
    override val baseCurrencyAmount: Double = 0.0
    override val searchQuery: String? = null
}

data class SuccessUiState(
    override val rates: List<CurrencyRate>,
    val isStale: Boolean,
    val isConnected: Boolean,
    override val baseCurrencyCode: String,
    override val baseCurrencyAmount: Double,
    override val searchQuery: String? = null
) : RatesUiState()

data class ErrorUiState(
    override val rates: List<CurrencyRate>?,
    val message: String,
    val isConnected: Boolean,
    val showRefresh: Boolean,
    val showLimitReached: Boolean,
    override val baseCurrencyCode: String,
    override val baseCurrencyAmount: Double,
    override val searchQuery: String? = null
) : RatesUiState()

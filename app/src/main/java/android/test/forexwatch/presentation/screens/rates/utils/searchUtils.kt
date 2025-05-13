package android.test.forexwatch.presentation.screens.rates.utils

import android.test.forexwatch.domain.model.CurrencyRate

fun filterAndSort(currencyRates: List<CurrencyRate>, searchQuery: String): List<CurrencyRate> {
    val filteredAndSorted = currencyRates
        .filter { it.currencyCode.contains(searchQuery, ignoreCase = true) }
        .sortedWith(
            compareByDescending<CurrencyRate> {
                it.currencyCode.startsWith(
                    searchQuery,
                    ignoreCase = true
                )
            }
                .thenBy { it.currencyCode }
        )
    return filteredAndSorted
}

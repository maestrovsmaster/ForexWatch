package android.test.forexwatch.core.utils

import java.util.Currency
import java.util.Locale

fun getCurrencyName(code: String, locale: Locale = Locale.ENGLISH): String? {
    return try {
        Currency.getInstance(code).getDisplayName(locale)
    } catch (e: IllegalArgumentException) {
        code
    }
}
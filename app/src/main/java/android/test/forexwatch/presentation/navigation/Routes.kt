package android.test.forexwatch.presentation.navigation

sealed class Screen(val route: String) {

    data object Rates : Screen("rates")

    data object Detail : Screen("detail/{currencyCode}") {
        fun createRoute(currencyCode: String): String = "detail/$currencyCode"
    }
}
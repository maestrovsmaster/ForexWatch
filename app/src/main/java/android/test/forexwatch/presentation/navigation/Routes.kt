package android.test.forexwatch.presentation.navigation

sealed class Screen(val route: String) {
    data object Rates : Screen("rates")
    data object Detail : Screen("detail")
}
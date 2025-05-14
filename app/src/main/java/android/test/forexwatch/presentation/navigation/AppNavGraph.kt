package android.test.forexwatch.presentation.navigation

import android.test.forexwatch.presentation.screens.detail.DetailScreen
import android.test.forexwatch.presentation.screens.rates.RatesScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Rates.route,
        modifier = modifier
    ) {
        composable(Screen.Rates.route) {
            RatesScreen(navController = navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("currencyCode") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val currencyCode = backStackEntry.arguments?.getString("currencyCode") ?: ""
            DetailScreen(currencyCode = currencyCode, onBack = { navController.popBackStack() })
        }
    }
}


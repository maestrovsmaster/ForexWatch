package android.test.forexwatch.presentation.navigation

import android.test.forexwatch.presentation.screens.rates.RatesScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


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
    }
}


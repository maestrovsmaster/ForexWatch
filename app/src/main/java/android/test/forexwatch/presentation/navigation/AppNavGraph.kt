package android.test.forexwatch.presentation.navigation

import androidx.compose.runtime.Composable


import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Rates.route
    ) {
        composable(Screen.Rates.route) {
            //TODO RatesScreen
        }
        composable(Screen.Detail.route) {
            //TODO DetailScreen
        }
    }
}


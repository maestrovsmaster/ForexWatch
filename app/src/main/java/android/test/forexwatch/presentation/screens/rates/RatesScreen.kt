package android.test.forexwatch.presentation.screens.rates

import android.test.forexwatch.core.logging.LogTags
import android.test.forexwatch.presentation.components.LoadingIndicator
import android.test.forexwatch.presentation.components.ErrorMessage
import android.test.forexwatch.presentation.screens.rates.widgets.RateItem
import android.test.forexwatch.presentation.state.RatesUiState
import android.test.forexwatch.presentation.viewmodel.RatesViewModel
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatesScreen(navController: NavController, viewModel: RatesViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState().value
    Log.d(LogTags.FETCH_DATA, "RatesScreen..")
    LaunchedEffect(Unit) {
        Log.d(LogTags.FETCH_DATA, "LaunchedEffect")
        viewModel.loadRates()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Forex Rates") })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is RatesUiState.Loading -> LoadingIndicator()
                is RatesUiState.Error -> ErrorMessage(state.message)
                is RatesUiState.Success -> RatesList(state)
            }
        }
    }
}

@Composable
private fun RatesList(state: RatesUiState.Success) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(state.rates) { rate ->
            RateItem(rate = rate)
        }
    }
}


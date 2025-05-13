package android.test.forexwatch.presentation.screens.rates

import android.test.forexwatch.core.logging.LogTags
import android.test.forexwatch.presentation.components.LoadingIndicator
import android.test.forexwatch.presentation.components.ErrorMessage
import android.test.forexwatch.presentation.screens.rates.widgets.RateItem
import android.test.forexwatch.presentation.state.RatesUiState
import android.test.forexwatch.presentation.viewmodel.RatesViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RatesScreen(navController: NavController, viewModel: RatesViewModel = hiltViewModel()) {

    val state = viewModel.uiState.collectAsState().value

    val isRefreshing = state is RatesUiState.Loading && state.fromUser
    val showLoading = state is RatesUiState.Loading && !state.fromUser

    val rates = when (state) {
        is RatesUiState.Success -> state.rates
        is RatesUiState.Error -> state.rates ?: emptyList()
        else -> emptyList()
    }


    val errorMessage = (state as? RatesUiState.Error)?.message
    val showRetryCenter = rates.isEmpty() && errorMessage != null

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            viewModel.loadRates(forceRefresh = true)
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadRates()
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Forex Rates") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .background(Color.Yellow)
        ) {
            if (showLoading) {
                LoadingIndicator()
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                if (rates.isEmpty().not()) {
                    items(rates) { rate ->
                        RateItem(rate)
                    }
                }

                errorMessage?.let { msg ->
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        ErrorMessage(msg)
                    }
                }
            }

            if (showRetryCenter) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ErrorMessage(errorMessage)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadRates(forceRefresh = true) }) {
                        Text("Refresh")
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
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


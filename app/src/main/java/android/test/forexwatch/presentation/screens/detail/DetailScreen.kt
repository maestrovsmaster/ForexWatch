package android.test.forexwatch.presentation.screens.detail

import android.test.forexwatch.presentation.screens.detail.components.CurrencyChart
import android.test.forexwatch.presentation.screens.detail.components.DateRangeSection
import android.test.forexwatch.presentation.screens.detail.components.DetailTopBar
import android.test.forexwatch.presentation.state.TimeSeriesErrorState
import android.test.forexwatch.presentation.state.TimeSeriesLoadingState
import android.test.forexwatch.presentation.state.TimeSeriesSuccessState
import android.test.forexwatch.presentation.viewmodel.TimeSeriesViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    currencyCode: String,
    onBack: () -> Unit = {},
    viewModel: TimeSeriesViewModel = hiltViewModel()
) {
    LaunchedEffect(currencyCode) {
        viewModel.updateCurrency(currencyCode)
    }

    val state by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            DetailTopBar(
                currencyCode = currencyCode,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DateRangeSection(
                startDate = LocalDate.now().minusDays(7),
                endDate = LocalDate.now(),
                onDateRangeChange = { _, _ -> } // TODO
            )

            Spacer(modifier = Modifier.height(16.dp))




            when (state) {
                is TimeSeriesLoadingState -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is TimeSeriesSuccessState -> {
                    CurrencyChart(rates = (state as TimeSeriesSuccessState).series.rates)
                }

                is TimeSeriesErrorState -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Error: ${(state as TimeSeriesErrorState).message}")
                        Spacer(modifier = Modifier.height(8.dp))
                        (state as? TimeSeriesErrorState)?.fallbackSeries?.let {
                            CurrencyChart(rates = it.rates)
                        }
                    }
                }
            }

        }
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(currencyCode = "USD")
}


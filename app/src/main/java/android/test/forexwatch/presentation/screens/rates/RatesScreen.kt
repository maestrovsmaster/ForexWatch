package android.test.forexwatch.presentation.screens.rates

import android.test.forexwatch.presentation.screens.rates.components.RatesList
import android.test.forexwatch.presentation.screens.rates.widgets.CurrencyHeader
import android.test.forexwatch.presentation.theme.Blue
import android.test.forexwatch.presentation.theme.White
import android.test.forexwatch.presentation.viewmodel.RatesViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RatesScreen(navController: NavController, viewModel: RatesViewModel = hiltViewModel()) {

    val state = viewModel.uiState.collectAsState().value



    LaunchedEffect(Unit) {
        viewModel.loadRates()
    }


    Scaffold(

    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Blue)
                .padding(start = 16.dp, end = 16.dp)
        ) {

            Column() {
                Spacer(modifier = Modifier.height(16.dp))
                CurrencyHeader(
                    currencyCode = state.baseCurrencyCode,
                    baseCurrencyAmount = state.baseCurrencyAmount,
                    onAmountChanged = {
                        viewModel.updateBaseAmount(it)
                    })
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(White)
                        .padding(
                            horizontal = 16.dp, vertical = 16.dp

                        )
                ) {
                    RatesList(
                        state = state, onRefresh = {
                            viewModel.loadRates(forceRefresh = true)
                        },
                        onSearch = {
                            viewModel.searchRates(it)
                        },
                        searchQuery = state.searchQuery ?: "",
                        onClear = {
                            viewModel.searchRates("")
                        }
                    )

                }

            }

        }

    }
}

@Preview
@Composable
fun RatesScreenPreview() {
    RatesScreen(
        navController = NavController(LocalContext.current),
        viewModel = RatesViewModel.preview()
    )
}





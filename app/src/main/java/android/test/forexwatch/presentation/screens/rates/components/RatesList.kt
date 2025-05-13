package android.test.forexwatch.presentation.screens.rates.components

import android.test.forexwatch.presentation.components.ErrorMessage
import android.test.forexwatch.presentation.components.LoadingIndicator
import android.test.forexwatch.presentation.components.SearchBar
import android.test.forexwatch.presentation.screens.rates.utils.filterAndSort
import android.test.forexwatch.presentation.screens.rates.widgets.RateItem
import android.test.forexwatch.presentation.state.ErrorUiState
import android.test.forexwatch.presentation.state.LoadingUiState
import android.test.forexwatch.presentation.state.RatesUiState
import android.test.forexwatch.presentation.state.SuccessUiState
import android.test.forexwatch.presentation.theme.Black80
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RatesList(
    modifier: Modifier = Modifier, state: RatesUiState, onRefresh: () -> Unit,
    searchQuery: String,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val isRefreshing = state is LoadingUiState && state.fromUser
    val showLoading = state is LoadingUiState && !state.fromUser

    val rates = when (state) {
        is SuccessUiState -> state.rates
        is ErrorUiState -> state.rates ?: emptyList()
        else -> emptyList()
    }

    val filteredAndSorted = filterAndSort(rates, searchQuery)


    val errorMessage = (state as? ErrorUiState)?.message
    val showRetryCenter = rates.isEmpty() && errorMessage != null

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            //  viewModel.loadRates(forceRefresh = true)
            onRefresh()
        }
    )

    Box(
        modifier = Modifier

            .fillMaxSize()
            .pullRefresh(pullRefreshState)

    ) {
        if (showLoading) {
            LoadingIndicator()
        }

        Column {

            SearchBar(
                searchQuery = searchQuery,
                onSearch = onSearch,
                onClear = onClear,
            )
            Divider(
                modifier = Modifier.padding(horizontal = 0.dp),
                color = Color.LightGray,
                thickness = 0.5.dp
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
            ) {
                if (filteredAndSorted.isEmpty().not()) {


                    itemsIndexed(filteredAndSorted) { index, item ->
                        Column {

                            RateItem(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                item,
                                multiplier = state.baseCurrencyAmount,
                                onItemClick = onItemClick
                            )

                            if (index < filteredAndSorted.lastIndex) {
                                Divider(
                                    modifier = Modifier.padding(horizontal = 0.dp),
                                    color = Color.LightGray,
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    }
                }


            }

        }

        errorMessage?.let { msg ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Black80)
                    .padding(6.dp),

                contentAlignment = Alignment.Center,
            ) {
                ErrorMessage(msg)
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
                Button(onClick = {
                    //viewModel.loadRates(forceRefresh = true)
                    onRefresh()
                }) {
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
package android.test.forexwatch.presentation.screen


import android.test.forexwatch.presentation.state.*
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import android.test.forexwatch.domain.model.CurrencyTimeseries
import android.test.forexwatch.domain.model.DailyRate
import android.test.forexwatch.presentation.screens.detail.DetailScreen
import android.test.forexwatch.presentation.viewmodel.TimeSeriesViewModel

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeViewModel: FakeTimeSeriesViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        fakeViewModel = FakeTimeSeriesViewModel()
    }

    @Test
    fun loadingState_showsProgressIndicator() {
        fakeViewModel.setTestState(
            TimeSeriesLoadingState(
                targetCurrency = "USD",
                startDate = LocalDate.now().minusDays(7),
                endDate = LocalDate.now()
            )
        )

        composeRule.setContent {
            DetailScreen(currencyCode = "USD", viewModel = fakeViewModel)
        }

        composeRule
            .onNodeWithContentDescription("Progress indicator")
            .assertExists()
    }

    @Test
    fun successState_showsChart() {
        fakeViewModel.setTestState(
            TimeSeriesSuccessState(
                series = CurrencyTimeseries(
                    base = "EUR",
                    target = "USD",
                    rates = listOf(
                        DailyRate(LocalDate.now().minusDays(1), 40.0),
                        DailyRate(LocalDate.now(), 41.0)
                    )
                ),
                targetCurrency = "USD",
                startDate = LocalDate.now().minusDays(1),
                endDate = LocalDate.now()
            )
        )

        composeRule.setContent {
            DetailScreen(currencyCode = "USD", viewModel = fakeViewModel)
        }

        composeRule
            .onNodeWithTag("CurrencyChart")
            .assertExists()
    }

    @Test
    fun errorState_showsErrorText() {
        fakeViewModel.setTestState(
            TimeSeriesErrorState(
                message = "Timeout",
                fallbackSeries = CurrencyTimeseries(
                    base = "EUR",
                    target = "USD",
                    rates = listOf(DailyRate(LocalDate.now(), 39.5))
                ),
                targetCurrency = "USD",
                startDate = LocalDate.now().minusDays(2),
                endDate = LocalDate.now()
            )
        )

        composeRule.setContent {
            DetailScreen(currencyCode = "USD", viewModel = fakeViewModel)
        }

        composeRule
            .onNodeWithText("Error: Timeout")
            .assertExists()
    }
}


class FakeTimeSeriesViewModel : TimeSeriesViewModel(
    getTimeSeriesUseCase = android.test.forexwatch.fake.FakeGetTimeSeriesUseCase(),
    logger = android.test.forexwatch.fake.FakeLogger()
) {
    private val fakeState = MutableStateFlow<TimeSeriesUiState>(
        TimeSeriesLoadingState("USD", LocalDate.now().minusDays(7), LocalDate.now())
    )

    override val uiState: StateFlow<TimeSeriesUiState>
        get() = fakeState

    fun setTestState(state: TimeSeriesUiState) {
        fakeState.value = state
    }

    override fun updateCurrency(newCurrency: String) {}
    override fun updateDateRange(start: LocalDate, end: LocalDate) {}
}
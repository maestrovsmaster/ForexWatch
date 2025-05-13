package android.test.forexwatch.presentation.screens.detail.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.test.forexwatch.domain.model.DailyRate

@Composable
fun CurrencyChart(rates: List<DailyRate>) {
    val entries: List<Entry> = remember(rates) {
        rates.mapIndexed { index, item ->
            Entry(index.toFloat(), item.rate.toFloat())
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = { context ->
            LineChart(context).apply {
                val dataSet = LineDataSet(entries, "Rate").apply {
                    setDrawValues(false)
                    setDrawCircles(false)
                    lineWidth = 2f
                }

                data = LineData(dataSet)
                description.isEnabled = false
                xAxis.isEnabled = false
                axisRight.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                animateX(300)
            }
        }
    )
}
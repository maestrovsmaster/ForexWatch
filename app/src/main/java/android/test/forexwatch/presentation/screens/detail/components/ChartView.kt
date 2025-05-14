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
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.testTag
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun CurrencyChart(entries: List<Entry>, labels: List<String>) {
    Box(modifier = Modifier.testTag("CurrencyChart")) {
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
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(true)
                    animateX(300)

                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(labels)
                        setLabelCount(5, true)
                        granularity = 1f
                        isGranularityEnabled = true
                        position = XAxis.XAxisPosition.BOTTOM
                        textSize = 10f
                        setDrawGridLines(false)
                        isEnabled = true
                    }
                }
            },
            update = { chart ->
                val dataSet = LineDataSet(entries, "Rate").apply {
                    setDrawValues(false)
                    setDrawCircles(false)
                    lineWidth = 2f
                }

                chart.data = LineData(dataSet)
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        )
    }
}

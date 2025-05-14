package android.test.forexwatch.presentation.screens.detail.components

import android.test.forexwatch.presentation.components.DatePickerButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DateRangeSection(
    startDate: LocalDate,
    endDate: LocalDate,
    onDateRangeChange: (LocalDate, LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DatePickerButton(
            label = "From",
            date = startDate,
            onDateSelected = { onDateRangeChange(it, endDate) }
        )

        DatePickerButton(
            label = "To",
            date = endDate,
            onDateSelected = { onDateRangeChange(startDate, it) }
        )
    }
}

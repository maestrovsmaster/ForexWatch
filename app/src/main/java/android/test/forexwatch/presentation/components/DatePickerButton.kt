package android.test.forexwatch.presentation.components

import android.app.DatePickerDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate

@Composable
fun DatePickerButton(
    label: String,
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )
    }

    Button(onClick = { datePicker.show() }) {
        Text("$label: ${date.toString()}")
    }
}

@Preview
@Composable
fun DatePickerButtonPreview() {
    DatePickerButton(
        label = "Select Date",
        date = LocalDate.now(),
        onDateSelected = {}
    )
}


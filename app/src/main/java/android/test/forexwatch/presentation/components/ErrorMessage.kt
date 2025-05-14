package android.test.forexwatch.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.Red)
    }
}

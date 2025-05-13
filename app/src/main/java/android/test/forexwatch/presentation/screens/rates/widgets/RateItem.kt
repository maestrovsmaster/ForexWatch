package android.test.forexwatch.presentation.screens.rates.widgets

import android.test.forexwatch.core.utils.getCurrencyName
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.presentation.components.FlagCircle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RateItem(rate: CurrencyRate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            FlagCircle(rate.currencyCode)
            Text(
                text = "${getCurrencyName(rate.currencyCode)} (${rate.currencyCode})",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 8.dp).weight(1f)
            )
            Text(text = rate.rate.toString(), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview
@Composable
fun RateItemPreview() {
    val rate = CurrencyRate("USD", 1.2345)
    RateItem(rate = rate)

}

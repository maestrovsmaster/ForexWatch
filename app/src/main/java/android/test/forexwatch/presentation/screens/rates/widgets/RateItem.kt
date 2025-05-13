package android.test.forexwatch.presentation.screens.rates.widgets

import android.test.forexwatch.core.utils.getCurrencyName
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.presentation.components.FlagCircle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RateItem(modifier: Modifier = Modifier, rate: CurrencyRate, multiplier: Double = 1.0, onItemClick: (String) -> Unit = {}) {
    val currencyName = getCurrencyName(rate.currencyCode)
    val currencyCode = rate.currencyCode
    val rateValue = rate.rate * multiplier

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onItemClick(currencyCode) },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        FlagCircle(rate.currencyCode)
        Text(
            text = buildAnnotatedString {
                append("$currencyName ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append("($currencyCode)")
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )
        Text(
            text = "%.2f".format(rateValue),
            style = androidx.compose.material.MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
    }

}

@Preview
@Composable
fun RateItemPreview() {
    val rate = CurrencyRate("USD", 1.2345)
    RateItem(rate = rate)

}

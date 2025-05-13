package android.test.forexwatch.presentation.screens.rates.widgets

import android.test.forexwatch.core.utils.getCurrencyName
import android.test.forexwatch.domain.model.CurrencyRate
import android.test.forexwatch.presentation.components.EditableAmountField
import android.test.forexwatch.presentation.components.FlagCircle
import android.test.forexwatch.presentation.theme.Black
import android.test.forexwatch.presentation.theme.BlueLight
import android.test.forexwatch.presentation.theme.Gold
import android.test.forexwatch.presentation.theme.White
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun CurrencyHeader(modifier: Modifier = Modifier, currencyCode: String,
                   baseCurrencyAmount: Double = 1.0,
                   onAmountChanged: (Double) -> Unit,) {

    val currencyName = getCurrencyName(currencyCode)
    val currencyCode = currencyCode

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
        .padding(0.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        FlagCircle(currencyCode, modifier = Modifier.size(56.dp).border(width = 2.dp, color = BlueLight, shape = CircleShape))
        Text(
            text = buildAnnotatedString {
                append("$currencyName ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append("($currencyCode)")
                }
            },
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
            color = White,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )

        EditableAmountField(
            amount = baseCurrencyAmount,
            textColour = White,
            onAmountChanged = onAmountChanged,
            modifier = Modifier
                .padding(0.dp).width(100.dp).height(60.dp)

        )

    }

}

@Preview
@Composable
fun CurrencyHeaderPreview() {
    CurrencyHeader(currencyCode = "USD", onAmountChanged = {})

}

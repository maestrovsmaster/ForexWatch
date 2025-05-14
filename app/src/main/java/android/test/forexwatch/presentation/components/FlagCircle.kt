package android.test.forexwatch.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import android.test.forexwatch.core.utils.CurrencyCountryMapper
import android.test.forexwatch.presentation.theme.BlueDark
import android.test.forexwatch.presentation.theme.White
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun FlagCircle(currencyCode: String, modifier: Modifier = Modifier.size(32.dp), ) {
    val countryCode = CurrencyCountryMapper.currencyToCountry[currencyCode.uppercase()] ?: ""
    val flagUrl = "https://flagcdn.com/w80/${countryCode.lowercase()}.png"

    GlideImage(
        imageModel = { flagUrl },
        modifier = modifier.clip(CircleShape),
        loading = {
        },
        failure = {
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .background(Color.Gray).padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currencyCode.uppercase(),
                    color = Color.White,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
        },
        onImageStateChanged = { _: GlideImageState -> }
    )
}
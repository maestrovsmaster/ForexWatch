package android.test.forexwatch.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = White,

    secondary = BlueLight,
    onSecondary = Black,

    tertiary = BlueDark,
    onTertiary = White,

    background = White,
    onBackground = Black,

    surface = White,
    onSurface = Black,
)


@Composable
fun ForexWatchTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
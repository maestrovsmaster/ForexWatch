package android.test.forexwatch

import android.os.Bundle
import android.test.forexwatch.presentation.navigation.AppNavGraph
import android.test.forexwatch.presentation.theme.Blue
import android.test.forexwatch.presentation.theme.BlueDark
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.test.forexwatch.presentation.theme.ForexWatchTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import android.view.WindowManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )


        setContent {
            ForexWatchTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun AppContent(modifier: Modifier = Modifier) {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = false
        val backgroundColor = BlueDark

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = backgroundColor,
                darkIcons = useDarkIcons
            )
        }

        AppNavGraph(modifier = modifier)
    }
}
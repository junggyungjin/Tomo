package ja.ko.tomo.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.theme.TomoBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    // 화면이 처음으로 나탈때 딱 한번 실행 됨
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TomoBlue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "TOMO",
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
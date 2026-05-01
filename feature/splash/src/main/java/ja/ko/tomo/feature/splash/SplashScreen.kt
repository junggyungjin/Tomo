package ja.ko.tomo.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.core.ui.theme.TomoTheme
import kotlinx.coroutines.delay
import ja.ko.tomo.core.ui.R as CoreR

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    // 화면이 처음으로 나탈때 딱 한번 실행 됨
    LaunchedEffect(Unit) {
        delay(5000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TomoBlue),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = CoreR.drawable.tomo_splash),
            contentDescription = stringResource(R.string.splash_img_description),
            modifier = Modifier.size(120.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
private fun SplashScreenPreview() {
    TomoTheme {
        SplashScreen(
            onTimeout = {}
        )
    }
}
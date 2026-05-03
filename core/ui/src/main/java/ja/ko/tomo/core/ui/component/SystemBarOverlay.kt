package ja.ko.tomo.core.ui.component

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * 시스템 바 아이콘 색상만 제어 (투명 배경 유지 시 사용)
 * @param isDarkBackground 배경이 어두우면 아이콘을 흰색으로, 밝으면 검은색으로 설정
 */
@Composable
fun SystemBarAppearance(isDarkBackground: Boolean) {
    val view = LocalView.current
    val systemInDarkTheme = isSystemInDarkTheme()

    DisposableEffect(isDarkBackground) {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, view)
            // 진입 시: 배경이 어두우면(Dark) 아이콘은 밝게(isAppearanceLight = false)
            insetsController.isAppearanceLightStatusBars = !isDarkBackground
            insetsController.isAppearanceLightNavigationBars = !isDarkBackground
        }
        onDispose {
            // 나갈 때: 다음 화면을 위해 다시 시스템 테마 상태로 복구
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                // 시스템이 다크모드면 흰색 아이콘(false), 라이트모드면 검은색 아이콘(true)
                insetsController.isAppearanceLightStatusBars = !systemInDarkTheme
                insetsController.isAppearanceLightNavigationBars = !systemInDarkTheme
            }
        }
    }
}

/**
 * 안드로이드 시스템 상단, 하단 바 영역에 색상을 입히고 아이콘 색상을 자동 조정
 */
@Composable
fun SystemBarVisuals(
    statusBarColor: Color = Color.Black,
    navigationBarColor: Color = Color.Black,
    isDarkBackground: Boolean = true  // 이 값이 true면 아이콘은 '흰색'이 되고, false면 '검은색'이 됨.
) {
    // 1. 아이콘 색상 자동 제어 호출 (진입 및 퇴장 로직 포함)
    SystemBarAppearance(isDarkBackground = isDarkBackground)

    // 2. 배경 색상 영역 그리기
    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 바
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(statusBarColor)
        )

        // 중간 영역 비우기
        Spacer(modifier = Modifier.weight(1f))

        // 하단 바
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .background(navigationBarColor)
        )
    }
}

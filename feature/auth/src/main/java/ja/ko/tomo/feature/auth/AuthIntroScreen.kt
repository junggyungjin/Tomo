package ja.ko.tomo.feature.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.component.SystemBarVisuals
import ja.ko.tomo.core.ui.component.VideoBackground
import ja.ko.tomo.core.ui.theme.TomoBlue

@Composable
fun AuthIntroScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToInquiry: () -> Unit
) {
    // stateless content만 호출 (여기에 ui 로직은 없음)
    AuthIntroContent(
        onSignUpClick = onNavigateToSignUp,
        onLoginClick = onNavigateToLogin,
        onInquiryClick = onNavigateToInquiry
    )
}

@Composable
private fun AuthIntroContent(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onInquiryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 1. 최하단 배경 비디오
        VideoBackground(
            videoResId = R.raw.auth_background,
            modifier = Modifier.fillMaxSize(),
            darkOverlayAlpha = 0.15f
        )

        // 2. 상/하단 블랙 바 및 아이콘 색상 제어
        SystemBarVisuals()

        // 상단 로고
        Text(
            text = "Tomo",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding() // 상태바 영역 만큼 자동 패딩
                .padding(top = 60.dp)
        )

        // 하단 버튼
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding() // 하단 시스템 영역만큼 자동 패딩
                .padding(bottom = 40.dp)
                .padding(horizontal = 28.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 회원가입 버튼
            Button(
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(text = stringResource(R.string.auth_intro_signup), color = TomoBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // 로그인버튼
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(text = stringResource(R.string.auth_intro_login), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 문의하기 텍스트버튼
            TextButton(onClick = onInquiryClick) {
                Text(
                    text = stringResource(R.string.auth_intro_inquiry),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }
    }
}
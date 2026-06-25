package ja.ko.tomo.feature.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.component.TomoSnackbar
import ja.ko.tomo.core.ui.component.TomoStateView
import ja.ko.tomo.core.ui.theme.DarkGray
import ja.ko.tomo.core.ui.theme.ErrorRed
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.core.ui.theme.TomoTypography
import ja.ko.tomo.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun MyPageScreen(
    state: MyPageUiState,
    effect: Flow<MyPageUiEffect>,
    onLogoutClick: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is MyPageUiEffect.NavigateToAuth -> onNavigateToAuth()
                is MyPageUiEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = uiEffect.message.asString(context)
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                TomoSnackbar(snackbarData = data)
            }
        }
    ) { padding ->
        TomoStateView(
            isLoading = state is MyPageUiState.Loading,
            errorMessage = (state as? MyPageUiState.Error)?.message,
            isSuccess = state is MyPageUiState.Success,
            onRetry = onRetry,
            modifier = Modifier.padding(padding)
        ) {
            MyPageContent(
                state = state as MyPageUiState.Success,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

@Composable
private fun MyPageContent(
    state: MyPageUiState.Success,
    onLogoutClick: () -> Unit
) {
    val user = state.user
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // 프사
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = stringResource(R.string.mypage_profile_desc),
            modifier = Modifier.size(120.dp),
            tint = TomoBlue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 닉네임
        user.nickname?.let {
            Text(
                text = it,
                style = TomoTypography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // 이메일
        Text(
            text = user.email ?: "",
            style = TomoTypography.bodyMedium,
            color = DarkGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 자기소개 라벨
        Text(
            text = stringResource(R.string.mypage_introduction_label),
            style = TomoTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        // 자기소개 내용
        Text(
            text = user.introduction ?: "",
            style = TomoTypography.bodyMedium,
            modifier = Modifier.align(Alignment.Start),
        )

        Spacer(modifier = Modifier.weight(1f))

        // 로그아웃 버튼
        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            enabled = !state.isLoggingOut,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = ErrorRed
            )
        ) {
            if (state.isLoggingOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = ErrorRed,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.mypage_logout),
                    style = TomoTypography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "SuccessPreview")
@Composable
private fun MyPageScreenPreview() {
    TomoTheme {
        MyPageScreen(
            state = MyPageUiState.Success(
                user = User(
                    id = "1",
                    email = "tomo@example.com",
                    nickname = "토모개발자",
                    handle = "tomo_dev",
                    introduction = "안녕하세요, 토모입니다!",
                    status = "ACTIVE",
                    nationality = null,
                    profileImageUrl = null
                )
            ),
            effect = emptyFlow(),
            onLogoutClick = {},
            onNavigateToAuth = {},
            onRetry = {}
        )
    }
}
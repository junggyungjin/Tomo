package ja.ko.tomo.feature.auth.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import ja.ko.tomo.core.ui.component.TomoSnackbar
import ja.ko.tomo.core.ui.component.TomoStateView
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.feature.auth.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialSignUpScreen(
    state: SocialSignUpUiState,
    effect: Flow<SocialSignUpUiEffect>,
    onGoogleSignUpClick: (token: String, providerId: String, name: String?, email: String?) -> Unit,
    onBackButtonClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToNext: () -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val credentialManager = CredentialManager.create(context)

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is SocialSignUpUiEffect.NavigateToNext -> onNavigateToNext()
                is SocialSignUpUiEffect.NavigateBack -> onNavigateBack()
                is SocialSignUpUiEffect.ShowSnackbar -> {
                    // 직접 내부 SnackbarHost에 표시
                    val job = launch {
                        val message = uiEffect.message.asString(context)
                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(500)
                    job.cancel()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                TomoSnackbar(snackbarData = data)
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.social_signup_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackButtonClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        TomoStateView(
            isLoading = state is SocialSignUpUiState.Loading,
            errorMessage = (state as? SocialSignUpUiState.Error)?.message,
            isSuccess = state is SocialSignUpUiState.Success,
            onRetry = onRetry,
            modifier = Modifier.padding(padding)
        ) {
            SocialSignUpContent(
                state = state as SocialSignUpUiState.Success,
                onGoogleButtonClick = {
                    scope.launch {
                        try {
                            val googleIdOption = GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId(context.getString(R.string.google_web_client_id))
                                .build()

                            val request = GetCredentialRequest.Builder()
                                .addCredentialOption(googleIdOption)
                                .build()

                            val result = credentialManager.getCredential(context, request)
                            val credential = result.credential

                            if (credential is GoogleIdTokenCredential) {
                                onGoogleSignUpClick(
                                    credential.idToken,
                                    credential.id,
                                    credential.displayName,
                                    null
                                )
                            }
                        }catch (e: Exception) {
                            // 에러 시 스낵바 처리는 유지 (부분 에러이므로)
                            snackbarHostState.showSnackbar(
                                context.getString(R.string.auth_error_google_login, e.localizedMessage ?: "Unknown")
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun SocialSignUpContent(
    modifier: Modifier = Modifier,
    state: SocialSignUpUiState,
    onGoogleButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.social_signup_logo_text),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = TomoBlue
        )
        Text(
            text = stringResource(R.string.social_signup_subtitle),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(60.dp))

        SocialSignUpButton(
            text = stringResource(R.string.social_signup_google),
            iconResId = ja.ko.tomo.core.ui.R.drawable.ic_google,
            onClick = onGoogleButtonClick,
            isLoading = (state as? SocialSignUpUiState.Success)?.isSigningUp == true
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.social_signup_terms),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }
}

@Composable
fun SocialSignUpButton(
    text: String,
    iconResId: Int,
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(0.5.dp, Color.LightGray),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
        enabled = !isLoading,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // 1. 로고
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 20.dp)
                        .size(24.dp)
                )

                // 2. 텍스트
                Text(
                    text = text,
                    color = Color.Black.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "초기 화면 (Success)")
@Composable
private fun SocialSignUpScreenPreview() {
    TomoTheme {
        SocialSignUpScreen(
            state = SocialSignUpUiState.Success(),
            effect = emptyFlow(),
            onGoogleSignUpClick = {_,_,_,_, -> },
            onBackButtonClick = {},
            onNavigateBack = {},
            onNavigateToNext = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "회원가입 중 (Loading)")
@Composable
private fun SocialSignUpScreenLoadingPreview() {
    TomoTheme {
        SocialSignUpScreen(
            state = SocialSignUpUiState.Success(isSigningUp = true),
            effect = emptyFlow(),
            onGoogleSignUpClick = {_,_,_,_, -> },
            onBackButtonClick = {},
            onNavigateBack = {},
            onNavigateToNext = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "버튼 단독 프리뷰")
@Composable
private fun SocialSignUpButtonPreview() {
    TomoTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SocialSignUpButton(
                text = "Google로 계속",
                iconResId = ja.ko.tomo.core.ui.R.drawable.ic_google, // UPDATED: 실제 리소스 연결
                onClick = {}
            )
        }
    }
}
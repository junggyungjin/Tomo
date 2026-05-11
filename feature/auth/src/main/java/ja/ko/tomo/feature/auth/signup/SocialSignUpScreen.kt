package ja.ko.tomo.feature.auth.signup

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun SocialSignUpScreen(
    state: SocialSignUpUiState,
    effect: Flow<SocialSignUpUiEffect>,
    onGoogleSignUpClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToNext: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val credentialManager = CredentialManager.create(context)

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is SocialSignUpUiEffect.NavigateToNext -> onNavigateToNext()
                is SocialSignUpUiEffect.NavigateBack -> onBackClick()
                is SocialSignUpUiEffect.ShowSnackbar -> {
                    // UPDATED: 직접 내부 SnackbarHost에 표시
                    val job = launch {
                        val message = context.getString(uiEffect.resId)
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
}
package ja.ko.tomo.feature.auth.navigation

import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.core.ui.util.sendInquiryEmail
import ja.ko.tomo.feature.auth.AuthIntroScreen
import ja.ko.tomo.feature.auth.AuthIntroViewModel
import ja.ko.tomo.feature.auth.signup.SocialSignUpScreen
import ja.ko.tomo.feature.auth.signup.SocialSignUpViewModel

fun NavGraphBuilder.authGraph(
    navController: NavController
) {
    composable(TomoNavRoutes.AuthIntro) {
        val authViewModel: AuthIntroViewModel = hiltViewModel()
        val authIntroUiState by authViewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        // 인증 인트로 화면(진입점)
        AuthIntroScreen(
            state = authIntroUiState,
            effect = authViewModel.effect,
            onNavigateToSignUp =  {
                navController.navigate(TomoNavRoutes.SignUp)
            },
            onNavigateToLogin = {
                // TODO 로그인 api 만들고나서 실제 구현
                navController.navigate(TomoNavRoutes.MeetingList) {
                    popUpTo(TomoNavRoutes.AuthIntro) { inclusive = true}
                }
            },
            onInquiryClick = {
                authViewModel.onInquiryClick()
            },
            onNavigateToInquiry = {
                context.sendInquiryEmail(to = "jerryaa77@gmail.com")
            },
            onUserReturned = {
                authViewModel.onUserReturned()
            }
        )
    }

    // 소셜 회원가입 선택 화면
    composable(TomoNavRoutes.SignUp) {
        val socialSignUpViewModel: SocialSignUpViewModel = hiltViewModel()
        val uiState by socialSignUpViewModel.uiState.collectAsStateWithLifecycle()

        SocialSignUpScreen(
            state = uiState,
            effect = socialSignUpViewModel.effect,
            onGoogleSignUpClick = { idToken ->
                socialSignUpViewModel.signUpWithGoogle(idToken)
            },
            onBackButtonClick = {
                socialSignUpViewModel.onBackClick()
            },
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToNext = {
                navController.navigate(TomoNavRoutes.MeetingList) {
                    popUpTo(TomoNavRoutes.AuthIntro) { inclusive = true}
                }
            }
        )
    }
}
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

fun NavGraphBuilder.authGraph(
    navController: NavController
) {
    composable(TomoNavRoutes.AuthIntro) {
        val authViewModel: AuthIntroViewModel = hiltViewModel()
        val authIntroUiState by authViewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        AuthIntroScreen(
            state = authIntroUiState,
            effect = authViewModel.effect,
            onNavigateToSignUp =  {
                navController.navigate(TomoNavRoutes.MeetingList) {
                    popUpTo(TomoNavRoutes.AuthIntro) { inclusive = true}
                }
            },
            onNavigateToLogin = {
                // TODO 회원가입 api 만들고나서 실제 구현
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
}
package ja.ko.tomo.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.feature.auth.AuthIntroScreen

fun NavGraphBuilder.authGraph(
    navController: NavController
) {
    composable(TomoNavRoutes.AuthIntro) {
        AuthIntroScreen(
            onNavigateToSignUp =  {
                navController.navigate(TomoNavRoutes.MeetingList) {
                    popUpTo(TomoNavRoutes.AuthIntro) { inclusive = true}
                }
            },
            onNavigateToLogin = {
                // TODO 회원가입 api 만들고나서 실제 구현
            },
            onNavigateToInquiry = {
                // TODO 문의하기 api 만들고나서 실제 구현
            }
        )
    }
}
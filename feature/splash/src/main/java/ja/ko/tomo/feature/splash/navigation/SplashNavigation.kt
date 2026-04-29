package ja.ko.tomo.feature.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.feature.splash.SplashScreen

fun NavGraphBuilder.splashGraph(
    navController: NavController
) {
    composable(TomoNavRoutes.Splash) {
        SplashScreen(onTimeout = {
            navController.navigate(TomoNavRoutes.AuthIntro) {
                // 스택에서 스플래쉬화면 삭제
                popUpTo(TomoNavRoutes.Splash) { inclusive = true}
            }
        })
    }
}
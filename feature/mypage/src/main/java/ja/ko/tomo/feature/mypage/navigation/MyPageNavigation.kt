package ja.ko.tomo.feature.mypage.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.feature.mypage.MyPageScreen
import ja.ko.tomo.feature.mypage.MyPageViewModel

fun NavGraphBuilder.myPageGraph(
    navController: NavController
) {
    composable(
        TomoNavRoutes.MyPage
    ) {
        val myPageViewModel: MyPageViewModel = hiltViewModel()
        val myPageUiState by myPageViewModel.uiState.collectAsStateWithLifecycle()

        MyPageScreen(state = myPageUiState)
    }
}
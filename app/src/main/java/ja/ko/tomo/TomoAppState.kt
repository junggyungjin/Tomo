package ja.ko.tomo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.core.navigation.bottomNavItem

@Composable
fun rememberTomoAppState(
    navController: NavHostController = rememberNavController()
) : TomoAppState {
    return remember(navController) {
        TomoAppState(navController = navController)
    }
}

@Stable
class TomoAppState(
    val navController: NavHostController
) {
    // 현재 어떤 화면에 있는지 관찰 가능한 상태로 가져옴
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    // 하단 바를 보여줘야 할 화면인지 판단하는 로직을 일로 옮깁니다.
    val shouldShowBottomBar: Boolean
        @Composable get() = currentDestination?.route in bottomNavItem.map { it.route }

    // 하단 바 클릭 시 이동 로직도 캡슐화합니다
    fun navigateToTopLevelDestination(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
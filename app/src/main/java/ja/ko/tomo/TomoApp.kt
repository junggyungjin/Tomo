package ja.ko.tomo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.core.navigation.bottomNavItem
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.feature.auth.navigation.authGraph
import ja.ko.tomo.feature.chat.navigation.chatGraph
import ja.ko.tomo.feature.meeting.navigation.meetingGraph
import ja.ko.tomo.feature.mypage.navigation.myPageGraph
import ja.ko.tomo.feature.splash.navigation.splashGraph
import kotlin.collections.contains

@Composable
fun TomoApp() {
    TomoTheme {
        val navController = rememberNavController()
        // 현재 네비게이션 상태 추적
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val mainTabRoutes = listOf(
            TomoNavRoutes.MeetingList,
            TomoNavRoutes.ChatList,
            TomoNavRoutes.MyPage
        )

        // 메인 화면 일때만 하단 바 노출 결정
        val showBottomBar = currentRoute in mainTabRoutes

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    TomoBottomBar(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                TomoNavHost(navController = navController)
            }
        }
    }
}

@Composable
private fun TomoBottomBar(navController: NavController, currentRoute: String?) {
    NavigationBar(
        modifier = Modifier.height(100.dp),
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        bottomNavItem.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = stringResource(item.nameRes)) },
                label = { Text(stringResource(item.nameRes)) }
            )
        }
    }
}

@Composable
fun TomoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = TomoNavRoutes.Splash
    ) {
        splashGraph(navController)
        authGraph(navController)
        meetingGraph(navController)
        myPageGraph(navController)
        chatGraph(navController)
    }
}
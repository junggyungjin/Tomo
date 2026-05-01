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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.core.navigation.bottomNavItem
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.feature.auth.navigation.authGraph
import ja.ko.tomo.feature.chat.navigation.chatGraph
import ja.ko.tomo.feature.meeting.navigation.meetingGraph
import ja.ko.tomo.feature.mypage.navigation.myPageGraph
import ja.ko.tomo.feature.splash.navigation.splashGraph

@Composable
fun TomoApp(
    appState: TomoAppState = rememberTomoAppState() // StateHolder 주입
) {
    TomoTheme {
        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    TomoBottomBar(
                        currentRoute = appState.currentDestination?.route,
                        onNavigate = { route -> appState.navigateToTopLevelDestination(route)}
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                TomoNavHost(navController = appState.navController)
            }
        }
    }
}

@Composable
private fun TomoBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit // NavController 대신 콜백을 받음(결합도 낮춤)
) {
    NavigationBar(
        modifier = Modifier.height(100.dp),
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        bottomNavItem.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) }, // 위임 받은 함수 실행
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
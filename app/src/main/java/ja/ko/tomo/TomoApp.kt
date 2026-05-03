package ja.ko.tomo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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


@Composable
fun TomoApp(
    appState: TomoAppState = rememberTomoAppState()
) {
    TomoTheme {
        // 현업 팁: 실제로는 여기서 viewModel을 통해 '로그인 여부'를 판단하여
        // startDestination을 결정합니다. 지금은 임시로 AuthIntro를 시작점으로 잡습니다.
        val startDestination = TomoNavRoutes.AuthIntro

        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    TomoBottomBar(
                        currentRoute = appState.currentDestination?.route,
                        onNavigate = { route -> appState.navigateToTopLevelDestination(route) }
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                TomoNavHost(
                    navController = appState.navController,
                    startDestination = startDestination,
                    innerPadding = padding
                )
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
private fun TomoNavHost(
    navController: NavHostController,
    startDestination: String, // 파라미터 추가
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        // 이제 시스템 스플래시를 쓰므로 기존 splashGraph(navController)는 여기서 제거해도 됩니다.
        authGraph(navController)
        meetingGraph(navController)
        myPageGraph(navController)
        chatGraph(navController)
    }
}
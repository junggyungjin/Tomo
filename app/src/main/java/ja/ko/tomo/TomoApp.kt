package ja.ko.tomo

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.core.navigation.bottomNavItem
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.core.ui.util.DoubleBackToExitHandler
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.feature.auth.navigation.authGraph
import ja.ko.tomo.feature.chat.navigation.chatGraph
import ja.ko.tomo.feature.meeting.navigation.meetingGraph
import ja.ko.tomo.feature.mypage.navigation.myPageGraph


@Composable
fun TomoApp(
    appState: TomoAppState = rememberTomoAppState(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    // ViewModel에서 결정된 시작점을 구독
    val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()
    // 뒤로 가기 스택이 없을때만 뒤로가기 두번 눌렀을때 앱 종료
    DoubleBackToExitHandler(
        enabled = appState.navController.previousBackStackEntry == null
    )

    TomoTheme {
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
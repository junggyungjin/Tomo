package ja.ko.tomo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.core.navigation.bottomNavItem
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.core.ui.util.DoubleBackToExitHandler
import ja.ko.tomo.data.local.AuthEvent
import ja.ko.tomo.feature.auth.navigation.authGraph
import ja.ko.tomo.feature.chat.navigation.chatGraph
import ja.ko.tomo.feature.feed.navigation.feedGraph
import ja.ko.tomo.feature.meeting.navigation.meetingGraph
import ja.ko.tomo.feature.mypage.navigation.myPageGraph

@Composable
fun TomoApp(
    appState: TomoAppState = rememberTomoAppState(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    // ViewModel에서 결정된 시작점을 구독
    val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()

    val isReady by mainViewModel.isReady.collectAsStateWithLifecycle()

    // 앱 전체 화면을 캡처하고 블러 처리할 Haze 상태 관리 객체
    val hazeState = remember { HazeState() }

    // 전역 세션 만료 이벤트 처리
    LaunchedEffect(Unit) {
        mainViewModel.authEvent.collect { event ->
            when (event) {
                is AuthEvent.SessionExpired -> {
                    mainViewModel.logout()
                    Toast.makeText(context, "세션이 만료되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
                    // 모든 스택을 제거하고 로그인 화면으로 이동
                    appState.navController.navigate(TomoNavRoutes.AuthIntro) {
                        popUpTo(0) { inclusive = true}
                    }
                }
            }
        }
    }

    // 뒤로 가기 스택이 없을때만 뒤로가기 두번 눌렀을때 앱 종료
    DoubleBackToExitHandler(
        enabled = appState.navController.previousBackStackEntry == null
    )

    TomoTheme {
        if (isReady) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background
            ) { padding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    TomoNavHost(
                        navController = appState.navController,
                        startDestination = startDestination,
                        innerPadding = padding,
                        mainViewModel = mainViewModel,
                        modifier = Modifier.haze(state = hazeState)
                    )

                    if (appState.shouldShowBottomBar) {
                        TomoBottomBar(
                            currentRoute = appState.currentDestination?.route,
                            onNavigate = { route -> appState.navigateToTopLevelDestination(route) },
                            hazeState = hazeState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp)
                                .navigationBarsPadding()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TomoBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit, // NavController 대신 콜백을 받음(결합도 낮춤)
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(38.dp))
            .hazeChild(
                state = hazeState,
                shape = RoundedCornerShape(38.dp),
                style = HazeStyle(
                    // 💡 [핵심] 탁해진 원인(0.7f) 제거! 투명도를 0.25f로 확 낮춰서 뒤의 무지개색이 '맑게' 올라오게 합니다.
                    tint = Color.Black.copy(alpha = 0.45f),
                    blurRadius = 20.dp, // 블러는 높게 유지하여 형태만 예쁘게 뭉개줍니다.
                    noiseFactor = 0.03f
                )
            )
            .border(
                width = 0.5.dp,
                // 테두리 반사광: 차가운 하얀색이 아니라 '따뜻한 아이보리빛'으로 상단에만 빛을 줍니다.
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f), // 상단 빛 반사
                        Color.White.copy(alpha = 0.05f) // 하단 그림자
                    )
                ),
                shape = RoundedCornerShape(38.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItem.forEach { item ->
                val isSelected = currentRoute == item.route

                Surface(
                    onClick = { onNavigate(item.route) },
                    color = if (isSelected) Color.White else Color.Transparent,
                    shape = CircleShape,
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.nameRes),
                            tint = if (isSelected) Color.Black else Color.White.copy(alpha = 0.45f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TomoNavHost(
    navController: NavHostController,
    startDestination: String, // 파라미터 추가
    innerPadding: PaddingValues,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
            .fillMaxSize()
    ) {
        // 이제 시스템 스플래시를 쓰므로 기존 splashGraph(navController)는 여기서 제거해도 됩니다.
        authGraph(navController)
        feedGraph(navController)
        meetingGraph(navController)
        myPageGraph(
            navController = navController,
            onLogoutSuccess = {
                mainViewModel.logout()
                navController.navigate(TomoNavRoutes.AuthIntro) {
                    popUpTo(0) { inclusive = true}
                }
            }
        )
        chatGraph(navController)
    }
}
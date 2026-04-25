package ja.ko.tomo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.core.navigation.bottomNavItem
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.feature.chat.ChatListScreen
import ja.ko.tomo.feature.chat.ChatListViewModel
import ja.ko.tomo.feature.chatroom.ChatRoomScreen
import ja.ko.tomo.feature.chatroom.ChatRoomViewModel
import ja.ko.tomo.feature.meeting.meetingcreate.MeetingCreateScreen
import ja.ko.tomo.feature.meeting.meetingcreate.MeetingCreateViewModel
import ja.ko.tomo.feature.meeting.meetingdetail.MeetingDetailScreen
import ja.ko.tomo.feature.meeting.meetingdetail.MeetingDetailViewModel
import ja.ko.tomo.feature.meeting.meetinglist.MeetingListScreen
import ja.ko.tomo.feature.meeting.meetinglist.MeetingListViewModel
import ja.ko.tomo.feature.mypage.MyPageScreen
import ja.ko.tomo.feature.mypage.MyPageViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val listViewModel: MeetingListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TomoTheme {
                val navController = rememberNavController()
                // 현재 네비게이션 상태 추적
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 메인 화면 일때만 하단 바 노출 결정
                val showBottomBar = currentRoute in listOf(
                    TomoNavRoutes.MeetingList,
                    TomoNavRoutes.ChatList,
                    TomoNavRoutes.MyPage
                )

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
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
                                        icon = { Icon(item.icon, contentDescription = item.name) }
                                    )
                                }
                            }
                        }
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        NavHost(
                            navController = navController,
                            startDestination = TomoNavRoutes.MeetingList
                        ) {
                            composable(TomoNavRoutes.MeetingList) {
                                val listUiState by listViewModel.uiState.collectAsStateWithLifecycle()

                                LaunchedEffect(Unit) {
                                    listViewModel.reloadMeetings()
                                }

                                MeetingListScreen(
                                    state = listUiState,
                                    onMeetingClick = { meetingId ->
                                        navController.navigate(
                                            TomoNavRoutes.meetingDetailRoute(meetingId)
                                        )
                                    },
                                    onFilterChange = { filter ->
                                        listViewModel.updateFilter(filter)
                                    },
                                    onSearchQueryChange = { query ->
                                        listViewModel.onSearchQueryChange(query)
                                    },
                                    onToggleFavorite = { meetingId ->
                                        listViewModel.toggleFavorite(meetingId)
                                    },
                                    onNavigateToCreate = {
                                        navController.navigate(TomoNavRoutes.MeetingCreate)
                                    }
                                )
                            }

                            composable(
                                route = "${TomoNavRoutes.MeetingDetail}/{meetingId}",
                                arguments = listOf(
                                    navArgument("meetingId") {
                                        type = NavType.LongType
                                    }
                                )
                            ) {
                                val detailViewModel: MeetingDetailViewModel = hiltViewModel()
                                val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()

                                MeetingDetailScreen(
                                    state = detailUiState,
                                    effect = detailViewModel.effect,
                                    onBackClick = { navController.popBackStack() },
                                    onActionButtonClick = detailViewModel::onActionButtonClick,
                                    onToggleFavorite = { meetingId ->
                                        detailViewModel.toggleFavorite(meetingId = meetingId)
                                    }
                                )
                            }

                            composable(
                                TomoNavRoutes.MeetingCreate
                            ) {
                                val createViewModel: MeetingCreateViewModel = hiltViewModel()
                                val createUiState by createViewModel.uiState.collectAsStateWithLifecycle()

                                MeetingCreateScreen(
                                    state = createUiState,
                                    effect = createViewModel.effect,
                                    onTitleChange = createViewModel::onTitleChanged,
                                    onSubtitleChange = createViewModel::onSubtitleChanged,
                                    onDateTimeChange = createViewModel::onDateTimeChanged,
                                    onLocationChange = createViewModel::onLocationChanged,
                                    onCapacityChange = createViewModel::onCapacityChanged,
                                    onSaveClick = createViewModel::saveMeeting,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }

                            composable(
                                TomoNavRoutes.MyPage
                            ) {
                                val myPageViewModel: MyPageViewModel = hiltViewModel()
                                val myPageUiState by myPageViewModel.uiState.collectAsStateWithLifecycle()

                                MyPageScreen(state = myPageUiState)
                            }

                            composable(TomoNavRoutes.ChatList) {
                                val chatViewModel: ChatListViewModel = hiltViewModel()
                                val chatListUiState by chatViewModel.uiState.collectAsStateWithLifecycle()

                                ChatListScreen(
                                    state = chatListUiState,
                                    onChatRoomClick = { chatId ->
                                        navController.navigate(
                                            TomoNavRoutes.chatRoomRoute(chatId)
                                        )
                                    }
                                )
                            }

                            composable(
                                route = "${TomoNavRoutes.ChatRoom}/{chatId}",
                                arguments = listOf(navArgument("chatId") { type = NavType.LongType })
                            ) {
                                val chatRoomViewModel: ChatRoomViewModel = hiltViewModel()
                                val uiState by chatRoomViewModel.uiState.collectAsStateWithLifecycle()

                                ChatRoomScreen(
                                    state = uiState,
                                    onMessageChange = chatRoomViewModel::onMessageChange,
                                    onSendClick = chatRoomViewModel::sendMessage,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}
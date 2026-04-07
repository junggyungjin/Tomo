package ja.ko.tomo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ja.ko.tomo.data.repository.FakeMeetingRepositoryImpl
import ja.ko.tomo.domain.usecase.meeting.CancelJoinUseCase
import ja.ko.tomo.domain.usecase.meeting.GetMeetingDetailUseCase
import ja.ko.tomo.domain.usecase.meeting.GetMeetingsUseCase
import ja.ko.tomo.domain.usecase.meeting.JoinMeetingUseCase
import ja.ko.tomo.presentation.meetingdetail.MeetingDetailScreen
import ja.ko.tomo.presentation.meetingdetail.MeetingDetailViewModel
import ja.ko.tomo.presentation.meetingdetail.MeetingDetailViewModelFactory
import ja.ko.tomo.presentation.meetinglist.MeetingListScreen
import ja.ko.tomo.presentation.meetinglist.MeetingListViewModel
import ja.ko.tomo.presentation.meetinglist.MeetingListViewModelFactory
import ja.ko.tomo.presentation.navigation.TomoNavRoutes
import ja.ko.tomo.ui.theme.TomoTheme

class MainActivity : ComponentActivity() {
    private val repository by lazy {
        FakeMeetingRepositoryImpl()
    }

    private val getMeetingsUseCase by lazy {
        GetMeetingsUseCase(repository)
    }

    private val getMeetingDetailUseCase by lazy {
        GetMeetingDetailUseCase(repository)
    }

    private val joinMeetingUseCase by lazy {
        JoinMeetingUseCase(repository)
    }

    private val cancelJoinUseCase by lazy {
        CancelJoinUseCase(repository)
    }

    private val listViewModel: MeetingListViewModel by viewModels {
        MeetingListViewModelFactory(
            getMeetingsUseCase = getMeetingsUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TomoTheme {
                val navController = rememberNavController()
                val listUiState by listViewModel.uiState.collectAsStateWithLifecycle()

                NavHost(
                    navController = navController,
                    startDestination = TomoNavRoutes.MeetingList
                ) {
                    composable(TomoNavRoutes.MeetingList) {
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
                    ) { backStackEntry ->
                        val meetingId =
                            backStackEntry.arguments?.getLong("meetingId") ?: return@composable

                        val detailViewModel: MeetingDetailViewModel = viewModel(
                            factory = MeetingDetailViewModelFactory(
                                meetingId = meetingId,
                                getMeetingDetailUseCase = getMeetingDetailUseCase,
                                joinMeetingUseCase = joinMeetingUseCase,
                                cancelJoinUseCase = cancelJoinUseCase
                            )
                        )

                        val detailUiState by detailViewModel.uiState.collectAsStateWithLifecycle()

                        MeetingDetailScreen(
                            state = detailUiState,
                            onActionButtonClick = detailViewModel::onActionButtonClick // TODO 질문 왜 이 코드만 뷰모델의 함수호출방법이 달라?
                        )
                    }
                }
            }
        }
    }
}
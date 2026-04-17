package ja.ko.tomo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.feature.meeting.meetingcreate.MeetingCreateScreen
import ja.ko.tomo.feature.meeting.meetingcreate.MeetingCreateViewModel
import ja.ko.tomo.feature.meeting.meetingdetail.MeetingDetailScreen
import ja.ko.tomo.feature.meeting.meetingdetail.MeetingDetailViewModel
import ja.ko.tomo.feature.meeting.meetinglist.MeetingListScreen
import ja.ko.tomo.feature.meeting.meetinglist.MeetingListViewModel



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val listViewModel: MeetingListViewModel by viewModels()

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
                }
            }
        }
    }
}
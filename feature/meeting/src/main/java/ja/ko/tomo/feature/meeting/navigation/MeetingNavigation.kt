package ja.ko.tomo.feature.meeting

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.feature.meeting.meetingcreate.MeetingCreateScreen
import ja.ko.tomo.feature.meeting.meetingcreate.MeetingCreateViewModel
import ja.ko.tomo.feature.meeting.meetingdetail.MeetingDetailScreen
import ja.ko.tomo.feature.meeting.meetingdetail.MeetingDetailViewModel
import ja.ko.tomo.feature.meeting.meetinglist.MeetingListScreen
import ja.ko.tomo.feature.meeting.meetinglist.MeetingListViewModel

fun NavGraphBuilder.meetingGraph(
    navController: NavController,
    listViewModel: MeetingListViewModel
) {
    // 모임 리스트
    composable(
        route = TomoNavRoutes.MeetingList
    ) {
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

    // 모임 상세
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

    // 모임 생성
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
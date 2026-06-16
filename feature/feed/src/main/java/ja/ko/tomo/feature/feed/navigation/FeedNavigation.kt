package ja.ko.tomo.feature.feed.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.feature.feed.create.FeedCreateScreen
import ja.ko.tomo.feature.feed.create.FeedCreateViewModel
import ja.ko.tomo.feature.feed.feedlist.FeedListScreen
import ja.ko.tomo.feature.feed.feedlist.FeedViewModel

fun NavGraphBuilder.feedGraph(
    navController: NavController
) {
    // 1. 피드 리스트 화면
    composable(TomoNavRoutes.FeedList) {
        val viewModel: FeedViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        FeedListScreen(
            state = uiState,
            effect = viewModel.uiEffect,
            onFeedClick = viewModel::onFeedClick,
            onCallRoomClick = viewModel::onCallRoomClick,
            onCreateClick = viewModel::onCreateFeedClick,
            onNavigateToDetail = { feedId ->
                // TODO: navController.navigate(TomoNavRoutes.feedDetailRoute(feedId))
            },
            onNavigateToCreate = {
                navController.navigate(TomoNavRoutes.FeedCreate)
            },
            onFilterClick = {},
            onRetry = viewModel::onRefresh
        )
    }

    // 2. 피드 작성 화면
    composable(TomoNavRoutes.FeedCreate) {
        val viewModel: FeedCreateViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        FeedCreateScreen(
            state = uiState,
            effect = viewModel.uiEffect,
            onContentChange = viewModel::onContentChange,
            onHasCallRoomChange = viewModel::onHasCallRoomChange,
            onCreateClick = viewModel::onCreateClick,
            onBackClick = viewModel::onBackClick,
            onNavigateBack = {
                navController.popBackStack()
            },
            onRetry = viewModel::onRefresh
        )
    }
}
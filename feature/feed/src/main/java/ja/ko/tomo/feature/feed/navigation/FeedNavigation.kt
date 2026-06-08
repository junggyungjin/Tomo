package ja.ko.tomo.feature.feed.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ja.ko.tomo.core.navigation.TomoNavRoutes
import ja.ko.tomo.feature.feed.FeedListScreen
import ja.ko.tomo.feature.feed.FeedViewModel

fun NavGraphBuilder.feedGraph(
    navController: NavController
) {
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
                // TODO: navController.navigate(TomoNavRoutes.FEED_CREATE)
            },
            onRetry = viewModel::fetchFeeds
        )
    }
}
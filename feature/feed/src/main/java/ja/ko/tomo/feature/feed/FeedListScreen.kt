package ja.ko.tomo.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.component.TomoSnackbar
import ja.ko.tomo.core.ui.component.TomoStateView
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.feed.model.CallRoom
import ja.ko.tomo.domain.feed.model.Feed
import ja.ko.tomo.domain.feed.model.RoomStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.Date

@Composable
fun FeedListScreen(
    state: FeedUiState,
    effect: Flow<FeedUiEffect>,
    onFeedClick: (String) -> Unit,
    onCallRoomClick: (String) -> Unit,
    onCreateClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is FeedUiEffect.NavigateToDetail -> onNavigateToDetail(uiEffect.feedId)
                is FeedUiEffect.NavigateToCreateFeed -> onNavigateToCreate()
                is FeedUiEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = uiEffect.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                TomoSnackbar(snackbarData = data)
            }
        }
    ) { paddding ->
        TomoStateView(
            isLoading = state is FeedUiState.Loading,
            errorMessage = (state as? FeedUiState.Error)?.message,
            isSuccess = state is FeedUiState.Success,
            onRetry = onRetry,
            modifier = Modifier.padding(paddding)
        ) {
            FeedListContent(
                state = state as FeedUiState.Success,
                onFeedClick = onFeedClick,
                onCallRoomClick = onCallRoomClick
            )
        }
    }
}

@Composable
private fun FeedListContent(
    state: FeedUiState.Success,
    onFeedClick: (String) -> Unit,
    onCallRoomClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 통화룸 영역
        item {
            CallRoomStorySection(
                activeRooms = state.activeCallRooms,
                onRoomClick = onCallRoomClick
            )
        }

        // 피드 리스트
        items(state.feeds) { feed ->
            FeedItemCard(
                feed = feed,
                onClick = { onFeedClick(feed.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CallRoomStorySection(
    activeRooms: List<Feed>,
    onRoomClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Active Call Rooms",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(activeRooms) { room ->
                Surface(
                    onClick = { room.callRoom?.let { onRoomClick(it.id) }},
                    shape = CircleShape,
                    modifier = Modifier.size(72.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = room.authorId.take(1).uppercase(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedItemCard(
    feed: Feed,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = feed.authorId,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            feed.content?.let {
                Text(text = it, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = feed.createdAt.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "로딩 상태")
@Composable
private fun FeedListScreenLoadingPreview() {
    TomoTheme {
        FeedListScreen(
            state = FeedUiState.Loading,
            effect = emptyFlow(),
            onFeedClick = {},
            onCallRoomClick = {},
            onCreateClick = {},
            onNavigateToDetail = {},
            onNavigateToCreate = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "성공 상태 - 데이터 있음")
@Composable
private fun FeedListScreenSuccessPreview() {
    val dummyFeeds = listOf(
        Feed(
            id = "1",
            content = "오늘 날씨가 정말 좋네요! 다들 뭐하시나요?",
            authorId = "UserA",
            callRoom = CallRoom("room1", RoomStatus.OPEN, 5, 2),
            createdAt = Date()
        ),
        Feed(
            id = "2",
            content = "맛있는 점심 추천해주세요.",
            authorId = "UserB",
            callRoom = null,
            createdAt = Date()
        ),
        Feed(
            id = "3",
            content = "새로운 취미를 찾고 있어요.",
            authorId = "UserC",
            callRoom = CallRoom("room2", RoomStatus.OPEN, 8, 4),
            createdAt = Date()
        )
    )

    TomoTheme {
        FeedListScreen(
            state = FeedUiState.Success(
                feeds = dummyFeeds,
                activeCallRooms = dummyFeeds.filter { it.callRoom != null && it.callRoom?.status == RoomStatus.OPEN }
            ),
            effect = emptyFlow(),
            onFeedClick = {},
            onCallRoomClick = {},
            onCreateClick = {},
            onNavigateToDetail = {},
            onNavigateToCreate = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "성공 상태 - 데이터 없음")
@Composable
private fun FeedListScreenEmptyPreview() {
    TomoTheme {
        FeedListScreen(
            state = FeedUiState.Success(
                feeds = emptyList(),
                activeCallRooms = emptyList()
            ),
            effect = emptyFlow(),
            onFeedClick = {},
            onCallRoomClick = {},
            onCreateClick = {},
            onNavigateToDetail = {},
            onNavigateToCreate = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "에러 상태")
@Composable
private fun FeedListScreenErrorPreview() {
    TomoTheme {
        FeedListScreen(
            state = FeedUiState.Error(
                message = UiText.DynamicString("서버 연결에 실패했습니다. 다시 시도해 주세요.")
            ),
            effect = emptyFlow(),
            onFeedClick = {},
            onCallRoomClick = {},
            onCreateClick = {},
            onNavigateToDetail = {},
            onNavigateToCreate = {},
            onRetry = {}
        )
    }
}
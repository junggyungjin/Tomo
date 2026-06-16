package ja.ko.tomo.feature.feed.feedlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.component.TomoSnackbar
import ja.ko.tomo.core.ui.component.TomoStateView
import ja.ko.tomo.core.ui.theme.Black
import ja.ko.tomo.core.ui.theme.ErrorRed
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.core.ui.theme.TomoTypography
import ja.ko.tomo.core.ui.theme.White
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.feed.model.CallRoom
import ja.ko.tomo.domain.feed.model.Feed
import ja.ko.tomo.domain.feed.model.RoomStatus
import ja.ko.tomo.domain.model.FeedFilter
import ja.ko.tomo.feature.feed.R
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
    onFilterClick: (FeedFilter) -> Unit,
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
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                TomoSnackbar(snackbarData = data)
            }
        },
        topBar = {
            FeedTopBar(onCreateClick = onCreateClick)
        }
    ) { padding ->
        TomoStateView(
            isLoading = state is FeedUiState.Loading,
            errorMessage = (state as? FeedUiState.Error)?.message,
            isSuccess = state is FeedUiState.Success,
            onRetry = onRetry,
            modifier = Modifier.padding(padding)
        ) {
            FeedListContent(
                state = state as FeedUiState.Success,
                onFeedClick = onFeedClick,
                onCallRoomClick = onCallRoomClick,
                onFilterClick = onFilterClick
            )
        }
    }
}

@Composable
private fun FeedListContent(
    state: FeedUiState.Success,
    onFeedClick: (String) -> Unit,
    onCallRoomClick: (String) -> Unit,
    onFilterClick: (FeedFilter) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // 통화룸 영역
        item {
            CallRoomStorySection(
                activeRooms = state.activeCallRooms,
                onRoomClick = onCallRoomClick
            )
        }

        item {
            FeedFilterTab(
                selectedFilter = state.selectedFilter,
                onFilterClick = onFilterClick
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

@Composable
private fun FeedTopBar(
    onCreateClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.feed_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TopBarIconButton(icon = Icons.Default.Add, onClick = onCreateClick)
                TopBarIconButton(icon = Icons.Default.Notifications, onClick = {})
                TopBarIconButton(icon = Icons.Default.ChatBubbleOutline, onClick = {})
            }
        }
    }
}

@Composable
private fun TopBarIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.White
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
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp), // 아이템 간 간격
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(activeRooms) { room ->
            CallRoomItem(
                name = room.authorId,
                onClick = { room.callRoom?.let { onRoomClick(it.id) }}
            )
        }
    }
}

@Composable
private fun CallRoomItem(
    name: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            modifier = Modifier.size(72.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = name.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun FeedFilterTab(
    selectedFilter: FeedFilter,
    onFilterClick: (FeedFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(52.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterTabItem(
            text = stringResource(id = R.string.feed_filter_following),
            isSelected = selectedFilter == FeedFilter.FOLLOWING,
            onClick = { onFilterClick(FeedFilter.FOLLOWING) },
            modifier = Modifier.weight(1f)
        )
        FilterTabItem(
            text = stringResource(id = R.string.feed_filter_all),
            isSelected = selectedFilter == FeedFilter.ALL,
            onClick = { onFilterClick(FeedFilter.ALL) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FilterTabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = if (isSelected) White else Color.Transparent,
        modifier = modifier.fillMaxHeight()
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) Black else White.copy(alpha = 0.6f),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 헤더
            FeedItemHeader(authorId = feed.authorId)

            Spacer(modifier = Modifier.height(16.dp))

            // 본문 내용
            feed.content?.let {
                Text(
                    text = it,
                    style = TomoTypography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 푸터
            FeedItemFooter()
        }
    }
}

@Composable
private fun FeedItemHeader(
    authorId: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = authorId.take(1).uppercase(),
                        style = TomoTypography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column{
                Text(
                    text = authorId,
                    style = TomoTypography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "864 followers", // 임시 데이터
                    style = TomoTypography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Follow 버튼
        Surface(
            onClick = {},
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.onSurface,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Follow",
                    style = TomoTypography.labelSmall,
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 피드 하단 푸터
@Composable
private fun FeedItemFooter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 소셜 액션 그룹
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(26.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SocialActionButton(icon = Icons.Default.Favorite, count = "216", iconColor = ErrorRed)
                SocialActionButton(icon = Icons.Default.ChatBubbleOutline, count = "108")
                SocialActionButton(icon = Icons.Default.Share, count = "102")
            }
        }

        // 북마크
        Surface(
            onClick = {},
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(44.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SocialActionButton(
    icon: ImageVector,
    count: String,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Surface(
        onClick = {},
        color = Color.Transparent,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = count,
                style = TomoTypography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
            onFilterClick = {},
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
            content = "오늘 날씨가 정말 좋네요! 다들 뭐하시나요?오늘 날씨가 정말 좋네요! 다들 뭐하시나요?오늘 날씨가 정말 좋네요! 다들 뭐하시나요?오늘 날씨가 정말 좋네요! 다들 뭐하시나요?",
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
            onFilterClick = {},
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
            onFilterClick = {},
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
            onFilterClick = {},
            onRetry = {}
        )
    }
}
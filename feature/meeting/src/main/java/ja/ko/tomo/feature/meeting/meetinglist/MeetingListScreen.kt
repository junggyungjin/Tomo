package ja.ko.tomo.feature.meeting.meetinglist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.theme.Black
import ja.ko.tomo.core.ui.theme.DarkGray
import ja.ko.tomo.core.ui.theme.DarkerGrey
import ja.ko.tomo.core.ui.theme.Gray
import ja.ko.tomo.core.ui.theme.LightGrey
import ja.ko.tomo.core.ui.theme.MediumGrey
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.domain.model.Meeting

@Composable
fun MeetingListScreen(
    state: MeetingListUiState,
    onMeetingClick: (Long) -> Unit,
    onFilterChange: (MeetingListFilter) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onNavigateToCreate: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            // 모임 생성 버튼
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = TomoBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "모임 생성")
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Scaffold의 패딩 적용
            color = Gray
        ) {
            when (state) {
                MeetingListUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Loading..")
                    }
                }

                MeetingListUiState.Empty -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "예정된 모임이 없습니다")
                    }
                }

                is MeetingListUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message)
                    }
                }

                is MeetingListUiState.Success -> {
                    // 전체를 Column으로 감싸 헤더와 컨텐츠 영역을 나눕니다.
                    Column(
                        modifier = Modifier
                            .fillMaxSize().padding(horizontal = 16.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TOMO",
                            color = TomoBlue,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            fontSize = 36.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "한일 교류 모임 목록",
                            color = DarkGray,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onFilterChange(MeetingListFilter.ALL) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (state.selectedFilter == MeetingListFilter.ALL) TomoBlue else Color.White
                                )
                            ) {
                                Text(
                                    text = "전체",
                                    color = if (state.selectedFilter == MeetingListFilter.ALL) Color.White else DarkGray
                                )
                            }

                            Button(
                                onClick = { onFilterChange(MeetingListFilter.JOINED) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (state.selectedFilter == MeetingListFilter.JOINED) TomoBlue else Color.White
                                )
                            ) {
                                Text(
                                    text = "참가한 모임",
                                    color = if (state.selectedFilter == MeetingListFilter.JOINED) Color.White else DarkGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { onSearchQueryChange(it) },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            placeholder = { Text("제목 또는 장소로 검색") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null)},
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (state.meetings.isEmpty()) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when {
                                        state.searchQuery.isNotEmpty() -> "검색 결과가 없습니다"
                                        state.selectedFilter == MeetingListFilter.JOINED -> "참가한 모임이 없습니다."
                                        else -> "예정된 모임이 없습니다"
                                    },
                                    color = DarkGray
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f), // 리스트도 남은 공간을 차지하도록 설정
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(state.meetings) { meeting ->
                                    MeetingListItem(
                                        meeting = meeting,
                                        onClick = onMeetingClick,
                                        onToggleFavorite = onToggleFavorite
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun MeetingListItem(
    meeting: Meeting,
    onClick: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(meeting.id) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = meeting.title,
                    color = Black,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )

                // 찜 버튼
                IconButton(onClick = { onToggleFavorite(meeting.id)}) {
                    Icon(
                        imageVector = if (meeting.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "찜하기",
                        tint = if (meeting.isFavorite) Color.Red else DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = meeting.subtitle,
                color = DarkGray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = LightGrey
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "date time",
                    tint = MediumGrey,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = meeting.dateTime,
                    color = DarkerGrey,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = LightGrey
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "location",
                    tint = MediumGrey,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = meeting.location,
                    color = DarkerGrey,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = when {
                    meeting.isClosed -> "마감됨"
                    meeting.isJoined -> "참가 중"
                    else -> "모집중"
                },
                color = when {
                    meeting.isClosed -> DarkGray
                    meeting.isJoined -> TomoBlue
                    else -> TomoBlue
                },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun MeetingListLoadingPreview() {
    TomoTheme {
        MeetingListScreen(
            state = MeetingListUiState.Loading,
            onMeetingClick = {},
            onFilterChange = {},
            onSearchQueryChange = {},
            onToggleFavorite =  {},
            onNavigateToCreate = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun MeetingListSuccessPreview() {
    TomoTheme {
        MeetingListScreen(
            state = MeetingListUiState.Success(
                meetings = listOf(
                    Meeting(
                        id = 1L,
                        title = "서울 한일 언어교환 모임",
                        subtitle = "한국어 + 일본어 자유대화",
                        dateTime = "2026.03.22 일요일 19:00",
                        location = "홍대입구 카페 하루",
                        isClosed = false,
                        isJoined = false,
                        isFavorite = false,
                        capacity = 4
                    ),
                    Meeting(
                        id = 2L,
                        title = "강남 일본어 스터디",
                        subtitle = "JLPT N2 수준 회화 스터디",
                        dateTime = "2026.03.23 월요일 18:30",
                        location = "강남역 스터디룸",
                        isClosed = false,
                        isJoined = false,
                        isFavorite = false,
                        capacity = 4
                    ),
                    Meeting(
                        id = 3L,
                        title = "성수 한일 교류 번개",
                        subtitle = "가볍게 커피 마시며 자유대화",
                        dateTime = "2026.03.24 화요일 20:00",
                        location = "성수동 카페",
                        isClosed = false,
                        isJoined = false,
                        isFavorite = false,
                        capacity = 4
                    )
                ),
                selectedFilter = MeetingListFilter.ALL
            ),
            onMeetingClick = {},
            onFilterChange =  {},
            onSearchQueryChange = {},
            onToggleFavorite = {},
            onNavigateToCreate = {}
        )
    }
}
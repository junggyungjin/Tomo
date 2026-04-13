package ja.ko.tomo.presentation.meetingdetail

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.domain.model.Meeting
import ja.ko.tomo.ui.theme.Black
import ja.ko.tomo.ui.theme.DarkGray
import ja.ko.tomo.ui.theme.DarkGrey
import ja.ko.tomo.ui.theme.DarkerGrey
import ja.ko.tomo.ui.theme.Gray
import ja.ko.tomo.ui.theme.LightGrey
import ja.ko.tomo.ui.theme.MediumGrey
import ja.ko.tomo.ui.theme.TomoBlue
import ja.ko.tomo.ui.theme.TomoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun MeetingDetailScreen(
    state: MeetingDetailUiState,
    effect: Flow<MeetingDetailUiEffect>,
    onBackClick: () -> Unit,
    onActionButtonClick: () -> Unit,
    onToggleFavorite: (Long) -> Unit
) {
    val context = LocalContext.current
    // Snackbar를 위해 필요한 상태
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is MeetingDetailUiEffect.ShowToast -> {
                    Toast.makeText(context, uiEffect.message, Toast.LENGTH_SHORT).show()
                }
                MeetingDetailUiEffect.NavigateBack -> {
                    onBackClick()
                }
                is MeetingDetailUiEffect.ShowSnackbar -> {
                    // 새로운 코루틴 스코프안에서 실행하여 스낵바 시간을 제어
                    val job = launch {
                        snackbarHostState.showSnackbar(
                            message = uiEffect.message,
                            duration = SnackbarDuration.Indefinite // 일단 스낵바를 무한정 띄움
                        )
                    }
                    delay(500L) // 여기서 스낵바 시간조절
                    job.cancel() // 시간이 다 되면 스낵바 요청 작업 자체를 취소
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Gray
        ) {
            when (state) {
                MeetingDetailUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Loading...")
                    }
                }

                is MeetingDetailUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message)
                    }
                }

                is MeetingDetailUiState.Success -> {
                    MeetingDetailContent(
                        successState = state,
                        onActionButtonClick = onActionButtonClick,
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }
        }
    }
}

@Composable
private fun MeetingDetailContent(
    successState: MeetingDetailUiState.Success,
    onActionButtonClick: () -> Unit,
    onToggleFavorite: (Long) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
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
                text = "모임 상세",
                color = DarkGray,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = successState.meeting.title,
                        color = Black,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = successState.meeting.subtitle,
                        color = DarkGrey,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = LightGrey
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "date time",
                            tint = MediumGrey,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = successState.meeting.dateTime,
                            color = DarkerGrey,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = LightGrey
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                            text = successState.meeting.location,
                            color = DarkerGrey,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = LightGrey
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = when {
                            successState.meeting.isClosed -> "마감된 모임입니다"
                            successState.meeting.isJoined -> "참가 중인 모임입니다"
                            else -> "현재 모집 중입니다"
                        },
                        color = when {
                            successState.meeting.isClosed -> DarkGray
                            successState.meeting.isJoined -> TomoBlue
                            else -> TomoBlue
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onActionButtonClick,
                        enabled = successState.isButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TomoBlue
                        )
                    ) {
                        Text(
                            text = successState.buttonText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = { onToggleFavorite(successState.meeting.id)},
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 12.dp)
        ) {
            Icon(
                imageVector = if (successState.meeting.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "찜하기",
                tint = if (successState.meeting.isFavorite) Color.Red else DarkGray,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun MeetingDetailLoadingPreview() {
    TomoTheme{
        MeetingDetailScreen(
            state = MeetingDetailUiState.Loading,
            effect = emptyFlow(),
            onBackClick = {},
            onActionButtonClick = {},
            onToggleFavorite = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun MeetingDetailSuccessPreview() {
    TomoTheme{
        MeetingDetailScreen(
            state = MeetingDetailUiState.Success(
                meeting = Meeting(
                    id = 1L,
                    title = "서울 한일 언어교환 모임",
                    subtitle = "한국어 + 일본어 자유대화",
                    dateTime = "2026.03.22 일요일 19:00",
                    location = "홍대입구 카페 하루",
                    isClosed = false,
                    isJoined = false,
                    isFavorite = false
                ),
                buttonText = "참가하기",
                isButtonEnabled = true
            ),
            effect = emptyFlow(),
            onBackClick = {},
            onActionButtonClick = {},
            onToggleFavorite = {}
        )
    }
}
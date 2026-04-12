package ja.ko.tomo.presentation.meeting

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun TomoMeetingScreen(
    state: TomoMeetingUiState,
    event: SharedFlow<TomoMeetingEvent>,
    onJoinClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        event.collect { meetingEvent ->
            when (meetingEvent) {
                is TomoMeetingEvent.ShowToast -> {
                    Toast.makeText(context, meetingEvent.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Gray
    ) {
        when (state) {
            TomoMeetingUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Loading...")
                }
            }

            TomoMeetingUiState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "오늘 예정된 한일 교류 모임이 없습니다")
                }
            }

            is TomoMeetingUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message)
                }
            }

            is TomoMeetingUiState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOMO",
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = TomoBlue,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                        ),
                        fontSize = 40.sp
                    )
                    Text(
                        text = "오늘의 한일 교류 모임",
                        modifier = Modifier.padding(bottom = 20.dp),
                        color = DarkGray,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        fontSize = 18.sp
                    )
                    TomoMeetingCard(
                        successState = state,
                        onJoinClick = onJoinClick
                    )
                }
            }
        }

    }
}

@Composable
private fun TomoMeetingCard(
    successState: TomoMeetingUiState.Success,
    onJoinClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(50.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 25.dp
                )
                .fillMaxWidth(0.82f)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = successState.meeting.title,
                color = Black,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                fontSize = 23.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = successState.meeting.subtitle,
                color = DarkGrey,
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
                    text = successState.meeting.dateTime,
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
                    text = successState.meeting.location,
                    color = DarkerGrey,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onJoinClick,
                enabled = successState.isJoinEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TomoBlue
                )
            ) {
                Text(
                    text = successState.buttonText,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun TomoMeetingLoadingPreview() {
    TomoTheme {
        TomoMeetingScreen(
            state = TomoMeetingUiState.Loading,
            event = MutableSharedFlow(),
            onJoinClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun TomoMeetingSuccessPreview() {
    TomoTheme {
        TomoMeetingScreen(
            state = TomoMeetingUiState.Success(
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
                isJoinEnabled = true
            ),
            event = MutableSharedFlow(),
            onJoinClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun TomoMeetingEmptyPreview() {
    TomoTheme {
        TomoMeetingScreen(
            state = TomoMeetingUiState.Empty,
            event = MutableSharedFlow(),
            onJoinClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun TomoMeetingErrorPreview() {
    TomoTheme {
        TomoMeetingScreen(
            state = TomoMeetingUiState.Error(
                message = "모임 정보를 불러오지 못했습니다"
            ),
            event = MutableSharedFlow(),
            onJoinClick = {}
        )
    }
}
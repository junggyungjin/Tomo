package ja.ko.tomo.presentation.meetingdetail

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
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun MeetingDetailScreen(
    state: MeetingDetailUiState,
    onActionButtonClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
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
                    onActionButtonClick = onActionButtonClick
                )
            }
        }
    }
}

@Composable
private fun MeetingDetailContent(
    successState: MeetingDetailUiState.Success,
    onActionButtonClick: () -> Unit
) {
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
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun MeetingDetailLoadingPreview() {
    TomoTheme{
        MeetingDetailScreen(
            state = MeetingDetailUiState.Loading,
            onActionButtonClick = {}
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
                    isJoined = false
                ),
                buttonText = "참가하기",
                isButtonEnabled = true
            ),
            onActionButtonClick = {}
        )
    }
}
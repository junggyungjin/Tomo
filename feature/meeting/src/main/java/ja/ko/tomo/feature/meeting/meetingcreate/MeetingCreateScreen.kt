package ja.ko.tomo.feature.meeting.meetingcreate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ja.ko.tomo.core.ui.theme.TomoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun MeetingCreateScreen(
    state: MeetingCreateUiState,
    effect: Flow<MeetingCreateUiEffect>,
    onTitleChange: (String) -> Unit,
    onSubtitleChange: (String) -> Unit,
    onDateTimeChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onCapacityChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is MeetingCreateUiEffect.ShowSnackbar -> {
                    val job = launch {
                        snackbarHostState.showSnackbar(
                            message = uiEffect.message,
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                    delay(500)
                    job.cancel()
                }
                MeetingCreateUiEffect.NavigateBack -> {
                    onBackClick()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { }
    ) { padding ->
        when(state) {
            is MeetingCreateUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState()) // TODO 질문 이게 무슨 코드야?
                ) {
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = onTitleChange,
                        label = { Text("제목") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.subtitle,
                        onValueChange = onSubtitleChange,
                        label = { Text("부제목") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.dateTime,
                        onValueChange = onDateTimeChange,
                        label = { Text("시간") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.location,
                        onValueChange = onLocationChange,
                        label = { Text("장소") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.capacity,
                        onValueChange = onCapacityChange,
                        label = { Text("정원") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onSaveClick,
                        enabled = state.isSaveEnabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("모임 만들기")
                    }
                }
            }

            MeetingCreateUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Loading...")
                }
            }

            is MeetingCreateUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(state.message)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun MeetingCreateScreenPreview() {
    TomoTheme{
        MeetingCreateScreen(
            state = MeetingCreateUiState.Success(
                title = "세크수모임",
                subtitle = "영차",
                dateTime = "야심한 밤",
                location = "라부호텔",
                capacity = "4",
                isSaveEnabled = false,
                isSubmitting = false
            ),
            effect = emptyFlow(),
            onTitleChange = {},
            onSubtitleChange = {},
            onDateTimeChange = {},
            onLocationChange = {},
            onCapacityChange = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
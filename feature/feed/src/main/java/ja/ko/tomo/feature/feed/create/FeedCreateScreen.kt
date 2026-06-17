package ja.ko.tomo.feature.feed.create

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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ja.ko.tomo.core.ui.component.TomoSnackbar
import ja.ko.tomo.core.ui.component.TomoStateView
import ja.ko.tomo.core.ui.theme.Gray
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.core.ui.theme.White
import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.feature.feed.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * 피드 작성 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedCreateScreen(
    state: FeedCreateUiState,
    effect: Flow<FeedCreateUiEffect>,
    onContentChange: (String) -> Unit,
    onHasCallRoomChange: (Boolean) -> Unit,
    onCreateClick: () -> Unit,
    onBackClick: () -> Unit,
    onNavigateBack: (Boolean) -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(effect) {
        effect.collect { uiEffect ->
            when (uiEffect) {
                is FeedCreateUiEffect.NavigateBack -> onNavigateBack(uiEffect.isSuccess)
                is FeedCreateUiEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = uiEffect.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                TomoSnackbar(snackbarData = data)
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.feed_create_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        TomoStateView(
            isLoading = state is FeedCreateUiState.Loading,
            errorMessage = (state as? FeedCreateUiState.Error)?.message,
            isSuccess = state is FeedCreateUiState.Success,
            onRetry = onRetry,
            modifier = Modifier.padding(padding)
        ) {
            FeedCreateContent(
                state = state as FeedCreateUiState.Success,
                onContentChange = onContentChange,
                onHasCallRoomChange = onHasCallRoomChange,
                onCreateClick = onCreateClick
            )
        }
    }
}

@Composable
private fun FeedCreateContent(
    state: FeedCreateUiState.Success,
    onContentChange: (String) -> Unit,
    onHasCallRoomChange: (Boolean) -> Unit,
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            value = state.content,
            onValueChange = onContentChange,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(
                    text = stringResource(R.string.feed_create_placeholder),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Gray
                )
            },
            enabled = !state.isSubmitting,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 통화방 생성 옵션 영역
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.hasCallRoom,
                    onCheckedChange = onHasCallRoomChange,
                    enabled = !state.isSubmitting
                )
                Text(
                    text = stringResource(R.string.feed_create_call_room_option),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onCreateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = state.canSubmit,
            shape = RoundedCornerShape(28.dp)
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.feed_create_submit),
                    style = MaterialTheme.typography.bodyLarge.copy(color = White)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "성공 상태")
@Composable
private fun FeedCreateScreenSuccessPreview() {
    TomoTheme {
        FeedCreateScreen(
            state = FeedCreateUiState.Success(content = "테스트 피드 내용입니다."),
            effect = emptyFlow(),
            onContentChange = {},
            onHasCallRoomChange = {},
            onCreateClick = {},
            onBackClick = {},
            onNavigateBack = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "로딩 상태")
@Composable
private fun FeedCreateScreenLoadingPreview() {
    TomoTheme {
        FeedCreateScreen(
            state = FeedCreateUiState.Loading,
            effect = emptyFlow(),
            onContentChange = {},
            onHasCallRoomChange = {},
            onCreateClick = {},
            onBackClick = {},
            onNavigateBack = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740, name = "에러 상태")
@Composable
private fun FeedCreateScreenErrorPreview() {
    TomoTheme {
        FeedCreateScreen(
            state = FeedCreateUiState.Error(UiText.DynamicString("네트워크 연결 실패")),
            effect = emptyFlow(),
            onContentChange = {},
            onHasCallRoomChange = {},
            onCreateClick = {},
            onBackClick = {},
            onNavigateBack = {},
            onRetry = {}
        )
    }
}
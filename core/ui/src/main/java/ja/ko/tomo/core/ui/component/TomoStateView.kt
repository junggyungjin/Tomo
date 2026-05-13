package ja.ko.tomo.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ja.ko.tomo.core.ui.R
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.core.ui.util.UiText

/**
 * Tomo 프로젝트 전역 UI 상태 관리 컨벤션
 *
 * @param isLoading 전체 화면 로딩 여부
 * @param errorMessage 에러 발생 시 표시할 UiText
 * @param isSuccess 성공 상태 (콘텐츠 표시 가능 여부)
 * @param onRetry 에러 화면에서 '다시 시도' 버튼 클릭 시 콜백 // ADDED
 * @param modifier 레이아웃 수정을 위한 Modifier
 */
@Composable
fun TomoStateView(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    errorMessage: UiText? = null,
    isSuccess: Boolean = false,
    onRetry: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TomoBlue)
                }
            }
            errorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = errorMessage.asString(),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = TomoBlue)
                    ) {
                        Text(text = stringResource(R.string.core_common_retry), color = Color.White)
                    }
                }
            }
            isSuccess -> {
                content()
            }
        }
    }
}
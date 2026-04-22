package ja.ko.tomo.feature.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.theme.DarkGray
import ja.ko.tomo.core.ui.theme.Gray
import ja.ko.tomo.core.ui.theme.TomoBlue
import ja.ko.tomo.core.ui.theme.TomoTheme
import ja.ko.tomo.domain.model.User

@Composable
fun MyPageScreen(
    state: MyPageUiState
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Gray
    ) {
        when (state) {
            MyPageUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Loading...")
                }
            }
            is MyPageUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message)
                }
            }
            is MyPageUiState.Success -> {
                MyPageContent(user = state.user)
            }
        }
    }
}

@Composable
private fun MyPageContent(
    user: User
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // 프사
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Picture",
            modifier = Modifier.size(120.dp),
            tint = TomoBlue
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 닉네임
        Text(
            text = user.nickname,
            fontSize = 28.sp,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold
            )
        )

        // 이메일
        Text(
            text = user.email,
            fontSize = 16.sp,
            color = DarkGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 자기소개 라벨
        Text(
            text = "자기소개",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        // 자기소개 내용
        Text(
            text = user.introduction,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Start),
            lineHeight = 24.sp // TODO 질문 이건 무슨 옵션이야?
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun MyPageContentPreview() {
    TomoTheme{
        MyPageContent(
            user = User(
                id = 1L,
                email = "asdf@asdf",
                nickname = "개발자",
                profileImageUrl = null,
                introduction = "hihi"
            )
        )
    }
}
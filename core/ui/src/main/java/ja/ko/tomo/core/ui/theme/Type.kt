package ja.ko.tomo.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ja.ko.tomo.core.ui.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

// Pretendard 폰트
val PretendardFontFamily = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_light, FontWeight.Light)
)

// 2. 앱 전체에서 사용할 타이포그래피(글꼴 스타일) 정의
val TomoTypography = Typography(
    // [Headline] 가장 큰 제목 (예: 온보딩 타이틀, 홈 화면 최상단 타이틀)
    headlineLarge = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.5).sp
    ),

    // ADDED: [Title] 중간 크기의 제목 (예: 다이얼로그 제목, 각 섹션의 타이틀)
    titleLarge = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold, // Headline보단 살짝 얇게
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.4).sp
    ),

    // ADDED: [Body] 강조되는 본문 (예: 피드 본문 첫 줄, 중요한 안내 문구)
    bodyLarge = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium, // 일반 본문보단 살짝 또렷하게
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.3).sp
    ),

    // [Body] 일반 본문 텍스트 (예: 피드 내용, 상세 설명)
    bodyMedium = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.2).sp
    ),

    // [Label] 작은 부가 정보 (예: 닉네임 아랫줄, 시간, 조회수, 버튼 안의 텍스트)
    labelSmall = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium, // 크기가 작으므로 Normal보단 Medium으로 가독성 확보
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )
)
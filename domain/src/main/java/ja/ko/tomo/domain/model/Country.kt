package ja.ko.tomo.domain.model

data class Country(
    val name: String,
    val code: String, // 예: "KR", "US"
    val flag: String  // 이모지 또는 리소스 ID
)

val countries = listOf(
    Country("대한민국", "KR", "🇰🇷"),
    Country("일본", "JP", "🇯🇵")
)
package ja.ko.tomo.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ja.ko.tomo.data.remote.AuthService
import ja.ko.tomo.data.remote.UserApiService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://192.168.45.197:3000/" // 서버 주소

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true // 서버에서 내려주는 필드가 DTO에 없어도 에러내지 않음
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor.Logger { message ->
            if (!message.startsWith("{") && !message.startsWith("[")) {
                Timber.tag("OkHttp").d(message)
                return@Logger
            }

            try {
                val prettyJson = Json { prettyPrint = true }
                val jsonElement = prettyJson.parseToJsonElement(message)
                val formattedJson = prettyJson.encodeToString(JsonElement.serializer(), jsonElement)

                Timber.tag("OkHttp").d("\n$formattedJson")
            }catch (e: Exception) {
                Timber.tag("OkHttp").d(message)
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(logger).apply {
                level = HttpLoggingInterceptor.Level.BODY // 개발 단계에선 로그 전체 확인
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }
}
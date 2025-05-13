package android.test.forexwatch.core.di

import android.test.forexwatch.BuildConfig
import android.test.forexwatch.core.logging.AppLogger
import android.test.forexwatch.core.logging.Logger
import android.test.forexwatch.data.remote.api.FixerApiService
import android.test.forexwatch.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_URL = "https://data.fixer.io/api/"

    @Provides
    @Named("apiKey")
    fun provideApiKey(): String = BuildConfig.API_KEY



    @Provides
    @Singleton
    fun provideAuthInterceptor(@Named("apiKey") apiKey: String, logger: Logger): Interceptor {
        return AuthInterceptor(apiKey, logger)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideFixerApiService(retrofit: Retrofit): FixerApiService =
        retrofit.create(FixerApiService::class.java)
}
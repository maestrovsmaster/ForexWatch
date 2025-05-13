package android.test.forexwatch.core.di

import android.test.forexwatch.core.logging.AppLogger
import android.test.forexwatch.core.logging.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger = AppLogger()
}
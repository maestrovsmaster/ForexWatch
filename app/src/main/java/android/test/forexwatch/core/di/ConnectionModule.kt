package android.test.forexwatch.core.di

import android.content.Context
import android.test.forexwatch.core.connectivity.AndroidConnectivityObserver
import android.test.forexwatch.core.connectivity.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver = AndroidConnectivityObserver(context)
}

package android.test.forexwatch.core.di

import android.content.Context
import android.test.forexwatch.BuildConfig
import android.test.forexwatch.data.local.dao.CurrencyRateDao
import android.test.forexwatch.data.remote.api.FixerApiService
import android.test.forexwatch.data.repository.FixerRepositoryImpl
import android.test.forexwatch.domain.repository.FixerRepository
import android.test.forexwatch.fake.MockFixerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FixerModule {

    @Provides
    @Singleton
    fun provideFixerRepository(

        @ApplicationContext context: Context,
        api: FixerApiService,
        dao: CurrencyRateDao
    ): FixerRepository {
        return when (BuildConfig.BUILD_TYPE) {
            "mock" -> MockFixerRepository(context)
            else -> FixerRepositoryImpl(
                api = api,
                dao = dao

            )
        }
    }
}
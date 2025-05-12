package android.test.forexwatch.core.di

import android.content.Context
import android.test.forexwatch.data.local.AppDatabase
import android.test.forexwatch.data.local.dao.CurrencyRateDao
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "forex_database"
        ).build()

    @Provides
    @Singleton
    fun provideCurrencyRateDao(db: AppDatabase): CurrencyRateDao =
        db.currencyRateDao()
}

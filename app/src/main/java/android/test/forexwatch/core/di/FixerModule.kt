package android.test.forexwatch.core.di

import android.test.forexwatch.data.repository.FixerRepositoryImpl
import android.test.forexwatch.domain.repository.FixerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FixerModule {

    @Binds
    @Singleton
    abstract fun bindFixerRepository(
        impl: FixerRepositoryImpl
    ): FixerRepository
}

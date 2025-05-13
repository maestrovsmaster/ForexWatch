package android.test.forexwatch.core.di

import android.test.forexwatch.domain.usecase.get_rates_use_case.GetRatesUseCase
import android.test.forexwatch.domain.usecase.get_rates_use_case.GetRatesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetRatesUseCase(
        impl: GetRatesUseCaseImpl
    ): GetRatesUseCase
}

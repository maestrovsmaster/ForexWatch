package android.test.forexwatch.domain.usecase

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.usecase.rates_use_case.GetRatesUseCase
import android.test.forexwatch.domain.usecase.rates_use_case.GetRatesUseCaseImpl
import android.test.forexwatch.fake.MockFixerRepository
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetRatesUseCaseTest {

    val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val fakeRepo = MockFixerRepository(context = context)
    private lateinit var useCase: GetRatesUseCase

    @Before
    fun setUp() {
        useCase = GetRatesUseCaseImpl(fakeRepo)
    }

    @Test
    fun invokeReturnsSuccess_whenRepoSucceeds() = runTest {

        val result = useCase().drop(1).first()
        when (result) {
            is Resource.Success -> println("Success: ${result.data}")
            is Resource.Error -> println("Error: ${result.message}")
            is Resource.Loading -> println("Loading")
        }
        assertTrue(result is Resource.Success)
        assertFalse((result as Resource.Success).data.isNullOrEmpty())

    }

    @Test
    fun invokeReturnsError_whenRepoFails() = runTest {

        fakeRepo.shouldFail = true
        val useCase = GetRatesUseCaseImpl(fakeRepo)
        val result = useCase().first()
        assertTrue(result is Resource.Error)

    }

}

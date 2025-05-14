package android.test.forexwatch.domain.usecase

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.usecase.time_series_use_case.GetTimeSeriesUseCase
import android.test.forexwatch.domain.usecase.time_series_use_case.GetTimeSeriesUseCaseImpl
import android.test.forexwatch.fake.MockFixerRepository
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class GetTimeSeriesUseCaseTest {

    private lateinit var useCase: GetTimeSeriesUseCase
    private lateinit var fakeRepo: MockFixerRepository

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        fakeRepo = MockFixerRepository(context)
        useCase = GetTimeSeriesUseCaseImpl(fakeRepo)
    }

    @Test
    fun invokeReturnsSuccess_whenRepoSucceeds() = runTest {
        val result = useCase("UAH", LocalDate.parse("2025-05-07"), LocalDate.parse("2025-05-14"))
            .drop(1)
            .first()

        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertEquals("UAH", data.target)
        assertTrue(data.rates.isNotEmpty())
    }

    @Test
    fun invokeReturnsError_whenRepoFails() = runTest {
        fakeRepo.shouldFail = true
        val useCase = GetTimeSeriesUseCaseImpl(fakeRepo)

        val result = useCase("USD", LocalDate.now(), LocalDate.now().plusDays(1))
            .first { it is Resource.Error }

        assertTrue(result is Resource.Error)
    }

}

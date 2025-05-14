package android.test.forexwatch.data.repository

import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.data.local.dao.CurrencyRateDao
import android.test.forexwatch.data.local.entity.CurrencyRateEntity
import android.test.forexwatch.data.remote.api.FixerApiService
import android.test.forexwatch.data.remote.dto.FixerErrorDto
import android.test.forexwatch.data.remote.dto.FixerRatesResponseDto
import android.test.forexwatch.data.remote.dto.TimeSeriesResponseDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import android.test.forexwatch.data.remote.enums.ApiErrorType

@OptIn(ExperimentalCoroutinesApi::class)
class FixerRepositoryImplTest {

    private lateinit var api: FixerApiService
    private lateinit var dao: CurrencyRateDao
    private lateinit var repo: FixerRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        dao = mockk(relaxed = true)
        repo = FixerRepositoryImpl(api, dao)
    }

    @Test
    fun returnsCachedData_whenNotStale_andForceRefreshIsFalse() = runTest {

        val cachedRates = listOf(CurrencyRateEntity("USD", 1.1, 123456L))
        coEvery { dao.getCachedOnce() } returns cachedRates
        coEvery { dao.getLastUpdatedTimestamp() } returns System.currentTimeMillis()
        coEvery { dao.clearAndInsertAll(any()) } just Runs

        val result = repo.getRates(forceRefresh = false).drop(1).first()

        assertTrue(result is Resource.Success)
        assertEquals("USD", (result as Resource.Success).data.first().currencyCode)
    }

    @Test
    fun fetchesFromApi_whenForceRefreshTrue() = runTest {

        val timestamp = System.currentTimeMillis()
        val cachedRates = listOf(CurrencyRateEntity("USD", 1.1, timestamp))
        coEvery { dao.getCachedOnce() } returns cachedRates
        coEvery { dao.getLastUpdatedTimestamp() } returns timestamp
        coEvery { dao.clearAndInsertAll(any()) } just Runs
        coEvery { api.getLatestRates() } returns mockSuccessResponse()

        val result = repo.getRates(forceRefresh = true).drop(1).first()

        assertTrue(result is Resource.Success)
    }

    @Test
    fun returnsError_whenApiFails() = runTest {
        val oldTimestamp = 0L
        val cachedRates = listOf(CurrencyRateEntity("USD", 1.1, oldTimestamp))

        coEvery { dao.getCachedOnce() } returns cachedRates
        coEvery { dao.getLastUpdatedTimestamp() } returns oldTimestamp
        coEvery { api.getLatestRates() } throws RuntimeException("No internet")

        val result = repo.getRates(forceRefresh = true).last()

        assertTrue(result is Resource.Error)
        assertEquals("No internet", (result as Resource.Error).message)
    }

    @Test
    fun getTimeSeriesRates_success() = runTest {
        val dto = TimeSeriesResponseDto(
            success = true,
            timeseries = true,
            startDate = "2024-05-01",
            endDate = "2024-05-03",
            base = "EUR",
            rates = mapOf(
                "2024-05-01" to mapOf("USD" to 1.1),
                "2024-05-02" to mapOf("USD" to 1.2)
            ),
            error = null
        )

        coEvery { api.getTimeSeriesRates(any(), any(), any()) } returns dto

        val result = repo.getTimeSeriesRates("USD", LocalDate.parse("2024-05-01"), LocalDate.parse("2024-05-03")).drop(1).first()

        assertTrue(result is Resource.Success)
        assertEquals(2, (result as Resource.Success).data.rates.size)
    }


    @Test
    fun getTimeSeriesRates_apiError() = runTest {
        val dto = TimeSeriesResponseDto(
            success = false,
            timeseries = false,
            startDate = "2024-05-01",
            endDate = "2024-05-03",
            base = "EUR",
            rates = emptyMap(),
            error = FixerErrorDto(101, "Invalid API key")
        )

        coEvery { api.getTimeSeriesRates(any(), any(), any()) } returns dto

        val result = repo.getTimeSeriesRates("USD", LocalDate.parse("2024-05-01"), LocalDate.parse("2024-05-03")).drop(1).first()

        assertTrue(result is Resource.Error)
        assertEquals("Invalid API key", (result as Resource.Error).message)
    }

    @Test
    fun getTimeSeriesRates_exceptionThrown() = runTest {
        coEvery { api.getTimeSeriesRates(any(), any(), any()) } throws RuntimeException("Server down")

        val result = repo.getTimeSeriesRates("USD", LocalDate.parse("2024-05-01"), LocalDate.parse("2024-05-03")).drop(1).first()

        assertTrue(result is Resource.Error)
        assertEquals("Server down", (result as Resource.Error).message)
    }


    @Test
    fun returnsSuccess_whenTimeSeriesApiSucceeds() = runTest {
        coEvery { api.getTimeSeriesRates(any(), any(), any()) } returns TimeSeriesResponseDto(
            success = true,
            timeseries = true,
            startDate = "2024-01-01",
            endDate = "2024-01-02",
            base = "EUR",
            rates = mapOf(
                "2024-01-01" to mapOf("USD" to 1.1),
                "2024-01-02" to mapOf("USD" to 1.2)
            )
        )

        val result = repo.getTimeSeriesRates("USD", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02")).last()

        assertTrue(result is Resource.Success)
        assertEquals(2, (result as Resource.Success).data.rates.size)
    }


    @Test
    fun returnsError_whenTimeSeriesApiFails() = runTest {
        coEvery { api.getTimeSeriesRates(any(), any(), any()) } returns TimeSeriesResponseDto(
            success = false,
            timeseries = false,
            startDate = "2024-01-01",
            endDate = "2024-01-02",
            base = "EUR",
            rates = emptyMap(),
            error = FixerErrorDto(500,"invalid_key")
        )

        val result = repo.getTimeSeriesRates("USD", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02")).last()

        assertTrue(result is Resource.Error)
        assertEquals("invalid_key", (result as Resource.Error).message)
    }

    @Test
    fun returnsError_whenTimeSeriesApiThrows() = runTest {
        coEvery { api.getTimeSeriesRates(any(), any(), any()) } throws RuntimeException("Network down")

        val result = repo.getTimeSeriesRates("USD", LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02")).last()

        assertTrue(result is Resource.Error)
        assertEquals("Network down", (result as Resource.Error).message)
    }


    @Test
    fun fetchesFromApi_whenCacheIsEmptyAndLastUpdateIsNull() = runTest {
        coEvery { dao.getCachedOnce() } returns emptyList()
        coEvery { dao.getLastUpdatedTimestamp() } returns null
        coEvery { dao.clearAndInsertAll(any()) } just Runs
        coEvery { api.getLatestRates() } returns mockSuccessResponse()

        val result = repo.getRates(forceRefresh = false).drop(1).first()

        assertTrue(result is Resource.Success)
    }

    @Test
    fun returnsApiError_whenApiRespondsWithSuccessFalse() = runTest {

        val cachedRates = emptyList<CurrencyRateEntity>()
        coEvery { dao.getCachedOnce() } returns cachedRates
        coEvery { dao.getLastUpdatedTimestamp() } returns null
        coEvery { api.getLatestRates() } returns FixerRatesResponseDto(
            success = false,
            timestamp = 123456L,
            base = "EUR",
            date = "2024-05-01",
            rates = mapOf(), // `rates` є, але success = false
            error = FixerErrorDto(code = 106, type = "invalid_access_key")
        )


        val result = repo.getRates(forceRefresh = true).last()


        assertTrue(result is Resource.Error)
        assertEquals("invalid_access_key", (result as Resource.Error).message)
        assertEquals(ApiErrorType.UnexpectedError, result.errorType)
    }




    private fun mockSuccessResponse(): FixerRatesResponseDto {
        return FixerRatesResponseDto(
            success = true,
            timestamp = 123456L,
            base = "EUR",
            date = "2024-05-01",
            rates = mapOf("USD" to 1.1),
            error = null
        )
    }

}

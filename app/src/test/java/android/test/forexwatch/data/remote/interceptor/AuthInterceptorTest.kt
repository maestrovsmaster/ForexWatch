package android.test.forexwatch.data.remote.interceptor

import android.test.forexwatch.core.logging.Logger
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
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

@OptIn(ExperimentalCoroutinesApi::class)
class AuthInterceptorTest {

    private lateinit var interceptor: AuthInterceptor
    private val logger: Logger = mockk(relaxed = true)
    private val apiKey = "test_key"

    @Before
    fun setup() {
        interceptor = AuthInterceptor(apiKey, logger)
    }

    @Test
    fun `adds access_key query parameter to request url`() {
        val originalUrl = "https://example.com/data"
        val originalRequest = Request.Builder().url(originalUrl).build()

        val chain = mockk<Interceptor.Chain>()
        val response = mockk<Response>(relaxed = true)

        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } answers {
            val request = firstArg<Request>()
            assertTrue(request.url.queryParameter("access_key") == apiKey)
            response
        }

        interceptor.intercept(chain)
    }

    @Test
    fun `logs request and response in debug mode`() {
        val originalUrl = "https://example.com/test"
        val originalRequest = Request.Builder().url(originalUrl).build()

        val responseBody = "Mock body"
        val response = mockk<Response> {
            every { code } returns 200
            every { peekBody(any()) } returns ResponseBody.create(null, responseBody)
        }

        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } returns response

        interceptor.intercept(chain)

        verify { logger.d(any(), match { it.contains("Request URL") }) }
        verify { logger.d(any(), match { it.contains("Response status") }) }
        verify { logger.d(any(), match { it.contains("Response body") }) }
    }
}

package android.test.forexwatch.core.logging

import android.test.forexwatch.BuildConfig
import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppLoggerTest {

    private lateinit var logger: AppLogger
    private lateinit var buildConfigProvider: BuildConfigProvider

    @Before
    fun setup() {
        mockkStatic(Log::class)
        buildConfigProvider = mockk()
        logger = AppLogger(buildConfigProvider)
    }

    @Test
    fun `d does not log when BuildConfig DEBUG is false`() {
        every { buildConfigProvider.isDebug } returns false

        logger.d("TAG", "message")

        verify(exactly = 0) { Log.d(any(), any()) }
    }

    @Test
    fun `d logs when BuildConfig DEBUG is true`() {
        every { buildConfigProvider.isDebug } returns true
        every { Log.d(any(), any()) } returns 0

        logger.d("TAG", "message")

        verify { Log.d("TAG", "message") }
    }

    @Test
    fun `w logs even if BuildConfig DEBUG is false`() {
        every { Log.w(any(), any(), any()) } returns 0

        logger.w("TAG", "warn", null)

        verify { Log.w("TAG", "warn", null) }
    }

    @Test
    fun `e logs error`() {
        val t = Throwable("fail")
        every { Log.e(any(), any(), any()) } returns 0

        logger.e("TAG", "err", t)

        verify { Log.e("TAG", "err", t) }
    }

    @Test
    fun `i does not log when BuildConfig DEBUG is false`() {
        every { buildConfigProvider.isDebug } returns false

        logger.i("TAG", "message")

        verify(exactly = 0) { Log.i(any(), any()) }
    }

    @Test
    fun `i logs when BuildConfig DEBUG is true`() {
        every { buildConfigProvider.isDebug } returns true
        every { Log.i(any(), any()) } returns 0

        logger.i("TAG", "message")

        verify { Log.i("TAG", "message") }
    }

}

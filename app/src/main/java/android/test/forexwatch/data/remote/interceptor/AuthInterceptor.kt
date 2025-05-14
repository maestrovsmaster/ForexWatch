package android.test.forexwatch.data.remote.interceptor
import android.test.forexwatch.BuildConfig
import android.test.forexwatch.core.logging.LogTags
import android.test.forexwatch.core.logging.Logger
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(
    @Named("apiKey") private val apiKey: String,
    private val logger: Logger
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val url = originalRequest.url.newBuilder()
            .addQueryParameter("access_key", apiKey)
            .build()

        val newRequest = originalRequest.newBuilder().url(url).build()

        if (BuildConfig.DEBUG) {
            logger.d(LogTags.AUTH_INTERCEPTOR, "Request URL: $url")
        }

        val response = chain.proceed(newRequest)

        if (BuildConfig.DEBUG) {
            logger.d(LogTags.AUTH_INTERCEPTOR, "Response status: ${response.code}")

            //For debugging purposes only, uncomment carefully:
            val responseBody = response.peekBody(1024 * 10).string()
            logger.d(LogTags.AUTH_INTERCEPTOR, "Response body: $responseBody")
        }

        return response
    }
}

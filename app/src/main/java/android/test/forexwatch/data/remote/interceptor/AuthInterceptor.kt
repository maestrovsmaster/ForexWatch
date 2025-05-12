package android.test.forexwatch.data.remote.interceptor
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class AuthInterceptor @Inject constructor(
    @Named("apiKey") private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("access_key", apiKey)
            .build()

        val newRequest = chain.request().newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}

package android.test.forexwatch.data.remote.api

import android.test.forexwatch.data.remote.dto.FixerRatesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApiService {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String = "EUR"
    ): FixerRatesResponseDto
}

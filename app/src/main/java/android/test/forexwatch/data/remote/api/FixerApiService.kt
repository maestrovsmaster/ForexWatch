package android.test.forexwatch.data.remote.api

import android.test.forexwatch.data.remote.dto.FixerRatesResponseDto
import android.test.forexwatch.data.remote.dto.TimeSeriesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApiService {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String = "EUR"
    ): FixerRatesResponseDto

    @GET("timeseries")
    suspend fun getTimeSeriesRates(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("symbols") symbols: String
    ): TimeSeriesResponseDto
}

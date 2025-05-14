package android.test.forexwatch.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TimeSeriesResponseDto(
    val success: Boolean,
    val timeseries: Boolean,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    val base: String,
    val rates: Map<String, Map<String, Double>>,
    @SerializedName("error") val error: FixerErrorDto? = null
)
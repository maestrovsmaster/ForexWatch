package android.test.forexwatch.data.remote.dto

import com.google.gson.annotations.SerializedName


data class FixerRatesResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, Double>,
    @SerializedName("error") val error: FixerErrorDto? = null

)

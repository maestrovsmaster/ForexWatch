package android.test.forexwatch.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FixerErrorDto(
    @SerializedName("code") val code: Int,
    @SerializedName("type") val type: String
)
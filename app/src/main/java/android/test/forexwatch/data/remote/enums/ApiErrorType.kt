package android.test.forexwatch.data.remote.enums

sealed class ApiErrorType(val rawValue: String) {
    object UsageLimitReached : ApiErrorType("usage_limit_reached")
    object UnexpectedError : ApiErrorType("unexpected_error")
    object NetworkError : ApiErrorType("")

    companion object {
        fun fromString(type: String?): ApiErrorType {
            return when (type?.lowercase()) {
                UsageLimitReached.rawValue -> UsageLimitReached
                NetworkError.rawValue -> NetworkError
                else -> UnexpectedError
            }
        }
    }
}

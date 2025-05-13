package android.test.forexwatch.core.utils

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T, val isStale: Boolean = false) : Resource<T>()
    data class Error<T>(
        val message: String,
        val data: T? = null,
        val isNetworkError: Boolean = false
    ) : Resource<T>()
}



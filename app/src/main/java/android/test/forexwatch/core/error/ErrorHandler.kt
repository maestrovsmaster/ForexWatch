package android.test.forexwatch.core.error


import android.test.forexwatch.core.logging.Logger
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val logger: Logger
) {
    fun handle(tag: String, message: String, throwable: Throwable) {
        logger.e(tag, message, throwable)
        // TODO: Add Crashlytics/etc.
    }
}

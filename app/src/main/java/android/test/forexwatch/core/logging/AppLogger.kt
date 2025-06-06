package android.test.forexwatch.core.logging

import android.util.Log
import javax.inject.Inject

class AppLogger @Inject constructor(
    private val buildConfigProvider: BuildConfigProvider
) : Logger {

    override fun d(tag: String, message: String) {

        if (buildConfigProvider.isDebug) Log.d(tag, message)

    }

    override fun i(tag: String, message: String) {

        if (buildConfigProvider.isDebug) Log.i(tag, message)

    }

    override fun w(tag: String, message: String, throwable: Throwable?) {

        Log.w(tag, message, throwable)

    }

    override fun e(tag: String, message: String, throwable: Throwable?) {

        Log.e(tag, message, throwable)

    }
}

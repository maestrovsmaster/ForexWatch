package android.test.forexwatch.core.logging

interface Logger {

    fun d(tag: String, message: String)

    fun i(tag: String, message: String)

    fun w(tag: String, message: String, throwable: Throwable? = null)

    fun e(tag: String, message: String, throwable: Throwable? = null)

}
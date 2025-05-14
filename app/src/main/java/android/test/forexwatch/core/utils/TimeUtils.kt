package android.test.forexwatch.core.utils

fun isStale(timestampMillis: Long, thresholdMinutes: Long): Boolean {

    val now = System.currentTimeMillis()
    return (now - timestampMillis) > thresholdMinutes * 60 * 1000

}

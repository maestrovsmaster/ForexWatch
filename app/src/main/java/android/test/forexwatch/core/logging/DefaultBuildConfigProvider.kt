package android.test.forexwatch.core.logging

import android.test.forexwatch.BuildConfig

interface BuildConfigProvider {

    val isDebug: Boolean
}

class DefaultBuildConfigProvider : BuildConfigProvider {

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

}

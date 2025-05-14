package android.test.forexwatch.fake

import android.test.forexwatch.core.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeConnectivityObserver : ConnectivityObserver {

    override fun isConnected(): Boolean {

        return true
    }

    override fun observe(): Flow<Boolean> {

        return flowOf(true)
    }

}
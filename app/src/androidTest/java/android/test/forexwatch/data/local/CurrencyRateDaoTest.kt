package android.test.forexwatch.data.local


import android.content.Context
import android.test.forexwatch.data.local.dao.CurrencyRateDao
import android.test.forexwatch.data.local.entity.CurrencyRateEntity
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRateDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: CurrencyRateDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.currencyRateDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAll_and_getAllRates() = runTest {
        val list = listOf(
            CurrencyRateEntity("USD", 40.0, 123456L),
            CurrencyRateEntity("EUR", 42.0, 123457L)
        )
        dao.insertAll(list)

        val result = dao.getAllRates().first()
        assertEquals(2, result.size)
        assertTrue(result.any { it.currencyCode == "USD" })
        assertTrue(result.any { it.currencyCode == "EUR" })
    }

    @Test
    fun clear_removesAllRates() = runTest {
        dao.insertAll(listOf(CurrencyRateEntity("GBP", 45.0, 123L)))
        dao.clear()

        val result = dao.getAllRates().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getLastUpdatedTimestamp_returnsCorrectValue() = runTest {
        dao.insertAll(
            listOf(
                CurrencyRateEntity("USD", 40.0, 1000L),
                CurrencyRateEntity("EUR", 42.0, 2000L)
            )
        )

        val timestamp = dao.getLastUpdatedTimestamp()
        assertEquals(2000L, timestamp)
    }

    @Test
    fun clearAndInsertAll_replacesDataCorrectly() = runTest {
        dao.insertAll(listOf(CurrencyRateEntity("JPY", 10.0, 500L)))

        val newRates = listOf(
            CurrencyRateEntity("CHF", 38.0, 600L),
            CurrencyRateEntity("AUD", 30.0, 700L)
        )
        dao.clearAndInsertAll(newRates)

        val result = dao.getAllRates().first()
        assertEquals(2, result.size)
        assertTrue(result.any { it.currencyCode == "CHF" })
        assertFalse(result.any { it.currencyCode == "JPY" })
    }
}

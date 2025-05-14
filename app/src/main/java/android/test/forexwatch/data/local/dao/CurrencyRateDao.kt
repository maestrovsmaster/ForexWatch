package android.test.forexwatch.data.local.dao

import android.test.forexwatch.data.local.entity.CurrencyRateEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao {

    @Query("SELECT * FROM currency_rates")
    fun getAllRates(): Flow<List<CurrencyRateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<CurrencyRateEntity>)

    @Query("DELETE FROM currency_rates")
    suspend fun clear()

    @Query("SELECT updatedAt FROM currency_rates ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLastUpdatedTimestamp(): Long?


    @Transaction
    suspend fun clearAndInsertAll(rates: List<CurrencyRateEntity>) {

        clearAll()
        insertAll(rates)

    }

    @Query("DELETE FROM currency_rates")
    suspend fun clearAll()


    @Query("SELECT * FROM currency_rates")
    suspend fun getCachedOnce(): List<CurrencyRateEntity>


}
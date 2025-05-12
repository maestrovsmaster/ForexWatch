package android.test.forexwatch.data.local

import android.test.forexwatch.data.local.dao.CurrencyRateDao
import android.test.forexwatch.data.local.entity.CurrencyRateEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CurrencyRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyRateDao(): CurrencyRateDao
}

package com.savings.savinggoals.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.savings.savinggoals.database.entity.CurrencyEntity

@Dao
interface CurrencyDao {
    @Insert
    fun insertCurrency(vararg currencyEntity: CurrencyEntity)

    @Query("SELECT * FROM currency")
    fun loadCurrency(): List<CurrencyEntity>

    @Query("SELECT COUNT(id) FROM currency")
    fun countCurrency(): Int

    @Query("DELETE FROM currency")
    fun deleteCurrency()
}

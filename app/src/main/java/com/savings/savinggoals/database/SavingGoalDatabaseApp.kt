package com.savings.savinggoals.database

import android.app.Application
import android.content.Context
import com.savings.savinggoals.database.dao.CurrencyDao
import com.savings.savinggoals.database.dao.GoalDao

class SavingGoalDatabaseApp : Application() {

    private lateinit var currencyDao: CurrencyDao
    private lateinit var goalDao: GoalDao

    private lateinit var instance: SavingGoalDatabaseApp

    override fun onCreate() {
        super.onCreate()
        instance = this
        currencyDao = SavingGoalDatabase.getDatabase(this).currencyDao()
        goalDao = SavingGoalDatabase.getDatabase(this).goalDao()
    }

    @Synchronized
    fun getCurrencyDao(context: Context): CurrencyDao {
        return SavingGoalDatabase.getDatabase(context.applicationContext).currencyDao()
    }

    @Synchronized
    fun getGoalDao(context: Context): GoalDao {
        return SavingGoalDatabase.getDatabase(context.applicationContext).goalDao()
    }
}

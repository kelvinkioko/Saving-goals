package com.savings.savinggoals.database.repository

import android.app.Application
import com.savings.savinggoals.database.SavingGoalDatabase
import com.savings.savinggoals.database.entity.CurrencyEntity

class CurrencyRepository(application: Application) {

    private val currencyDao = SavingGoalDatabase.getDatabase(application).currencyDao()

    fun insertCurrency(currency: CurrencyEntity) {
        currencyDao.insertCurrency(currency)
    }

    fun loadCurrency(): List<CurrencyEntity> {
        return currencyDao.loadCurrency()
    }

    fun countCurrency(): Int {
        return currencyDao.countCurrency()
    }

    fun deleteCurrency() {
        currencyDao.deleteCurrency()
    }
}

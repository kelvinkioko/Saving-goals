package com.savings.savinggoals.database.repository

import android.app.Application
import com.savings.savinggoals.database.SavingGoalDatabase
import com.savings.savinggoals.database.entity.GoalEntity

class GoalRepository(application: Application) {

    private val goalDao = SavingGoalDatabase.getDatabase(application).goalDao()

    fun insertGoal(goalEntity: GoalEntity) {
        goalDao.insertGoal(goalEntity)
    }

    fun loadGoal(): List<GoalEntity> {
        return goalDao.loadGoal()
    }

    fun countGoal(): Int {
        return goalDao.countGoal()
    }

    fun updateGoalAmount(goalID: String, amount: String) {
        goalDao.updateGoalAmount(goalID = goalID, amount = amount)
    }

    fun deleteGoalByID(goalID: String) {
        goalDao.deleteGoalByID(goalID = goalID)
    }

    fun deleteGoal() {
        goalDao.deleteGoal()
    }
}

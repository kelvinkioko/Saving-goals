package com.savings.savinggoals.database.repository

import android.app.Application
import com.savings.savinggoals.database.SavingGoalDatabase
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.database.entity.GoalSavingEntity

class GoalRepository(application: Application) {

    private val goalDao = SavingGoalDatabase.getDatabase(application).goalDao()
    private val goalSavingDao = SavingGoalDatabase.getDatabase(application).goalSavingDao()

    fun insertGoal(goalEntity: GoalEntity) {
        goalDao.insertGoal(goalEntity)
    }

    fun loadGoals(): List<GoalEntity> {
        return goalDao.loadGoal()
    }

    fun loadGoalByID(goalID: String): GoalEntity {
        return goalDao.loadGoalByID(goalID = goalID)
    }

    fun countGoal(): Int {
        return goalDao.countGoal()
    }

    fun updateGoalAmount(goalID: String, amount: String) {
        goalDao.updateGoalAmount(goalID = goalID, amount = amount)
    }

    fun updateTargetAmount(goalID: String, target_amount: String) {
        goalDao.updateTargetAmount(goalID = goalID, target_amount = target_amount)
    }

    fun deleteGoalByID(goalID: String) {
        goalDao.deleteGoalByID(goalID = goalID)
    }

    fun deleteGoal() {
        goalDao.deleteGoal()
    }

    /**
     * Goal Saving functions
     */
    fun insertGoalSaving(goalSavingEntity: GoalSavingEntity) {
        goalSavingDao.insertGoalSaving(goalSavingEntity)
    }

    fun loadGoalSavingByGoalID(goalID: String): List<GoalSavingEntity> {
        return goalSavingDao.loadGoalSavingByGoalID(goalID = goalID)
    }

    fun countGoalSaving(): Int {
        return goalSavingDao.countGoalSaving()
    }

    fun updateGoalSaving(savingID: String, save_status: String) {
        goalSavingDao.updateGoalSaving(savingID = savingID, save_status = save_status)
    }

    fun deleteGoalSaving() {
        goalSavingDao.deleteGoalSaving()
    }
}

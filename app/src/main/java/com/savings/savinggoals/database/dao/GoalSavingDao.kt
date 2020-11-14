package com.savings.savinggoals.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.savings.savinggoals.database.entity.GoalSavingEntity

@Dao
interface GoalSavingDao {
    @Insert
    fun insertGoalSaving(vararg goalSavingEntity: GoalSavingEntity)

    @Query("SELECT * FROM goal_saving WHERE goalID = :goalID")
    fun loadGoalSavingByGoalID(goalID: String): List<GoalSavingEntity>

    @Query("SELECT COUNT(id) FROM goal_saving")
    fun countGoalSaving(): Int

    @Query("DELETE FROM goal_saving")
    fun deleteGoalSaving()
}

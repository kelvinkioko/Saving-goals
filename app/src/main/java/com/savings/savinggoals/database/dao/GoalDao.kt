package com.savings.savinggoals.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.savings.savinggoals.database.entity.GoalEntity

@Dao
interface GoalDao {
    @Insert
    fun insertGoal(vararg goalEntity: GoalEntity)

    @Query("SELECT * FROM goal ORDER BY goal_date ASC")
    fun loadGoal(): List<GoalEntity>

    @Query("SELECT COUNT(id) FROM goal")
    fun countGoal(): Int

    @Query("UPDATE goal SET amount =:amount WHERE goalID = :goalID")
    fun updateGoalAmount(
        goalID: String,
        amount: String
    )

    @Query("DELETE FROM goal WHERE goalID = :goalID")
    fun deleteGoalByID(
        goalID: String
    )

    @Query("DELETE FROM goal")
    fun deleteGoal()
}

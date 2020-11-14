package com.savings.savinggoals.ui.goal.saving

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.savings.savinggoals.constants.Hive
import com.savings.savinggoals.constants.getCurrentDateAsDate
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.database.entity.GoalSavingEntity
import com.savings.savinggoals.database.repository.GoalRepository

class SaveEntryViewModel(private val goalRepository: GoalRepository) : ViewModel() {
    private val _uiState = MutableLiveData<SaveEntryUIState>()
    val uiState: LiveData<SaveEntryUIState> = _uiState

    private lateinit var goalEntity: GoalEntity

    fun saveGoal(goalID: String, amount: String, note: String, type: String, transactionDate: String) {
        goalEntity = goalRepository.loadGoalByID(goalID = goalID)

        if (this::goalEntity.isInitialized) {
            val amountUpdate = if (type.equals("Saving", ignoreCase = true)) {
                goalEntity.amount.toFloat() + amount.toFloat()
            } else {
                goalEntity.amount.toFloat() - amount.toFloat()
            }
            goalRepository.updateGoalAmount(goalID = goalID, amount = amountUpdate.toString())
        }

        val goalSavingEntity = GoalSavingEntity(
            id = 0,
            savingID = "GSE-${Hive().getTimestamp()}",
            goalID = goalID,
            amount = amount,
            note = note,
            save_type = type,
            save_status = "Complete", // Can either be pending or Complete
            goal_date = getCurrentDateAsDate(),
            createdAt = transactionDate
        )
        goalRepository.insertGoalSaving(goalSavingEntity = goalSavingEntity)

        _uiState.postValue(SaveEntryUIState.Success)
    }
}

sealed class SaveEntryUIState {
    object Success : SaveEntryUIState()
}

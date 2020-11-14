package com.savings.savinggoals.ui.goal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.database.entity.GoalSavingEntity
import com.savings.savinggoals.database.repository.GoalRepository
import com.savings.savinggoals.ui.goal.saving.SavingEntryBottomSheet
import com.savings.savinggoals.util.Event
import com.savings.savinggoals.util.asEvent

class GoalViewModel(private val goalRepository: GoalRepository) : ViewModel() {
    private val _uiState = MutableLiveData<GoalUIState>()
    val uiState: LiveData<GoalUIState> = _uiState

    private val _action = MutableLiveData<Event<GoalActions>>()
    val action: LiveData<Event<GoalActions>> = _action

    private lateinit var goalEntity: GoalEntity

    fun loadGoal(goalID: String) {
        goalEntity = goalRepository.loadGoalByID(goalID = goalID)
        val goalSavingEntity = goalRepository.loadGoalSavingByGoalID(goalID = goalID)
        _uiState.postValue(GoalUIState.DisplayGoal(goalEntity = goalEntity, goalSavingEntity = goalSavingEntity))
    }

    fun manageGoalSavingClick(goalSaving: GoalSavingEntity) {
        if (this::goalEntity.isInitialized) {
            val amountUpdate = goalEntity.amount.toFloat() + goalSaving.amount.toFloat()
            goalRepository.updateGoalAmount(goalID = goalEntity.goalID, amount = amountUpdate.toString())
        }

        goalRepository.updateGoalSaving(savingID = goalSaving.savingID, save_status = "Complete")
        loadGoal(goalID = goalEntity.goalID)
    }

    fun addTransaction() {
        val bottomSheetFragment = SavingEntryBottomSheet(
            goalID = goalEntity.goalID,
            currency = goalEntity.currency,
            resendGoalTypeCallback = {
                loadGoal(goalID = goalEntity.goalID)
            }
        )
        _action.postValue(GoalActions.BottomNavigate(bottomSheetFragment).asEvent())
    }
}

sealed class GoalActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : GoalActions()

    data class Navigate(val destination: NavDirections) : GoalActions()
}

sealed class GoalUIState {
    data class DisplayGoal(val goalEntity: GoalEntity, val goalSavingEntity: List<GoalSavingEntity>) : GoalUIState()
}

package com.savings.savinggoals.ui.goalmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savings.savinggoals.constants.Hive
import com.savings.savinggoals.constants.formatDateHeader
import com.savings.savinggoals.constants.getCurrentDateAsDate
import com.savings.savinggoals.constants.getCurrentDateAsString
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.database.entity.GoalTypeEntity
import com.savings.savinggoals.database.repository.GoalRepository
import com.savings.savinggoals.ui.goalmanager.type.GoalTypeBottomSheet
import com.savings.savinggoals.util.Event
import com.savings.savinggoals.util.asEvent

class AddGoalViewModel(private val goalRepository: GoalRepository) : ViewModel() {
    private val _uiState = MutableLiveData<AddGoalUIState>()
    val uiState: LiveData<AddGoalUIState> = _uiState

    private val _action = MutableLiveData<Event<AddGoalActions>>()
    val action: LiveData<Event<AddGoalActions>> = _action

    private lateinit var goalTypeEntity: GoalTypeEntity
    private lateinit var startDate: String
    private lateinit var endDate: String

    init {
        setupDefaultGoalType()
    }

    private fun setupDefaultGoalType() {
        goalTypeEntity = GoalTypeEntity(
            goalTypeID = "GT52",
            goalType = "52 Week saving",
            description = "This is a 52 week saving goal where you save weekly and the amount to save increases each week by a certain set amount."
        )
        startDate = getCurrentDateAsString()
        endDate = getCurrentDateAsString()

        _uiState.postValue(AddGoalUIState.PageSetup(goalTypeEntity = goalTypeEntity, startDate = formatDateHeader(startDate), endDate = formatDateHeader(endDate)))
    }

    fun setupEndDate(endDate: String) {
        this.endDate = endDate
    }

    fun setupStartDate(startDate: String) {
        this.startDate = startDate
    }

    fun chooseGoalType() {
        val bottomSheetFragment = GoalTypeBottomSheet(
            goalTypeID = goalTypeEntity.goalTypeID,
            resendGoalTypeCallback = {
                this.goalTypeEntity = it
                _uiState.postValue(AddGoalUIState.GoalType(it))
            }
        )
        _action.postValue(AddGoalActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun saveGoal(goalName: String, goalDescription: String, targetAmount: String, incrementalAmount: String, currency: String) {
        val goal = GoalEntity(
            id = 0,
            goalID = "GOAL-${Hive().getTimestamp()}",
            name = goalName,
            description = goalDescription,
            currency = currency,
            amount = "0",
            target_amount = targetAmount,
            increment_amount = incrementalAmount,
            start_date = startDate,
            target_date = endDate,
            type = goalTypeEntity.goalTypeID,
            image = "",
            goal_date = getCurrentDateAsDate(),
            createdAt = getCurrentDateAsString()
        )
        goalRepository.insertGoal(goal)
        _uiState.postValue(AddGoalUIState.Success)
    }
}

sealed class AddGoalActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : AddGoalActions()

    data class Navigate(val destination: NavDirections) : AddGoalActions()
}

sealed class AddGoalUIState {
    object Success : AddGoalUIState()

    data class PageSetup(val goalTypeEntity: GoalTypeEntity, val startDate: String, val endDate: String) : AddGoalUIState()

    data class DisplayGoals(val goalsEntity: List<GoalEntity>) : AddGoalUIState()

    data class GoalType(val goalTypeEntity: GoalTypeEntity) : AddGoalUIState()
}

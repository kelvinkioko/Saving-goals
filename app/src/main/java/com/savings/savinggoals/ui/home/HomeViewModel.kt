package com.savings.savinggoals.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.util.Event
import com.savings.savinggoals.util.asEvent

class HomeViewModel : ViewModel() {
    private val _uiState = MutableLiveData<HomeUIState>()
    val uiState: LiveData<HomeUIState> = _uiState

    private val _action = MutableLiveData<Event<HomeActions>>()
    val action: LiveData<Event<HomeActions>> = _action

    fun addGoal() {
        _action.postValue(HomeActions.Navigate(HomeFragmentDirections.toAddGoalFragment()).asEvent())
    }
}

sealed class HomeActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : HomeActions()

    data class Navigate(val destination: NavDirections) : HomeActions()
}

sealed class HomeUIState {
    data class DisplayGoals(val goalsEntity: List<GoalEntity>) : HomeUIState()
}

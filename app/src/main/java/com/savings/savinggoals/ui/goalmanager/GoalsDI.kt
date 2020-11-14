package com.savings.savinggoals.ui.goalmanager

import com.savings.savinggoals.ui.goal.GoalViewModel
import com.savings.savinggoals.ui.goal.saving.SaveEntryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val goalsModule = module {
    viewModel { GoalViewModel(get()) }
    viewModel { AddGoalViewModel(get()) }
    viewModel { SaveEntryViewModel(get()) }
}

package com.savings.savinggoals.ui.goalmanager

import com.savings.savinggoals.ui.goal.GoalViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val goalsModule = module {
    viewModel { GoalViewModel() }
    viewModel { AddGoalViewModel(get()) }
}

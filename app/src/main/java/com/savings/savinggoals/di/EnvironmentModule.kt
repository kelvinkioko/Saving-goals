package com.savings.savinggoals.di

import com.savings.savinggoals.constants.setup.DefaultViewModel
import com.savings.savinggoals.database.repository.CurrencyRepository
import com.savings.savinggoals.database.repository.GoalRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val environmentModule = module {
    single { CurrencyRepository(get()) }
    single { GoalRepository(get()) }

    viewModel { DefaultViewModel(get()) }
}

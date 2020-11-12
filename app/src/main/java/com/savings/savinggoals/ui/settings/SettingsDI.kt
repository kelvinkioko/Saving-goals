package com.savings.savinggoals.ui.settings

import com.savings.savinggoals.ui.settings.currency.CurrencyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { SettingsViewModel() }
    viewModel { CurrencyViewModel(get()) }
}

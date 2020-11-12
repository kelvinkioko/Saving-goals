package com.savings.savinggoals.ui.settings.currency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.savings.savinggoals.database.repository.CurrencyRepository

class CurrencyViewModel(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<CurrencyUIState>()
    val uiState: LiveData<CurrencyUIState> = _uiState

    init {
        loadCurrency()
        println("Currency called ${currencyRepository.countCurrency()}")
    }

    private fun loadCurrency() {
        val currencies = currencyRepository.loadCurrency()

        val sectionedCurrencyItem = mutableListOf<SectionedCurrencyItem>()
        var previousRegion = ""
        currencies.forEach {
            val currentRegion = it.region
            if (previousRegion.isNullOrEmpty() || previousRegion != currentRegion) {
                previousRegion = currentRegion
                sectionedCurrencyItem.add(SectionedCurrencyItem.CurrencyHeader(currentRegion))
            }
            sectionedCurrencyItem.add(
                    SectionedCurrencyItem.CurrencyItems(
                            country = it.country,
                            currency = it.currency,
                            currencyCode = it.currencyCode,
                            currencySymbol = it.currencySymbol,
                            region = it.region
                    ))
        }

        _uiState.postValue(CurrencyUIState.Currencies(sectionedCurrencyItem))
    }
}

sealed class CurrencyUIState {
    object Loading : CurrencyUIState()

    object Success : CurrencyUIState()

    data class Currencies(val currencyEntity: List<SectionedCurrencyItem>) : CurrencyUIState()
}

sealed class SectionedCurrencyItem {
    data class CurrencyItems(
        val country: String,
        val currency: String,
        val currencyCode: String,
        val currencySymbol: String,
        val region: String
    ) : SectionedCurrencyItem()

    data class CurrencyHeader(val title: String) : SectionedCurrencyItem()
}

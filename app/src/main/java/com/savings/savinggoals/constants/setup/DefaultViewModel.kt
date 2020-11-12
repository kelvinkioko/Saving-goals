package com.savings.savinggoals.constants.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.savings.savinggoals.database.entity.CurrencyEntity
import com.savings.savinggoals.database.repository.CurrencyRepository

class DefaultViewModel(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<DefaultUIState>(DefaultUIState.Loading)
    val uiState: LiveData<DefaultUIState> = _uiState

    init {
        setupCurrency()
    }

    private fun setupCurrency() {
        val currency = mutableListOf<CurrencyEntity>()

        currency.add(CurrencyEntity(0, "Bulgaria", "Bulgarian lev", "BGN", "лв", "Europe"))
        currency.add(CurrencyEntity(0, "Switzerland", "Swiss franc", "CHF", "CHF", "Europe"))
        currency.add(CurrencyEntity(0, "Czechia", "Czech koruna", "CZK", "Kč", "Europe"))
        currency.add(CurrencyEntity(0, "Denmark", "Danish krone", "DKK", "kr", "Europe"))
        currency.add(CurrencyEntity(0, "Euro area countries", "Euro", "EUR", "€", "Europe"))
        currency.add(CurrencyEntity(0, "United Kingdom", "Pounds sterling", "GBP", "£", "Europe"))
        currency.add(CurrencyEntity(0, "Croatia", "Croatian Kuna", "HRK", "kn", "Europe"))
        currency.add(CurrencyEntity(0, "Georgia", "Georgian lari", "GEL", "₾", "Europe"))
        currency.add(CurrencyEntity(0, "Hungary", "Hungarian forint", "HUF", "ft", "Europe"))
        currency.add(CurrencyEntity(0, "Norway", "Norwegian krone", "NOK", "kr", "Europe"))
        currency.add(CurrencyEntity(0, "Poland", "Polish zloty", "PLN", "zł", "Europe"))
        currency.add(CurrencyEntity(0, "Russia", "Russian ruble", "RUB", "₽", "Europe"))
        currency.add(CurrencyEntity(0, "Romania", "Romanian leu", "RON", "lei", "Europe"))
        currency.add(CurrencyEntity(0, "Sweden", "Swedish krona", "SEK", "kr", "Europe"))
        currency.add(CurrencyEntity(0, "Turkey", "Turkish lira", "TRY", "₺", "Europe"))
        currency.add(CurrencyEntity(0, "Ukraine", "Ukrainian hryvna", "UAH", "₴", "Europe"))

        currency.add(CurrencyEntity(0, "UAE", "Emirati dirham", "AED", "د.إ", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "Israel", "Israeli shekel", "ILS", "₪", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "Kenya", "Kenyan shilling", "KES", "Ksh", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "Morocco", "Moroccan dirham", "MAD", ".د.م", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "Nigeria", "igerian naira", "NGN", "₦", "Middle East and Africa"))
        currency.add(CurrencyEntity(0, "South Africa", "South african rand**", "ZAR", "R", "Middle East and Africa"))

        currency.add(CurrencyEntity(0, "Brazil", "Brazilian real", "BRL", "R$", "The Americas"))
        currency.add(CurrencyEntity(0, "Canada", "Canadian dollars", "CAD", "$", "The Americas"))
        currency.add(CurrencyEntity(0, "Chile", "Chilean peso", "CLP", "$", "The Americas"))
        currency.add(CurrencyEntity(0, "Colombia", "Colombian peso", "COP", "$", "The Americas"))
        currency.add(CurrencyEntity(0, "Mexico", "Mexican peso", "MXN", "$", "The Americas"))
        currency.add(CurrencyEntity(0, "Peru", "Peruvian sol", "PEN", "S/.", "The Americas"))
        currency.add(CurrencyEntity(0, "USA", "US dollar", "USD", "$", "The Americas"))

        currency.add(CurrencyEntity(0, "Australia", "Australian dollars", "AUD", "$", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Bangladesh", "Bangladeshi taka", "BDT", "৳", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "China", "Chinese yuan", "CNY", "¥", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Hong Kong", "Hong Kong dollar", "HKD", "$", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Indonesia", "Indonesian rupiah", "IDR", "Rp", "Asia & The Pacific region"))

        currency.add(CurrencyEntity(0, "India", "Indian rupee", "INR", "₹", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Japan", "Japanese yen", "JPY", "¥", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Malaysia", "Malaysian ringgit", "MYR", "RM", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "New Zealand", "New Zealand dollar", "NZD", "$", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Philippines", "Philippine peso", "PHP", "₱", "Asia & The Pacific region"))

        currency.add(CurrencyEntity(0, "Pakistan", "Pakistani rupee", "PKR", "Rs", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Singapore", "Singapore dollar", "SGD", "$", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "South Korea", "South Korean won", "KRW", "₩", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Sri Lanka", "Sri Lankan rupee", "LKR", "Rs", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Thailand", "Thai baht", "THB", "฿", "Asia & The Pacific region"))
        currency.add(CurrencyEntity(0, "Vietnam", "Vietnamese dong", "VND", "₫", "Asia & The Pacific region"))

        if (currencyRepository.countCurrency() == 0) {
            currencyRepository.deleteCurrency()
            for (singleCurrency in currency) {
                currencyRepository.insertCurrency(singleCurrency)
            }
        }
    }
}

sealed class DefaultUIState {
    object Loading : DefaultUIState()

    object Success : DefaultUIState()
}

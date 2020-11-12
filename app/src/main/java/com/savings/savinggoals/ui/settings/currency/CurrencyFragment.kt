package com.savings.savinggoals.ui.settings.currency

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.savings.savinggoals.R
import com.savings.savinggoals.databinding.SettingsCurrencyFragmentBinding
import com.savings.savinggoals.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyFragment : Fragment(R.layout.settings_currency_fragment) {

    private val binding by viewBinding(SettingsCurrencyFragmentBinding::bind)

    private val viewModel: CurrencyViewModel by viewModel()

    private val currencyAdapter: CurrencyAdapter by lazy {
        CurrencyAdapter { onCurrencyTypePicked(it) }
    }

    private fun onCurrencyTypePicked(currencyItems: SectionedCurrencyItem.CurrencyItems) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupAccountTypesList()
        setupViewObservers()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupViewObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is CurrencyUIState.Currencies -> populateAccountTypes(it.currencyEntity)
            }
        }
    }

    private fun populateAccountTypes(sectionedCurrencyItem: List<SectionedCurrencyItem>) {
        currencyAdapter.submitList(sectionedCurrencyItem)
    }

    private fun setupAccountTypesList() {
        binding.currenciesList.adapter = currencyAdapter
    }
}

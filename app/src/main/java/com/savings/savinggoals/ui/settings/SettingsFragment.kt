package com.savings.savinggoals.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.savings.savinggoals.R
import com.savings.savinggoals.databinding.SettingsFragmentBinding
import com.savings.savinggoals.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private val binding by viewBinding(SettingsFragmentBinding::bind)

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupClickListeners()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            currencyTypes.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toCurrencyFragment())
            }
            tellAFriend.setOnClickListener {
                viewModel.shareApp(requireActivity())
            }
            ratingReview.setOnClickListener {
                viewModel.rateApp(requireActivity())
            }
        }
    }
}

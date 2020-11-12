package com.savings.savinggoals.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.savings.savinggoals.R
import com.savings.savinggoals.constants.setup.DefaultUIState
import com.savings.savinggoals.constants.setup.DefaultViewModel
import com.savings.savinggoals.databinding.HomeFragmentBinding
import com.savings.savinggoals.util.observeEvent
import com.savings.savinggoals.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val binding by viewBinding(HomeFragmentBinding::bind)

    private val viewModel: HomeViewModel by viewModel()
    private val setupViewModel: DefaultViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupObservers()

        binding.apply {
            toolbar.setOnMenuItemClickListener { item ->
                val navController = findNavController()
                item.onNavDestinationSelected(navController)
                true
            }
        }

        setupViewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is DefaultUIState.Loading -> { }
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            addGoal.setOnClickListener { viewModel.addGoal() }
        }
    }

    private fun setupObservers() {
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is HomeActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}

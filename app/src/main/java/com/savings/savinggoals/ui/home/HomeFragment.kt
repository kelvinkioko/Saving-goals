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
import com.google.android.material.transition.MaterialFadeThrough
import com.savings.savinggoals.R
import com.savings.savinggoals.constants.setup.DefaultUIState
import com.savings.savinggoals.constants.setup.DefaultViewModel
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.databinding.HomeFragmentBinding
import com.savings.savinggoals.util.observeEvent
import com.savings.savinggoals.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val binding by viewBinding(HomeFragmentBinding::bind)

    private val viewModel: HomeViewModel by viewModel()
    private val setupViewModel: DefaultViewModel by viewModel()

    private lateinit var cardView: View

    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter { goalEntity: GoalEntity, view: View -> onGoalPicked(goalEntity, view) }
    }

    private fun onGoalPicked(goal: GoalEntity, cardView: View) {
        this.cardView = cardView
        viewModel.viewGoal(goalID = goal.goalID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
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
        setupGoalsList()
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
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is HomeUIState.DisplayGoals -> populateGoals(it.goalsEntity)
            }
        }

        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is HomeActions.Navigate -> {
                    findNavController().navigate(it.destination)
                }
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

    private fun setupGoalsList() {
        binding.goalsList.adapter = homeAdapter
    }

    private fun populateGoals(goalsEntity: List<GoalEntity>) {
        homeAdapter.setGoals(goalsEntity)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGoals()
    }
}

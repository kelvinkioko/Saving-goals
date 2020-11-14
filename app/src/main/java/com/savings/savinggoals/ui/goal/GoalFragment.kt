package com.savings.savinggoals.ui.goal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savings.savinggoals.R
import com.savings.savinggoals.constants.formatAmount
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.database.entity.GoalSavingEntity
import com.savings.savinggoals.databinding.GoalFragmentBinding
import com.savings.savinggoals.util.observeEvent
import com.savings.savinggoals.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GoalFragment : Fragment(R.layout.goal_fragment) {

    private val binding by viewBinding(GoalFragmentBinding::bind)

    private val viewModel: GoalViewModel by viewModel()
    private val args: GoalFragmentArgs by navArgs()

    private val goalSavingAdapter: GoalSavingAdapter by lazy {
        GoalSavingAdapter { onGoalSavingPicked(it) }
    }

    private fun onGoalSavingPicked(goal: GoalSavingEntity) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupObservers()
        setupClickListeners()
        setupGoalSavingList()

        viewModel.loadGoal(goalID = args.goalID)
    }

    private fun setupClickListeners() {
        binding.apply {
            addSaving.setOnClickListener {
                viewModel.addTransaction()
            }
        }
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupObservers() {
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is GoalActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
                is GoalActions.Navigate -> findNavController().navigate(it.destination)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is GoalUIState.DisplayGoal -> {
                    renderGoalDetails(it.goalEntity)
                    populateGoalSavings(goalSavingEntity = it.goalSavingEntity, currency = it.goalEntity.currency)
                }
            }
        }
    }

    private fun renderGoalDetails(goalEntity: GoalEntity) {
        binding.apply {
            goalTitle.text = goalEntity.name
            goalType.text = goalEntity.type.split("#")[1]
            goalDescription.text = goalEntity.description
            goalAmount.text = "${goalEntity.amount.formatAmount()} ${goalEntity.currency} / ${goalEntity.target_amount.formatAmount()}  ${goalEntity.currency}"
            goalProgress.apply {
                setMax(Integer.parseInt(goalEntity.target_amount.split(".")[0]))
                setProgress(Integer.parseInt(goalEntity.amount.split(".")[0]))
            }
        }
    }

    private fun setupGoalSavingList() {
        binding.goalSavingList.adapter = goalSavingAdapter
    }

    private fun populateGoalSavings(goalSavingEntity: List<GoalSavingEntity>, currency: String) {
        goalSavingAdapter.setGoalSavings(goalSavingList = goalSavingEntity, goalCurrency = currency)
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }
}

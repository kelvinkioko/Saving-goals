package com.savings.savinggoals.ui.goalmanager.type

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savings.savinggoals.R
import com.savings.savinggoals.database.entity.GoalTypeEntity
import com.savings.savinggoals.databinding.FragmentGoalTypeBinding
import com.savings.savinggoals.util.viewBinding

class GoalTypeBottomSheet(private val goalTypeID: String, val resendGoalTypeCallback: (GoalTypeEntity) -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentGoalTypeBinding::bind)

    private val goalTypeAdapter: GoalTypeAdapter by lazy {
        GoalTypeAdapter { onGoalTypePicked(it) }
    }

    private fun onGoalTypePicked(goalType: GoalTypeEntity) {
        resendGoalTypeCallback(goalType)
        dismiss()
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_goal_type, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGoalTypesList()
        val goals = mutableListOf<GoalTypeEntity>()
        goals.add(
            GoalTypeEntity(
                goalTypeID = "GT52",
                goalType = "52 Week saving",
                description = "This is a 52 week saving goal where you save weekly and the amount to save increases each week by a certain set amount."
            )
        )
        goals.add(
            GoalTypeEntity(
                goalTypeID = "GT01",
                goalType = "Target Amount Saving",
                description = "This a target saving where you save for a specific goal. i.e New phone or that bucket list trip"
            )
        )
        goals.add(
            GoalTypeEntity(
                goalTypeID = "GT02",
                goalType = "Fixed Amount Saving",
                description = "This starts with a specific amount and saving is open to any amount any day"
            )
        )

        populateGoalTypes(goalTypeEntity = goals)
    }

    private fun setupGoalTypesList() {
        binding.goalTypeList.adapter = goalTypeAdapter
    }

    private fun populateGoalTypes(goalTypeEntity: List<GoalTypeEntity>) {
        goalTypeAdapter.setGoalTypes(goalTypeEntity, goalTypeID = goalTypeID)
    }
}

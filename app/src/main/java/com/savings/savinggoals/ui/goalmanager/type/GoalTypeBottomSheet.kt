package com.savings.savinggoals.ui.goalmanager.type

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    /**
     * This function makes BottomSheetDialogFragment full screen and without collapsed state
     * For some reason this doesn't work without the params.height
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val params = bottomSheet.layoutParams
            bottomSheet.layoutParams = params
            BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
        return dialog
    }

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
                goalType = "Target saving",
                description = "This a target saving where you save for a specific goal. i.e New phone or that bucket list trip"
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

package com.savings.savinggoals.ui.goalmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.savings.savinggoals.R
import com.savings.savinggoals.constants.PreferenceHandler
import com.savings.savinggoals.constants.formatDateHeader
import com.savings.savinggoals.database.entity.GoalTypeEntity
import com.savings.savinggoals.databinding.AddGoalFragmentBinding
import com.savings.savinggoals.util.formatAmount
import com.savings.savinggoals.util.getUnformatedAmount
import com.savings.savinggoals.util.observeEvent
import com.savings.savinggoals.util.viewBinding
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.add_goal_fragment.endDateValue
import kotlinx.android.synthetic.main.add_goal_fragment.targetAmountValue
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddGoalFragment : Fragment(R.layout.add_goal_fragment) {

    private val binding by viewBinding(AddGoalFragmentBinding::bind)

    private val viewModel: AddGoalViewModel by viewModel()

    private lateinit var preferenceHandler: PreferenceHandler

    private lateinit var goalTypeEntity: GoalTypeEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceHandler = PreferenceHandler(requireContext())

        setupToolBar()
        setupClickListener()
        setupObservers()
        inputChangeListener()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupClickListener() {
        binding.apply {
            goalType.root.setOnClickListener {
                viewModel.chooseGoalType()
            }
            startDate.editText!!.setOnClickListener {
                // Date Range Picker
                val builder = MaterialDatePicker.Builder.datePicker()
                val picker = builder.build()
                picker.show(requireActivity().supportFragmentManager, picker.toString())
                picker.addOnCancelListener {
                    Log.d("DatePicker Activity", "Dialog was cancelled")
                }
                picker.addOnPositiveButtonClickListener {
                    val uniformDateFormatter = SimpleDateFormat("dd/MM/yyyy").format(it)
                    viewModel.setupStartDate(uniformDateFormatter)
                    startDate.editText!!.setText(formatDateHeader(uniformDateFormatter))
                }
            }
            endDate.editText!!.setOnClickListener {
                // Date Range Picker
                val builder = MaterialDatePicker.Builder.datePicker()
                val picker = builder.build()
                picker.show(requireActivity().supportFragmentManager, picker.toString())
                picker.addOnCancelListener {
                    Log.d("DatePicker Activity", "Dialog was cancelled")
                }
                picker.addOnPositiveButtonClickListener {
                    val uniformDateFormatter = SimpleDateFormat("dd/MM/yyyy").format(it)
                    viewModel.setupEndDate(uniformDateFormatter)
                    endDate.editText!!.setText(formatDateHeader(uniformDateFormatter))
                }
            }
            saveGoal.setOnClickListener {
                binding.apply {
                    if (goalName.editText!!.text.toString().isEmpty()) {
                        goalName.error = "Please give your goal a name"
                    } else if (goalDescription.editText!!.text.toString().isEmpty()) {
                        goalDescription.error = "Please describe your goal"
                    } else if (goalTypeEntity.goalTypeID.equals("GT52", ignoreCase = true) && incrementalAmountValue.text.toString().isEmpty()) {
                        incrementalAmount.error = "Please set you weekly incremental amount"
                    } else if (!goalTypeEntity.goalTypeID.equals("GT52", ignoreCase = true) && targetAmountValue.text.toString().isEmpty()) {
                        targetAmount.error = "Please set the total amount you wish to save"
                    } else {
                        viewModel.saveGoal(
                            goalName = goalName.editText!!.text.toString(),
                            goalDescription = goalDescription.editText!!.text.toString(),
                            startAmount = startAmountValue.getUnformatedAmount().replace(preferenceHandler.getCurrency()!!, ""),
                            targetAmount = targetAmountValue.getUnformatedAmount().replace(preferenceHandler.getCurrency()!!, ""),
                            incrementalAmount = incrementalAmountValue.getUnformatedAmount().replace(preferenceHandler.getCurrency()!!, ""),
                            currency = preferenceHandler.getCurrency()!!
                        )
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AddGoalActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
                is AddGoalActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AddGoalUIState.Success -> findNavController().navigateUp()
                is AddGoalUIState.PageSetup -> {
                    updateGoalTypeView(it.goalTypeEntity)
                    binding.apply {
                        startDateValue.setText(it.startDate)
                        endDateValue.setText(it.endDate)
                    }
                }
                is AddGoalUIState.GoalType -> updateGoalTypeView(it.goalTypeEntity)
                is AddGoalUIState.TargetAmount -> targetAmountValue.setText("${it.targetAmount.formatAmount()} ${preferenceHandler.getCurrency()!!}")
                is AddGoalUIState.UpdateEndDate -> endDateValue.setText(it.endDate)
            }
        }
    }

    private fun updateGoalTypeView(goalTypeEntity: GoalTypeEntity) {
        this.goalTypeEntity = goalTypeEntity
        binding.apply {
            goalType.apply {
                accountTypeName.text = goalTypeEntity.goalType
                accountTypeDescription.text = goalTypeEntity.description
                accountTypeDescription.isVisible = true
            }

            if (goalTypeEntity.goalTypeID.equals("GT52", ignoreCase = true)) {
                startAmount.isVisible = true
                targetAmount.isVisible = true
                targetAmount.hint = "Target amount (auto-calculated)"
                incrementalAmount.isVisible = true
                endDate.isClickable = false
                endDateValue.isClickable = false
            } else if (goalTypeEntity.goalTypeID.equals("GT01", ignoreCase = true) ||
                goalTypeEntity.goalTypeID.equals("GT02", ignoreCase = true)) {
                incrementalAmount.isGone = true
                targetAmount.isVisible = true
                startAmount.isGone = true
                endDateValue.isClickable = true

                targetAmount.hint = "Target amount"
            }

            // Reset Values
            targetAmountValue.setText("")
            incrementalAmountValue.setText("")
            startAmountValue.setText("")
        }
    }

    private fun inputChangeListener() {
        binding.apply {
            var current = ""
            startAmountValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val stringText = charSequence.toString()
                    if (stringText != current && stringText.isNotEmpty()) {
                        startAmountValue.removeTextChangedListener(this)
                        current = startAmountValue.formatAmount(stringText, currency = preferenceHandler.getCurrency()!!)
                        startAmountValue.addTextChangedListener(this)
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                    if (startAmountValue.text.toString().isNotEmpty() && incrementalAmountValue.text.toString().isNotEmpty()) {
                        viewModel.setup52WeekTarget(
                            startAmount = startAmountValue.getUnformatedAmount().replace(preferenceHandler.getCurrency()!!, ""),
                            incrementalAmount = incrementalAmountValue.getUnformatedAmount().replace(preferenceHandler.getCurrency()!!, "")
                        )
                    }
                }
            })

            targetAmountValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val stringText = charSequence.toString()
                    if (stringText != current && stringText.isNotEmpty()) {
                        targetAmountValue.removeTextChangedListener(this)
                        current = targetAmountValue.formatAmount(stringText, currency = preferenceHandler.getCurrency()!!)
                        targetAmountValue.addTextChangedListener(this)
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            })

            incrementalAmountValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val stringText = charSequence.toString()
                    if (stringText != current && stringText.isNotEmpty()) {
                        incrementalAmountValue.removeTextChangedListener(this)
                        current = incrementalAmountValue.formatAmount(stringText, currency = preferenceHandler.getCurrency()!!)
                        incrementalAmountValue.addTextChangedListener(this)
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                    if (startAmountValue.text.toString().isNotEmpty() && incrementalAmountValue.text.toString().isNotEmpty()) {
                        viewModel.setup52WeekTarget(
                            startAmount = startAmountValue.getUnformatedAmount().replace(preferenceHandler.getCurrency()!!, ""),
                            incrementalAmount = incrementalAmountValue.getUnformatedAmount().replace(preferenceHandler.getCurrency()!!, "")
                        )
                    }
                }
            })
        }
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }
}

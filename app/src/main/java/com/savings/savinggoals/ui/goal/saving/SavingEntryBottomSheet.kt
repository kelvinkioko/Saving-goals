package com.savings.savinggoals.ui.goal.saving

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.savings.savinggoals.R
import com.savings.savinggoals.constants.formatDateHeader
import com.savings.savinggoals.constants.getCurrentDateAsString
import com.savings.savinggoals.databinding.DialogSavingEntryBinding
import com.savings.savinggoals.util.formatAmount
import com.savings.savinggoals.util.getUnformatedAmount
import com.savings.savinggoals.util.viewBinding
import java.text.SimpleDateFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavingEntryBottomSheet(private val goalID: String, private val currency: String, val resendGoalTypeCallback: () -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(DialogSavingEntryBinding::bind)

    private val viewModel: SaveEntryViewModel by viewModel()

    private var transactionDateEntry = ""
    private var transactionTypeEntry = "Saving"

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
    ): View = inflater.inflate(R.layout.dialog_saving_entry, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setOnClickListener()
        inputChangeListener()
        setupObservers()
    }

    private fun setupData() {
        binding.apply {
            transactionDateEntry = getCurrentDateAsString()
            transactionDate.editText!!.setText(formatDateHeader(transactionDateEntry))
        }
    }

    private fun setOnClickListener() {
        binding.apply {
            cancelAction.setOnClickListener {
                dismiss()
            }
            transactionSaving.apply {
                transactionName.text = "Saving"
                transactionRadio.isChecked = true
            }
            transactionWithdraw.apply {
                transactionName.text = "Withdraw"
                transactionRadio.isChecked = false
            }
            transactionSaving.root.setOnClickListener {
                transactionSaving.transactionRadio.isChecked = true
                transactionWithdraw.transactionRadio.isChecked = false
                transactionTypeEntry = "Saving"
            }
            transactionWithdraw.root.setOnClickListener {
                transactionSaving.transactionRadio.isChecked = false
                transactionWithdraw.transactionRadio.isChecked = true
                transactionTypeEntry = "Withdraw"
            }
            transactionDate.editText!!.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                val picker = builder.build()
                picker.show(requireActivity().supportFragmentManager, picker.toString())
                picker.addOnCancelListener {
                    Log.d("DatePicker Activity", "Dialog was cancelled")
                }
                picker.addOnPositiveButtonClickListener {
                    transactionDateEntry = SimpleDateFormat("dd/MM/yyyy").format(it)
                    transactionDate.editText!!.setText(formatDateHeader(transactionDateEntry))
                }
            }
            saveGoal.setOnClickListener {
                if (transactionAmountValue.text.toString().isEmpty()) {
                    transactionAmount.error = "Please set the amount ${if (transactionTypeEntry.equals("Saving", true)) "saved" else "withdrawn"}"
                } else {
                    viewModel.saveGoal(
                        goalID = goalID,
                        amount = transactionAmountValue.getUnformatedAmount().replace(currency, ""),
                        note = transactionDescription.editText!!.text.toString(),
                        type = transactionTypeEntry,
                        transactionDate = transactionDateEntry
                    )
                }
            }
        }
    }

    private fun inputChangeListener() {
        binding.apply {
            var current = ""
            transactionAmountValue.addTextChangedListener(object : TextWatcher {
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
                        transactionAmountValue.removeTextChangedListener(this)
                        current = transactionAmountValue.formatAmount(stringText, currency = currency)
                        transactionAmountValue.addTextChangedListener(this)
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is SaveEntryUIState.Success -> {
                    resendGoalTypeCallback()
                    dismiss()
                }
            }
        }
    }
}

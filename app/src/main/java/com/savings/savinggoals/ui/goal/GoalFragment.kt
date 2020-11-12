package com.savings.savinggoals.ui.goal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.savings.savinggoals.R
import com.savings.savinggoals.databinding.GoalFragmentBinding
import com.savings.savinggoals.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GoalFragment : Fragment(R.layout.goal_fragment) {

    private val binding by viewBinding(GoalFragmentBinding::bind)

    private val viewModel: GoalViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupToolBar() {
        binding.apply {
            // toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }
}

package com.savings.savinggoals.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.databinding.ItemGoalBinding
import com.savings.savinggoals.util.formatAmount

class HomeAdapter(private val goalTypeClicked: (GoalEntity, View) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<GoalEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(
            ItemGoalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setGoals(goal: List<GoalEntity>) {
        items.clear()
        items.addAll(goal)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(
        private val binding: ItemGoalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                goalTypeClicked.invoke(items[adapterPosition], binding.cardView)
            }
        }

        fun bind(goal: GoalEntity) {
            binding.apply {
                goalName.text = goal.name
                goalType.text = goal.type.split("#")[1]
                goalDescription.text = goal.description

                goalAmount.text = "(${goal.amount.formatAmount()} / ${goal.target_amount.formatAmount()}) ${goal.currency}"
                goalProgress.apply {
                    setMax(goal.target_amount.toFloat())
                    setProgress(goal.amount.toFloat())
                }
            }
        }
    }
}

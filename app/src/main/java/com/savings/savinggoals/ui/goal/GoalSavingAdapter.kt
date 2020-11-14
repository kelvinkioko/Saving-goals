package com.savings.savinggoals.ui.goal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.savings.savinggoals.constants.formatDateHeader
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.database.entity.GoalSavingEntity
import com.savings.savinggoals.databinding.ItemSavingBinding

class GoalSavingAdapter(private val goalSavingClicked: (GoalSavingEntity) -> Unit) :
    RecyclerView.Adapter<GoalSavingAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<GoalSavingEntity>()
    private lateinit var goalEntity: GoalEntity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(
            ItemSavingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setGoalSavings(goalSavingList: List<GoalSavingEntity>, goalEntity: GoalEntity) {
        this.goalEntity = goalEntity
        items.clear()
        items.addAll(goalSavingList)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(
        private val binding: ItemSavingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                goalSavingClicked.invoke(items[adapterPosition])
            }
        }

        fun bind(goalSaving: GoalSavingEntity) {
            binding.apply {
                goalSavingTitle.text = if (goalEntity.type.split("#")[0].equals("GT52", ignoreCase = true)) {
                    formatDateHeader(goalSaving.startDate) + " - " + formatDateHeader(goalSaving.endDate)
                } else {
                    formatDateHeader(goalSaving.createdAt)
                }
                goalSavingAmount.text = "${goalSaving.amount} ${goalEntity.currency}"
                goalSavingDescription.text = goalSaving.weekPosition
                goalSavingDescription.isGone = goalSaving.weekPosition.isEmpty()

                savingRadio.isChecked = goalSaving.save_status.equals("Complete", ignoreCase = true)
            }
        }
    }
}

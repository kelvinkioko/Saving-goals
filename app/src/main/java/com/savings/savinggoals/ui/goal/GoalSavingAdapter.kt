package com.savings.savinggoals.ui.goal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.savings.savinggoals.R
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
                if (goalEntity.type.split("#")[0].equals("GT52", ignoreCase = true)) {
                    goalSavingClicked.invoke(items[adapterPosition])
                }
            }
        }

        fun bind(goalSaving: GoalSavingEntity) {
            binding.apply {
                val resource = savingIndicator.context.resources
                if (goalEntity.type.split("#")[0].equals("GT52", ignoreCase = true)) {
                    goalSavingTitle.text = formatDateHeader(goalSaving.startDate) + " - " + formatDateHeader(goalSaving.endDate)
                    goalSavingDescription.text = goalSaving.weekPosition
                    goalSavingDescription.isVisible = true
                } else {
                    goalSavingTitle.text = formatDateHeader(goalSaving.createdAt)
                    goalSavingDescription.text = goalSaving.save_type
                    goalSavingDescription.isVisible = true
                }
                if (goalSaving.save_type.equals("Deposit", ignoreCase = true) || goalSaving.save_type.equals("Saving", ignoreCase = true)) {
                    savingIndicator.setImageDrawable(VectorDrawableCompat.create(resource, R.drawable.ic_plus, null)!!)
                    goalSavingAmount.setTextColor(resource.getColor(R.color.colorPositive))
                } else {
                    savingIndicator.setImageDrawable(VectorDrawableCompat.create(resource, R.drawable.ic_minus, null)!!)
                    goalSavingAmount.setTextColor(resource.getColor(R.color.colorNegative))
                }
                if (goalSaving.save_status.equals("Pending", ignoreCase = true)) {
                    savingIndicator.isGone = true
                } else {
                    savingIndicator.isVisible = true
                }
                goalSavingAmount.text = "${goalSaving.amount} ${goalEntity.currency}"
            }
        }
    }
}

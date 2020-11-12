package com.savings.savinggoals.ui.goalmanager.type

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.savings.savinggoals.database.entity.GoalTypeEntity
import com.savings.savinggoals.databinding.ItemCurrencyBinding

class GoalTypeAdapter(private val goalTypeClicked: (GoalTypeEntity) -> Unit) :
    RecyclerView.Adapter<GoalTypeAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<GoalTypeEntity>()
    private var goalTypeID: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(
            ItemCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setGoalTypes(goalType: List<GoalTypeEntity>, goalTypeID: String) {
        this.goalTypeID = goalTypeID
        items.clear()
        items.addAll(goalType)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(
        private val binding: ItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                goalTypeClicked.invoke(items[adapterPosition])
            }
        }

        fun bind(goalType: GoalTypeEntity) {
            binding.apply {
                currencyName.text = goalType.goalType
                currencyDetails.text = goalType.description

                currencyRadio.apply {
                    isVisible = true
                    isChecked = goalTypeID.equals(goalType.goalTypeID, ignoreCase = true)
                }
            }
        }
    }
}

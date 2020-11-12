package com.savings.savinggoals.ui.settings.currency

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savings.savinggoals.R
import com.savings.savinggoals.constants.PreferenceHandler
import com.savings.savinggoals.databinding.ItemCurrencyBinding
import com.savings.savinggoals.databinding.ItemCurrencyHeaderBinding

class CurrencyAdapter(private val currencyClicked: (SectionedCurrencyItem.CurrencyItems) -> Unit) :
    ListAdapter<SectionedCurrencyItem, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val itemCurrencyHeader = R.layout.item_currency_header
    private val itemCurrency = R.layout.item_currency

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            itemCurrencyHeader -> CurrencyItemHeaderViewHolder(
                ItemCurrencyHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> CurrencyItemViewHolder(
                ItemCurrencyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SectionedCurrencyItem.CurrencyItems -> (holder as CurrencyItemViewHolder).bind(item)
            is SectionedCurrencyItem.CurrencyHeader -> (holder as CurrencyItemHeaderViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SectionedCurrencyItem.CurrencyItems -> itemCurrency
            is SectionedCurrencyItem.CurrencyHeader -> itemCurrencyHeader
        }
    }

    inner class CurrencyItemViewHolder(
        private val binding: ItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                PreferenceHandler(itemView.context).apply {
                    setCurrency((getItem(position) as SectionedCurrencyItem.CurrencyItems).currencyCode)
                    notifyDataSetChanged()
                }
                currencyClicked.invoke(getItem(position) as SectionedCurrencyItem.CurrencyItems)
            }
        }

        fun bind(content: SectionedCurrencyItem.CurrencyItems) {
            binding.apply {
                currencyName.text = content.country
                currencyDetails.text = "${content.currency} | ${content.currencyCode} | ${content.currencySymbol}"

                if (PreferenceHandler(currencyName.context).getCurrency().equals(content.currencyCode, ignoreCase = false)) {
                    currencyRadio.isVisible = true
                } else {
                    currencyRadio.isGone = true
                }
            }
        }
    }

    inner class CurrencyItemHeaderViewHolder(
        private val binding: ItemCurrencyHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(content: SectionedCurrencyItem.CurrencyHeader) {
            binding.apply {
                currencyRegionHeader.text = content.title
                currencyRegionHeader.paintFlags = currencyRegionHeader.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<SectionedCurrencyItem>() {
            override fun areItemsTheSame(old: SectionedCurrencyItem, new: SectionedCurrencyItem): Boolean =
                old == new

            override fun areContentsTheSame(old: SectionedCurrencyItem, new: SectionedCurrencyItem): Boolean =
                old == new
        }
    }
}

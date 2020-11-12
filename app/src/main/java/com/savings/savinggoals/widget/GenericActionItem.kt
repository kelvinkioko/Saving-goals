package com.savings.savinggoals.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.savings.savinggoals.R
import com.savings.savinggoals.databinding.ItemGenericActionBinding

class GenericActionItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val itemBinding =
        ItemGenericActionBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.GenericClickableItem).apply {
                setItemText(getString(R.styleable.GenericClickableItem_text).orEmpty())
                setItemSubText(getString(R.styleable.GenericClickableItem_subText).orEmpty())
                getDrawable(R.styleable.GenericClickableItem_profile)?.let { icon -> setItemIcon(icon) }
                recycle()
            }
        }
    }

    private fun setItemSubText(itemSubText: String) {
        itemBinding.apply {
            actionSubTitle.text = itemSubText
            actionSubTitle.isVisible = actionSubTitle.text.isNotEmpty()
        }
    }

    private fun setItemText(itemText: String) {
        itemBinding.actionTitle.text = itemText
    }

    private fun setItemIcon(icon: Drawable) {
        itemBinding.apply {
            actionIcon.setImageDrawable(icon)
            actionIcon.isVisible = true
        }
    }
}

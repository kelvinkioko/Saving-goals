package com.savings.savinggoals.widget.progress

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.savings.savinggoals.R
import com.savings.savinggoals.widget.progress.common.BaseRoundCornerProgressBar

/**
 * Created by Akexorcist on 9/14/15 AD.
 */
class RoundCornerProgressBar : BaseRoundCornerProgressBar {
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    public override fun initLayout(): Int {
        return R.layout.round_corner_progress_bar
    }

    override fun initStyleable(context: Context?, attrs: AttributeSet?) {}
    override fun initView() {}
    override fun drawProgress(
        layoutProgress: LinearLayout?,
        max: Float,
        progress: Float,
        totalWidth: Float,
        radius: Int,
        padding: Int,
        colorProgress: Int,
        isReverse: Boolean
    ) {
        val backgroundDrawable = createGradientDrawable(colorProgress)
        val newRadius = radius - padding / 2
        backgroundDrawable.cornerRadii = floatArrayOf(
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat(),
            newRadius.toFloat()
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress!!.background = backgroundDrawable
        } else {
            layoutProgress!!.setBackgroundDrawable(backgroundDrawable)
        }
        val ratio = max / progress
        val progressWidth = ((totalWidth - padding * 2) / ratio).toInt()
        val progressParams = layoutProgress.layoutParams
        progressParams.width = progressWidth
        layoutProgress.layoutParams = progressParams
    }

    override fun onViewDraw() {}
}

package com.savings.savinggoals.widget.progress

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.savings.savinggoals.R

// @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
open class ArcProgress : View {
    private var paint: Paint? = null
    private var textPaint: Paint? = null

    private val rectF = RectF()

    private var strokeWidth = 0f
    private var suffixTextSize = 0f
    private var bottomTextSize = 0f
    private var bottomText: String? = null
    private var textSize = 0f
    private var textColor = 0
    private var progress = 0
    private var max = 0
    private var finishedStrokeColor = 0
    private var unfinishedStrokeColor = 0
    private var arcAngle = 0f
    private var suffixText: String? = "%"
    private var suffixTextPadding = 0f

    private var arcBottomHeight = 0f

    private val defaultFinishedColor = Color.WHITE
    private val defaultUnfinishedColor = Color.rgb(72, 106, 176)
    private val defaultTextColor = Color.rgb(66, 145, 241)
    private var defaultSuffixTextSize = 0f
    private var defaultSuffixPadding = 0f
    private var defaultBottomTextSize = 0f
    private var defaultStrokeWidth = 0f
    private var defaultSuffixText: String? = null
    private val defaultMax = 100
    private val defaultArcAngle = 360 * 0.8f
    private var defaultTextSize = 0f
    private var minSize = 0

    private val INSTANCE_STATE = "saved_instance"
    private val INSTANCE_STROKE_WIDTH = "stroke_width"
    private val INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size"
    private val INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding"
    private val INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size"
    private val INSTANCE_BOTTOM_TEXT = "bottom_text"
    private val INSTANCE_TEXT_SIZE = "text_size"
    private val INSTANCE_TEXT_COLOR = "text_color"
    private val INSTANCE_PROGRESS = "progress"
    private val INSTANCE_MAX = "max"
    private val INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color"
    private val INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color"
    private val INSTANCE_ARC_ANGLE = "arc_angle"
    private val INSTANCE_SUFFIX = "suffix"

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        defaultTextSize = sp2px(resources, 18F)
        minSize = dp2px(resources, 100F).toInt()
        defaultTextSize = sp2px(resources, 40F)
        defaultSuffixTextSize = sp2px(resources, 15F)
        defaultSuffixPadding = dp2px(resources, 4F)
        defaultSuffixText = "%"
        defaultBottomTextSize = sp2px(resources, 10F)
        defaultStrokeWidth = dp2px(resources, 4F)

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0)
        initByAttributes(attributes)
        attributes.recycle()
        initPainters()
    }

    protected open fun initByAttributes(attributes: TypedArray) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, defaultFinishedColor)
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, defaultUnfinishedColor)
        textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, defaultTextColor)
        textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, defaultTextSize)
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_angle, defaultArcAngle)
        setMax(attributes.getInt(R.styleable.ArcProgress_max, defaultMax))
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0))
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, defaultStrokeWidth)
        suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, defaultSuffixTextSize)
        suffixText = if (TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_suffix_text))) defaultSuffixText else attributes.getString(
            R.styleable.ArcProgress_suffix_text
        )
        suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_suffix_text_padding, defaultSuffixPadding)
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, defaultBottomTextSize)
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text)
    }

    private fun initPainters() {
        textPaint = TextPaint()
        textPaint!!.color = textColor
        textPaint!!.textSize = textSize
        textPaint!!.isAntiAlias = true
        paint = Paint()
        paint!!.color = defaultUnfinishedColor
        paint!!.isAntiAlias = true
        paint!!.strokeWidth = strokeWidth
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeCap = Paint.Cap.ROUND
    }

    override fun invalidate() {
        initPainters()
        super.invalidate()
    }

    private fun getStrokeWidth(): Float {
        return strokeWidth
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        this.invalidate()
    }

    private fun getSuffixTextSize(): Float {
        return suffixTextSize
    }

    fun setSuffixTextSize(suffixTextSize: Float) {
        this.suffixTextSize = suffixTextSize
        this.invalidate()
    }

    private fun getBottomText(): String? {
        return bottomText
    }

    fun setBottomText(bottomText: String?) {
        this.bottomText = bottomText
        this.invalidate()
    }

    fun getProgress(): Int {
        return progress
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        if (this.progress > getMax()) {
            this.progress %= getMax()
        }
        invalidate()
    }

    fun getMax(): Int {
        return max
    }

    fun setMax(max: Int) {
        if (max > 0) {
            this.max = max
            invalidate()
        }
    }

    fun getBottomTextSize(): Float {
        return bottomTextSize
    }

    fun setBottomTextSize(bottomTextSize: Float) {
        this.bottomTextSize = bottomTextSize
        this.invalidate()
    }

    fun getTextSize(): Float {
        return textSize
    }

    fun setTextSize(textSize: Float) {
        this.textSize = textSize
        this.invalidate()
    }

    fun getTextColor(): Int {
        return textColor
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        this.invalidate()
    }

    fun getFinishedStrokeColor(): Int {
        return finishedStrokeColor
    }

    fun setFinishedStrokeColor(finishedStrokeColor: Int) {
        this.finishedStrokeColor = finishedStrokeColor
        this.invalidate()
    }

    fun getUnfinishedStrokeColor(): Int {
        return unfinishedStrokeColor
    }

    fun setUnfinishedStrokeColor(unfinishedStrokeColor: Int) {
        this.unfinishedStrokeColor = unfinishedStrokeColor
        this.invalidate()
    }

    fun getArcAngle(): Float {
        return arcAngle
    }

    fun setArcAngle(arcAngle: Float) {
        this.arcAngle = arcAngle
        this.invalidate()
    }

    private fun getSuffixText(): String? {
        return suffixText
    }

    fun setSuffixText(suffixText: String?) {
        this.suffixText = suffixText
        this.invalidate()
    }

    fun getSuffixTextPadding(): Float {
        return suffixTextPadding
    }

    fun setSuffixTextPadding(suffixTextPadding: Float) {
        this.suffixTextPadding = suffixTextPadding
        this.invalidate()
    }

    override fun getSuggestedMinimumHeight(): Int {
        return minSize
    }

    override fun getSuggestedMinimumWidth(): Int {
        return minSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        rectF[strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f] = MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f
        val radius = width / 2f
        val angle = (360 - arcAngle) / 2f
        arcBottomHeight = radius * (1 - Math.cos(angle / 180 * Math.PI)).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val startAngle = 270 - arcAngle / 2f
        val finishedSweepAngle = progress / getMax().toFloat() * arcAngle
        paint!!.color = unfinishedStrokeColor
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint!!)
        paint!!.color = finishedStrokeColor
        canvas.drawArc(rectF, startAngle, finishedSweepAngle, false, paint!!)
        val text = getProgress().toString()
        if (!TextUtils.isEmpty(text)) {
            textPaint!!.color = textColor
            textPaint!!.textSize = textSize
            val textHeight: Float = textPaint!!.descent() + textPaint!!.ascent()
            val textBaseline = (height - textHeight) / 2.0f
            canvas.drawText(text, (width - textPaint!!.measureText(text)) / 2.0f, textBaseline, textPaint!!)
            textPaint!!.textSize = suffixTextSize
            val suffixHeight: Float = textPaint!!.descent() + textPaint!!.ascent()
            canvas.drawText(suffixText!!, width / 2.0f + textPaint!!.measureText(text) + suffixTextPadding, textBaseline + textHeight - suffixHeight, textPaint!!)
        }
        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint!!.textSize = bottomTextSize
            val bottomTextBaseline: Float = height - arcBottomHeight - (textPaint!!.descent() + textPaint!!.ascent()) / 2
            canvas.drawText(getBottomText()!!, (width - textPaint!!.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint!!)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth())
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize())
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding())
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize())
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText())
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize())
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor())
        bundle.putInt(INSTANCE_PROGRESS, getProgress())
        bundle.putInt(INSTANCE_MAX, getMax())
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor())
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor())
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle())
        bundle.putString(INSTANCE_SUFFIX, getSuffixText())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            strokeWidth = state.getFloat(INSTANCE_STROKE_WIDTH)
            suffixTextSize = state.getFloat(INSTANCE_SUFFIX_TEXT_SIZE)
            suffixTextPadding = state.getFloat(INSTANCE_SUFFIX_TEXT_PADDING)
            bottomTextSize = state.getFloat(INSTANCE_BOTTOM_TEXT_SIZE)
            bottomText = state.getString(INSTANCE_BOTTOM_TEXT)
            textSize = state.getFloat(INSTANCE_TEXT_SIZE)
            textColor = state.getInt(INSTANCE_TEXT_COLOR)
            setMax(state.getInt(INSTANCE_MAX))
            setProgress(state.getInt(INSTANCE_PROGRESS))
            finishedStrokeColor = state.getInt(INSTANCE_FINISHED_STROKE_COLOR)
            unfinishedStrokeColor = state.getInt(INSTANCE_UNFINISHED_STROKE_COLOR)
            suffixText = state.getString(INSTANCE_SUFFIX)
            initPainters()
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    fun dp2px(resources: Resources, dp: Float): Float {
        val scale: Float = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun sp2px(resources: Resources, sp: Float): Float {
        val scale: Float = resources.displayMetrics.scaledDensity
        return sp * scale
    }
}

package com.baha.reviewbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

class ReviewBar : LinearLayoutCompat {

    private enum class ReviewIcon {
        EMPTY, HALF, FILL
    }

    private var reviewScore = 3.8f
    private var reviewIconMap = HashMap<ReviewIcon, Drawable>()
    private var reviewScoreMax = 5
    private var reviewIconSpacing = 0
    private var halfEnable = true
    private var halfRangeMin = 0.3f
    private var halfRangeMax = 0.7f
    private var scaleTouchSlop = 0
    private var isIndicator = true
    private var touchDownX = 0f
    private var isDragging = false
    private var whenDragMinScore = 0f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isClickable = true
        isFocusable = true
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReviewBar, 0, 0)
        val fillIcon = typedArray.getResourceId(R.styleable.ReviewBar_fillIcon, R.drawable.ic_baseline_star_24)
        val halfIcon = typedArray.getResourceId(R.styleable.ReviewBar_halfIcon, R.drawable.ic_baseline_star_half_24)
        val emptyIcon = typedArray.getResourceId(R.styleable.ReviewBar_emptyIcon, R.drawable.ic_baseline_star_border_24)
        halfRangeMin = typedArray.getFloat(R.styleable.ReviewBar_halfRangeMin, 0.3f)
        halfRangeMax = typedArray.getFloat(R.styleable.ReviewBar_halfRangeMax, 0.7f)
        isIndicator = typedArray.getBoolean(R.styleable.ReviewBar_isIndicator, true)
        halfEnable = typedArray.getBoolean(R.styleable.ReviewBar_halfEnable, true)
        reviewIconSpacing = typedArray.getInt(R.styleable.ReviewBar_iconSpace, 0)
        reviewScore = typedArray.getFloat(R.styleable.ReviewBar_reviewScore, 0f)
        reviewScoreMax = typedArray.getInteger(R.styleable.ReviewBar_reviewScoreMax, 5)
        whenDragMinScore = typedArray.getFloat(R.styleable.ReviewBar_whenDragScoreMin, 0f)
        typedArray.recycle()

        reviewIconMap.apply {
            put(ReviewIcon.FILL, ContextCompat.getDrawable(context, fillIcon)!!)
            put(ReviewIcon.HALF, ContextCompat.getDrawable(context, halfIcon)!!)
            put(ReviewIcon.EMPTY, ContextCompat.getDrawable(context, emptyIcon)!!)
        }
        initView()
    }

    private fun initView() {
        for (i in 0 until reviewScoreMax) {
            addReviewIcon(i)
        }
    }

    private fun addReviewIcon(position: Int) {
        val icon = AppCompatImageView(context).apply {
            val layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            val iconSpacing = if (position == reviewScoreMax - 1) 0 else reviewIconSpacing
            layoutParams.marginEnd = iconSpacing
            this.layoutParams = layoutParams
            scaleType = ImageView.ScaleType.FIT_CENTER
            setReviewScoreIcon(this, position)
        }
        addView(icon)
        scaleTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun setReviewScoreIcon(imageView: AppCompatImageView, position: Int) {
        val drawable = if (reviewScore > 0f) {
            when {
                position <= floor(reviewScore - 1) -> reviewIconMap[ReviewIcon.FILL]
                position == ceil(reviewScore - 1).toInt() -> {
                    val drawable = when {
                        ((reviewScore * 100).toInt() - (floor(reviewScore) * 100).toInt()) < (halfRangeMin * 100).toInt() -> {
                            reviewIconMap[ReviewIcon.EMPTY]
                        }
                        ((reviewScore * 100).toInt() - (floor(reviewScore) * 100).toInt()) > (halfRangeMax * 100).toInt() -> {
                            reviewIconMap[ReviewIcon.FILL]
                        }
                        halfEnable -> {
                            reviewIconMap[ReviewIcon.HALF]
                        }
                        else -> {
                            reviewIconMap[ReviewIcon.EMPTY]
                        }
                    }
                    drawable
                }
                else -> reviewIconMap[ReviewIcon.EMPTY]
            }
        }
        else {
            reviewIconMap[ReviewIcon.EMPTY]
        }
        imageView.setImageDrawable(drawable)
    }

    fun setReviewIcon(fillIconRes:Int,halfIconRes:Int,emptyIconRes:Int){
        removeAllViews()
        reviewIconMap.apply {
            put(ReviewIcon.FILL, ContextCompat.getDrawable(context, fillIconRes)!!)
            put(ReviewIcon.HALF, ContextCompat.getDrawable(context, halfIconRes)!!)
            put(ReviewIcon.EMPTY, ContextCompat.getDrawable(context, emptyIconRes)!!)
        }
        initView()
    }

    fun setReviewScore(score: Float, isFormDrag: Boolean = false) {
        if (this.reviewScore == score || (isFormDrag && score < whenDragMinScore)) {
            return
        }

        this.reviewScore = score
        refreshReviewScore()
    }

    fun setReviewScoreMax(max:Int){
        reviewScoreMax = max
        removeAllViews()
        initView()
    }

    fun setIndicatorEnable(enable: Boolean) {
        isIndicator = enable
    }

    fun setHalfEnable(enable: Boolean) {
        halfEnable = enable
    }

    fun setHalfMinMax(min: Float, max: Float) {
        halfRangeMin = min
        halfRangeMax = max
    }

    fun setWhenDragScoreMin(min: Float) {
        whenDragMinScore = min
    }

    private fun refreshReviewScore() {
        for (i in 0..childCount) {
            val childAt = getChildAt(i)
            if (childAt != null) {
                setReviewScoreIcon(childAt as AppCompatImageView, i)
            }
        }
    }

    private fun isInScrollingContainer(): Boolean {
        var parentGroup = parent
        while (parentGroup != null && parentGroup is ViewGroup) {
            if (parentGroup.shouldDelayChildPressedState()) {
                return true
            }
            parentGroup = parentGroup.parent
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isIndicator) {
            return false
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isInScrollingContainer()) {
                    touchDownX = event.x
                }
                else {
                    startDrag(event)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    trackTouchEvent(event)
                }
                else {
                    val x = event.x
                    if (Math.abs(x - touchDownX) > scaleTouchSlop) {
                        startDrag(event)
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                if (isDragging) {
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                    isPressed = false
                }
                else {
                    onStartTrackingTouch()
                    trackTouchEvent(event)
                    onStopTrackingTouch()
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                if (isDragging) {
                    onStopTrackingTouch()
                    isPressed = false
                }
            }
        }

        return true
    }

    private fun startDrag(event: MotionEvent) {
        isPressed = true
        onStartTrackingTouch()
        trackTouchEvent(event)
    }

    private fun onStartTrackingTouch() {
        isDragging = true
    }

    private fun onStopTrackingTouch() {
        isDragging = false
    }

    private fun trackTouchEvent(event: MotionEvent) {
        val x = event.x.roundToInt()

        for (i in 0..childCount) {
            val child: View = getChildAt(i) ?: return

            if (x > child.left && x < (child.left + child.width)) {

                val dragOnView = x - child.left
                val ratioCross = i + (dragOnView / child.width.toFloat())

                setReviewScore(ratioCross, true)
            }
        }
    }
}
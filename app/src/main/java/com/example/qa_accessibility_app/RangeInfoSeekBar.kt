package com.example.qa_accessibility_app

import android.content.Context
import android.util.AttributeSet
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.SeekBar

// TE-21951 — Invalid Range Values, XML variant.
// A SeekBar (real slider look) that reports an ARBITRARY RangeInfo — including
// invalid combinations (min >= max, current out of range) that a native SeekBar
// would clamp. The min/max/current are declared in XML via app:rangeMin /
// app:rangeMax / app:rangeCurrent. Overriding onInitializeAccessibilityNodeInfo
// injects those values into the a11y tree so the rule can evaluate them.
class RangeInfoSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SeekBar(context, attrs, defStyleAttr) {

    private var rangeMin = 0f
    private var rangeMax = 100f
    private var rangeCurrent = 50f
    private var rangeLabel: String? = null

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.RangeInfoSeekBar)
            rangeMin = a.getFloat(R.styleable.RangeInfoSeekBar_rangeMin, 0f)
            rangeMax = a.getFloat(R.styleable.RangeInfoSeekBar_rangeMax, 100f)
            rangeCurrent = a.getFloat(R.styleable.RangeInfoSeekBar_rangeCurrent, 50f)
            rangeLabel = a.getString(R.styleable.RangeInfoSeekBar_rangeLabel)
            a.recycle()
        }
        max = 100
        progress = 50
        rangeLabel?.let { contentDescription = it }
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        @Suppress("DEPRECATION")
        info.rangeInfo = AccessibilityNodeInfo.RangeInfo.obtain(
            AccessibilityNodeInfo.RangeInfo.RANGE_TYPE_FLOAT,
            rangeMin, rangeMax, rangeCurrent
        )
    }
}

package ru.gozerov.core.recycler

import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.smoothScrollToPositionWithCustomSpeed(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 80f / displayMetrics.densityDpi
        }
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}
package com.akz.cinema.ui.screen.home.carousel.uitls

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselSnapHelper

interface OnSnapPositionChangeListener {
    fun onSnapPositionChange(position: Int)
}

class OnSnapPositionChangeListenerImpl(
    private val onPositionChanged: (Int) -> Unit
) : OnSnapPositionChangeListener {
    override fun onSnapPositionChange(position: Int) {
        onPositionChanged(position)
    }
}
class SnapOnScrollListener(
    private val snapHelper: CarouselSnapHelper,
    var behavior: Behavior = Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
    var onSnapPositionChangeListener: OnSnapPositionChangeListener? = null
) : RecyclerView.OnScrollListener() {

    enum class Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (behavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
            && newState == RecyclerView.SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange(recyclerView)
        }
    }

    private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
        val snapPosition = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            onSnapPositionChangeListener?.onSnapPositionChange(snapPosition)
            this.snapPosition = snapPosition
        }
    }
}

fun CarouselSnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}
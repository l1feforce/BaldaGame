package gusev.spbstu.org.baldagame

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View


abstract class CustomGestureListener(private val mView: View) : GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        mView.onTouchEvent(e)
        return super.onSingleTapConfirmed(e)
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        onTouch()
        return false
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e1.x < e2.x) {
            return onSwipeRight()
        }

        return if (e1.x > e2.x) {
            onSwipeLeft()
        } else onTouch()

    }

    abstract fun onSwipeRight(): Boolean
    abstract fun onSwipeLeft(): Boolean
    abstract fun onTouch(): Boolean
}
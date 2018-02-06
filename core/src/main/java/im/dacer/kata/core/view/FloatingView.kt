package im.dacer.kata.core.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.WindowManager
import im.dacer.kata.core.R
import im.dacer.kata.core.util.SchemeHelper
import timber.log.Timber

class FloatingView(context: Context) : android.support.v7.widget.AppCompatImageView(context) {

    private val mWindowManager: WindowManager
    private val mMargin: Int
    private val mMarginY: Int
    private val mDismissTask = Runnable { dismiss() }
    private var isShow: Boolean = false
    var mText: String? = null


    init {
        setImageResource(R.drawable.floating_button)
        mMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
        mMarginY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180f, resources.displayMetrics).toInt()
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        setOnClickListener {
            mText?.run { SchemeHelper.startKata(getContext(), this, 0) }
            dismiss()
        }
    }

    fun show() {
        if (!isShow) {
            val w = WindowManager.LayoutParams.WRAP_CONTENT
            val h = WindowManager.LayoutParams.WRAP_CONTENT
            val flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

            val type: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                WindowManager.LayoutParams.TYPE_TOAST
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

            val layoutParams = WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT)
            layoutParams.gravity = Gravity.RIGHT or Gravity.BOTTOM
            layoutParams.x = mMargin
            layoutParams.y = mMarginY

            try {
                mWindowManager.addView(this, layoutParams)
            } catch (e: WindowManager.BadTokenException) {
                Timber.e(e)
            }

            isShow = true

            scaleX = 0f
            scaleY = 0f
            animate().cancel()
            animate().scaleY(1f).scaleX(1f).setDuration(ANIMATION_DURATION.toLong()).setListener(null).start()
        }

        removeCallbacks(mDismissTask)
        postDelayed(mDismissTask, 3000)
    }

    private fun dismiss() {
        if (isShow) {
            animate().cancel()
            animate().scaleX(0f).scaleY(0f).setDuration(ANIMATION_DURATION.toLong()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    try {
                        mWindowManager.removeView(this@FloatingView)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }

                    isShow = false
                }
            }).start()
        }
        removeCallbacks(mDismissTask)
    }

    companion object {

        private const val ANIMATION_DURATION = 500
    }
}

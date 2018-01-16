package im.dacer.kata.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.drawable.NinePatchDrawable
import android.R.attr.y
import android.R.attr.x
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import im.dacer.kata.R
import im.dacer.kata.core.util.ViewUtil
import timber.log.Timber


/**
 * Created by Dacer on 16/01/2018.
 * A Copy Popup
 */
class PopupView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val popupTextPaint = Paint()
    private var point: Point? = null
    private val popupStr: String by lazy { context.getString(R.string.copy) }
    private var popupRect: Rect? = null
    var listener: PopupListener? = null

    init {
        popupTextPaint.isAntiAlias = true
        popupTextPaint.color = Color.WHITE
        popupTextPaint.textSize = ViewUtil.sp2px(16f).toFloat()
        popupTextPaint.strokeWidth = 5f
        popupTextPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        point?.run { drawPopup(canvas, this) }
    }

    val popupIsShown: Boolean get() { return point != null }

    fun show(point: Point) {
        this.point = point
        postInvalidate()
    }

    fun hide() {
        this.point = null
        postInvalidate()
    }

    private fun drawPopup(canvas: Canvas, point: Point) {
        val numStr = popupStr
        val sidePadding = ViewUtil.dpToPx(8)
        val x = point.x
        val y = point.y
        val popupTextRect = Rect()
        popupTextPaint.getTextBounds(numStr, 0, numStr.length, popupTextRect)
        popupRect = Rect(
                x - popupTextRect.width() / 2 - sidePadding,
                y - popupTextRect.height() - bottomTriangleHeight - popupTopPadding * 2 - popupBottomMargin,
                x + popupTextRect.width() / 2 + sidePadding,
                y + popupTopPadding + popupBottomPadding)


        val popup = resources.getDrawable(R.drawable.popup_red) as NinePatchDrawable
        popup.bounds = popupRect
        popup.draw(canvas)
        canvas.drawText(numStr, x.toFloat(), (y - bottomTriangleHeight - popupBottomMargin).toFloat(), popupTextPaint)
    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            if (popupRect?.contains(event.x.toInt(), event.y.toInt()) == true) {
                listener?.popupClicked()
            }
            hide()
        }
        return popupIsShown
    }


    interface PopupListener {
        fun popupClicked()
    }

    companion object {
        private val bottomTriangleHeight = 12
        private val popupTopPadding = ViewUtil.dpToPx(2)
        private val popupBottomPadding = ViewUtil.dpToPx(2)
        private val popupBottomMargin = ViewUtil.dpToPx(5)
    }
}
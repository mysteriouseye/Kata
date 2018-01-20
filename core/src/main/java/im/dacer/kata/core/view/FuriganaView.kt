package im.dacer.kata.core.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.atilika.kuromoji.ipadic.Token
import im.dacer.kata.core.extension.toKanjiResult
import im.dacer.kata.core.util.ViewUtil
import im.dacer.kata.segment.model.KanjiResult
import im.dacer.kata.segment.util.KanaHelper


/**
 * Created by Dacer on 20/01/2018.
 */
class FuriganaView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var kanjiResult: KanjiResult? = null

    private val furiganaPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val normalPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var topMargin: Int = ViewUtil.dpToPx(2)
    private val furiganaHeight: Float get() = furiganaPaint.fontMetrics.let { it.bottom - it.top }
    private var furiganaBottomMargin: Int = ViewUtil.dpToPx(0)

    private val normalHeight: Float get() = normalPaint.fontMetrics.let { it.bottom - it.top }
    private var bottomMargin: Int = ViewUtil.dpToPx(2)

    private val furiganaWidth: Float get() = furiganaPaint.measureText(kanjiResult?.furigana ?: "")
    private val normalWidth: Float get() = normalPaint.measureText(kanjiResult?.surface ?: "")


    init {
        furiganaPaint.color = GRAY
        normalPaint.color = Color.BLACK

        furiganaPaint.textSize = 16f
        normalPaint.textSize = 22f

        furiganaPaint.textAlign = Paint.Align.CENTER
        normalPaint.textAlign = Paint.Align.CENTER

//        if (attrs != null) {
//            val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BigBangLayout)
//            val textSize = typedArray.getDimensionPixelSize(R.styleable.BigBangLayout_textSize, resources.getDimensionPixelSize(R.dimen.big_bang_default_text_size))
//            setTextSpSize(textSize)
//            typedArray.recycle()
//        }
    }

    fun setTextSpSize(sizeInSp: Float) {
        normalPaint.textSize = ViewUtil.sp2px(sizeInSp)
        requestLayout()
        invalidate()
    }

    fun setText(kanjiResult: KanjiResult) {
        this.kanjiResult = kanjiResult
    }

    fun setText(token: Token) {
        setText(token.toKanjiResult())
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        normalPaint.color = if (selected) RED else Color.BLACK
    }

    public override fun onDraw(canvas: Canvas) {
        if (kanjiResult == null) return
        val xPos = canvas.width / 2f

        if (KanaHelper.hasKanji(kanjiResult!!.surface)) {
            canvas.drawText(kanjiResult!!.furigana, xPos, (topMargin - furiganaPaint.fontMetrics.top), furiganaPaint)
        }
        canvas.drawText(kanjiResult!!.surface, xPos,
                (topMargin + furiganaHeight + furiganaBottomMargin - normalPaint.fontMetrics.top), normalPaint)
    }

    override fun onMeasure(width_ms: Int, height_ms: Int) {
        setMeasuredDimension(
                Math.max(furiganaWidth, normalWidth).toInt(),
                (topMargin + furiganaHeight + furiganaBottomMargin + normalHeight + bottomMargin).toInt())
    }

    companion object {
        val RED = Color.parseColor("#F44336")
        val GRAY = Color.parseColor("#424242")
    }
}
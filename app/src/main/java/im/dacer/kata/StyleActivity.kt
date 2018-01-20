package im.dacer.kata

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.core.model.BigBangStyle
import im.dacer.kata.segment.model.KanjiResult
import kotlinx.android.synthetic.main.activity_style.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class StyleActivity : AppCompatActivity() {

    private var multiprocessPref: MultiprocessPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style)

        multiprocessPref = MultiprocessPref(this)
        kataLayout.setKanjiResultData(EXAMPLE_KANJI_RESULT_LIST)

        textSizeSeekBar.setOnProgressChangeListener(object : SimpleListener() {
            override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
                kataLayout.itemTextSize = value.toFloat()
            }
        })

        lineSpaceSeekBar.setOnProgressChangeListener(object : SimpleListener() {
            override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
                kataLayout.lineSpace = value
            }
        })

        itemSpace.setOnProgressChangeListener(object : SimpleListener() {
            override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
                kataLayout.itemSpace = value
            }
        })


        textSizeSeekBar.progress = multiprocessPref!!.getItemTextSize()
        lineSpaceSeekBar.progress = multiprocessPref!!.getLineSpace()
        itemSpace.progress = multiprocessPref!!.getItemSpace()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        multiprocessPref?.setBigBangStyle(BigBangStyle(
                itemSpace.progress,
                lineSpaceSeekBar.progress,
                textSizeSeekBar.progress
        ))
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    internal abstract class SimpleListener : DiscreteSeekBar.OnProgressChangeListener {

        override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {}

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {}

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {}
    }

    companion object {
        val EXAMPLE_KANJI_RESULT_LIST = listOf(
                KanjiResult("日本国", furigana = "にほんこく"),
                KanjiResult("または"),
                KanjiResult("日本", furigana = "にほん"),
                KanjiResult("は"),
                KanjiResult("、"),
                KanjiResult("東アジア", furigana = "ひがしあじあ"),
                KanjiResult("に"),
                KanjiResult("位置", furigana = "いち"),
                KanjiResult("する"),
                KanjiResult("日本列島", furigana = "にほん"),
                KanjiResult("及び", furigana = "および"),
                KanjiResult("、"),
                KanjiResult("南西諸島", furigana = "なんせいしょとう"),
                KanjiResult("・"),
                KanjiResult("伊豆諸島", furigana = "いずしょとう"),
                KanjiResult("・"),
                KanjiResult("小笠原諸島", furigana = "おがさわらしょとう"),
                KanjiResult("など"),
                KanjiResult("から"),
                KanjiResult("成る", furigana = "なる"),
                KanjiResult("島国", furigana = "しまぐに"),
                KanjiResult("である"))

    }
}

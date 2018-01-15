package im.dacer.kata

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.core.model.BigBangStyle
import kotlinx.android.synthetic.main.activity_style.*

class StyleActivity : AppCompatActivity() {

    private var multiprocessPref: MultiprocessPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style)

        multiprocessPref = MultiprocessPref(this)

        val testStrings = arrayOf("日本国", "または", "日本", "は", "、", "東アジア", "に", "位置する", "日本列島", "及び", "、", "南西諸島", "・", "伊豆諸島", "・", "小笠原諸島", "など", "から", "成る", "島国", "である")
        for (testString in testStrings) {
            bigbangLayout.addTextItem(testString)
        }

        textSizeSeekBar.setOnProgressChangeListener(object : SimpleListener() {
            override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
                bigbangLayout.setItemTextSize(value)
            }
        })

        lineSpaceSeekBar.setOnProgressChangeListener(object : SimpleListener() {
            override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
                bigbangLayout.setLineSpace(value)
            }
        })

        itemSpace.setOnProgressChangeListener(object : SimpleListener() {
            override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
                bigbangLayout.setItemSpace(value)
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

        override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {

        }

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {

        }

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {

        }
    }
}

package im.dacer.kata.core

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import im.dacer.kata.segment.model.KanjiResult

import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_big_bang.*
import timber.log.Timber

class BigBangActivity : AppCompatActivity(), BigBangLayout.ActionListener, BigBangLayout.ItemClickListener {

    var kanjiResultList: Array<KanjiResult>? = null

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        bigbangLayout.reset()
        handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_bang)
        bigbangLayout.setActionListener(this)
        bigbangLayout.setItemClickListener(this)
        if (BigBang.getItemSpace() > 0) bigbangLayout.setItemSpace(BigBang.getItemSpace())
        if (BigBang.getLineSpace() > 0) bigbangLayout.setLineSpace(BigBang.getLineSpace())
        if (BigBang.getItemTextSize() > 0) bigbangLayout.setItemTextSize(BigBang.getItemTextSize())
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data
        if (data == null) {
            finish()
            return
        }
        val text = data.getQueryParameter(EXTRA_TEXT)

        if (TextUtils.isEmpty(text)) {
            finish()
            return
        }

        //todo add loading progressbar
        BigBang.getSegmentParserAsync()
                .flatMap { it.parse(text) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    bigbangLayout.reset()
                    kanjiResultList = it
                    for (str in it) {
                        bigbangLayout.addTextItem(str.surface)
                    }
                }, {
                    Timber.e(it)
                    Toast.makeText(this@BigBangActivity, it.message, Toast.LENGTH_SHORT).show()
                })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val selectedText = bigbangLayout.selectedText
        BigBang.startAction(this, BigBang.ACTION_BACK, selectedText)
    }

    override fun onItemClicked(index: Int) {
        titleTv.text = "${kanjiResultList?.get(index)?.surface} ${kanjiResultList?.get(index)?.furigana}"
        descTv.text = kanjiResultList?.get(index)?.meanings?.joinToString("\n")
    }

    override fun onSearch(text: String) {
        BigBang.startAction(this, BigBang.ACTION_SEARCH, text)
    }

    override fun onShare(text: String) {
        BigBang.startAction(this, BigBang.ACTION_SHARE, text)
    }

    override fun onCopy(text: String) {
        BigBang.startAction(this, BigBang.ACTION_COPY, text)
    }

    companion object {

        val EXTRA_TEXT = "extra_text"
    }

}

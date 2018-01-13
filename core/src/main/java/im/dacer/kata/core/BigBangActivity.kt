package im.dacer.kata.core

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.atilika.kuromoji.ipadic.Token
import im.dacer.kata.core.data.JMDictDbHelper
import im.dacer.kata.core.data.SearchHelper
import im.dacer.kata.core.extension.getSubtitle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_big_bang.*
import timber.log.Timber


class BigBangActivity : AppCompatActivity(), BigBangLayout.ActionListener, BigBangLayout.ItemClickListener {

    private var kanjiResultList: List<Token>? = null
    private var db: SQLiteDatabase? = null
    private var segmentDis: Disposable? = null
    private var searchHelper: SearchHelper? = null
    private var dictDisposable: Disposable? = null

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

        db = JMDictDbHelper(this).readableDatabase
        searchHelper = SearchHelper(db!!)

        segmentDis?.dispose()
        segmentDis = BigBang.getSegmentParserAsync()
                .flatMap { it.parse(text) }
                .flatMap { Observable.fromIterable(it) }
                .toList()
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

    override fun onDestroy() {
        super.onDestroy()
        db?.close()
        segmentDis?.dispose()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val selectedText = bigbangLayout.selectedText
        BigBang.startAction(this, BigBang.ACTION_BACK, selectedText)
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClicked(index: Int) {
        val result = kanjiResultList?.get(index)
        if (result?.isKnown == true) {
            furikanaTv.text = result.pronunciation
            titleTv.text = result.baseForm ?: result.surface
            descTv.text = result.getSubtitle()
            meaningTv.text = ""

            dictDisposable?.dispose()
            dictDisposable = Observable.fromCallable{ searchHelper!!.search(result.baseForm) }
                    .map { it.map { "* ${it.gloss()} - (${it.reading()})" }.joinToString("\n") }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ meaningTv.text = it }, { Timber.e(it) })

        } else {
            // todo
        }

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

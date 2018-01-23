package im.dacer.kata.core

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.atilika.kuromoji.ipadic.Token
import im.dacer.kata.SearchEngine
import im.dacer.kata.core.data.JMDictDbHelper
import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.core.data.SearchHelper
import im.dacer.kata.core.extension.getSubtitle
import im.dacer.kata.core.extension.strForSearch
import im.dacer.kata.core.view.KataLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_big_bang.*
import timber.log.Timber


class BigBangActivity : AppCompatActivity(), KataLayout.ItemClickListener {

    private var kanjiResultList: List<Token>? = null
    private var db: SQLiteDatabase? = null
    private var segmentDis: Disposable? = null
    private var searchHelper: SearchHelper? = null
    private var dictDisposable: Disposable? = null
    private var bigBang: BigBang? = null
    var currentSelectedToken: Token? = null

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_bang)
        bigBang = BigBang(this)
        val appPre = MultiprocessPref(this)

        kataLayout.itemSpace = appPre.getItemSpace()
        kataLayout.lineSpace = appPre.getLineSpace()
        kataLayout.itemTextSize = appPre.getItemTextSize().toFloat()
        kataLayout.itemClickListener = this
        kataLayout.showFurigana(!appPre.isHideFurigana())

        handleIntent(intent)
        val searchAction = SearchEngine.getSearchAction(this)
        searchBtn.setOnClickListener {
            currentSelectedToken?.run { searchAction.start(baseContext, this.strForSearch()) }
        }
        eyeBtn.setOnClickListener {
            val showFurigana = !kataLayout.showFurigana
            appPre.setHideFurigana(!showFurigana)
            kataLayout.showFurigana(showFurigana)
            refreshIconStatus()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshIconStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        db?.close()
        segmentDis?.dispose()
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClicked(index: Int) {
        currentSelectedToken = kanjiResultList?.get(index)
        val strForSearch: String

        if (currentSelectedToken?.isKnown == true) {
            descTv.text = "[${currentSelectedToken?.baseForm}] ${currentSelectedToken?.getSubtitle()}"
            meaningTv.text = ""
            strForSearch = currentSelectedToken!!.baseForm

        } else {
            descTv.text = currentSelectedToken?.surface
            strForSearch = currentSelectedToken?.surface ?: ""
        }

        dictDisposable?.dispose()
        dictDisposable = Observable.fromCallable{ searchHelper!!.search(strForSearch) }
                .map { it.joinToString("\n\n") { "· ${it.gloss()}" } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isBlank()) {
                        meaningTv.text = getString(R.string.not_found_error, currentSelectedToken?.surface)
                    } else {
                        meaningTv.text = it
                    }
                }, { Timber.e(it) })
    }

    private fun refreshIconStatus() {
        eyeBtn.text = if (kataLayout.showFurigana) "{gmd-visibility}" else "{gmd-visibility-off}"
    }

    private fun handleIntent(intent: Intent) {
        val text = intent.data.getQueryParameter(EXTRA_TEXT)
        if (text.isEmpty()) {
            finish()
            return
        }
        meaningScrollView.smoothScrollTo(0,0)
        bigBangScrollView.smoothScrollTo(0,0)
        db = JMDictDbHelper(this).readableDatabase
        searchHelper = SearchHelper(db!!)

        segmentDis?.dispose()
        segmentDis = BigBang.getSegmentParserAsync()
                .flatMap { it.parse(text) }
                .flatMap { Observable.fromIterable(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    kataLayout.reset()
                    resetTopLayout()
                    kanjiResultList = it
                    kataLayout.setTokenData(it)
                }, {
                    Timber.e(it)
//                    Toast.makeText(this@BigBangActivity, it.message, Toast.LENGTH_SHORT).show()
                })

    }

    private fun resetTopLayout() {
        descTv.text = ""
        meaningTv.text = ""
    }

    companion object {
        const val EXTRA_TEXT = "extra_text"
    }

}

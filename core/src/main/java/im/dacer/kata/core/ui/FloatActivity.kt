package im.dacer.kata.core.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import im.dacer.kata.core.BigBang
import im.dacer.kata.core.R
import im.dacer.kata.core.extension.findUrl
import im.dacer.kata.core.extension.timberAndToast
import im.dacer.kata.core.service.UrlAnalysisService
import im.dacer.kata.core.util.SchemeHelper
import im.dacer.kata.core.view.FloatingView
import im.dacer.kata.core.view.KataLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_float.*

/**
 * Created by Dacer on 31/01/2018.
 */
class FloatActivity : AppCompatActivity(), KataLayout.ItemClickListener {

    private var disposable: Disposable? = null
    private var sharedText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_float)

        applyStyle()
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onItemClicked(index: Int) {
        SchemeHelper.startKata(this, sharedText!!, index)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    @SuppressLint("InlinedApi")
    private fun handleIntent(intent: Intent) {
        var skipFloatBtn = false

        sharedText = intent.data?.getQueryParameter(BigBangActivity.EXTRA_TEXT)

        if (sharedText.isNullOrEmpty() && intent.action == Intent.ACTION_PROCESS_TEXT) {
            skipFloatBtn = true
            sharedText = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
        }

        if (sharedText.isNullOrEmpty() && intent.action == Intent.ACTION_SEND) {
            skipFloatBtn = true
            sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT)
            if (sharedText.isNullOrEmpty()) {
                sharedText = getIntent().getStringExtra(Intent.EXTRA_SUBJECT)
            }
        }

        if (sharedText.isNullOrEmpty()) {
            finish()
            return
        }

        sharedText!!.findUrl()?.run {
            startService(UrlAnalysisService.getIntent(this@FloatActivity, this))
            finish()
            return
        }

        if (sharedText!!.length > SchemeHelper.SHOW_FLOAT_MAX_TEXT_COUNT) {
            if (skipFloatBtn) {
                SchemeHelper.startKata(this, sharedText!!, 0)
            } else {
                val mFloatingView = FloatingView(this)
                mFloatingView.mText = sharedText
                mFloatingView.show()
            }
            finish()
            return
        }

        applyData()
    }


    private fun applyData() {
        disposable?.dispose()
        disposable = BigBang.getSegmentParserAsync()
                .flatMap { it.parse(sharedText!!) }
                .flatMap { Observable.fromIterable(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    kataLayout.reset()
                    kataLayout.setTokenData(it)
                }, { timberAndToast(it) })
    }

    private fun applyStyle() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val p = window.attributes
        p.y = -dm.heightPixels
        p.alpha = 0.9f

        kataLayout.itemSpace = 2
        kataLayout.lineSpace = 10
        kataLayout.itemTextSize = 22f
        kataLayout.itemClickListener = this
    }
}
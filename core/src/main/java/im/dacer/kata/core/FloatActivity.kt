package im.dacer.kata.core

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import im.dacer.kata.core.util.SchemeHelper
import im.dacer.kata.core.view.KataLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_float.*
import timber.log.Timber

/**
 * Created by Dacer on 31/01/2018.
 */
class FloatActivity : AppCompatActivity(), KataLayout.ItemClickListener {

    private var segmentDis: Disposable? = null
    private var text: String? = null

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
        SchemeHelper.startKata(this, text!!, index)
        finish()
    }

    private fun handleIntent(intent: Intent) {
        text = intent.data?.getQueryParameter(BigBangActivity.EXTRA_TEXT)

        if (text.isNullOrEmpty()) {
            if (intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT) != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
                }
            }
        }

        if (text.isNullOrEmpty()) {
            finish()
            return
        }

        segmentDis?.dispose()
        segmentDis = BigBang.getSegmentParserAsync()
                .flatMap { it.parse(text!!) }
                .flatMap { Observable.fromIterable(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    kataLayout.reset()
                    kataLayout.setTokenData(it)
                }, {
                    Timber.e(it)
//                    Toast.makeText(this@BigBangActivity, it.message, Toast.LENGTH_SHORT).show()
                })
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
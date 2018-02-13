package im.dacer.kata.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.baoyz.treasure.Treasure
import im.dacer.kata.Config
import im.dacer.kata.R
import im.dacer.kata.data.DictImporter
import im.dacer.kata.service.ListenClipboardService
import im.dacer.kata.widget.PopupView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Dacer on 13/02/2018.
 */
class MainPresenter(val context: Context, private val mainMvp: MainMvp) : PopupView.PopupListener {
    private var nothingHappenedCountdown: Disposable? = null
    private val treasure by lazy { Treasure.get(context, Config::class.java) }

    fun onStop() {
        nothingHappenedCountdown?.dispose()
    }

    fun importDictDb() {
        val dbImporter = DictImporter(context)
        if (!dbImporter.isDataBaseExists || !treasure.isDatabaseImported) {
            mainMvp.setBigbangTipTv(R.string.initializing_database)
            Observable.fromCallable{ dbImporter.importDataBaseFromAssets() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mainMvp.setBigbangTipTv(R.string.bigbang_hold_tip)
                        treasure.isDatabaseImported = true
                    }, { mainMvp.catchError(it) })
        }
    }
    fun restartListenService() {
        if (treasure.isListenClipboard) {
            ListenClipboardService.restart(context)
        }
    }

    override fun onPopupClicked() {
        val service = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        service.primaryClip = ClipData.newPlainText("", mainMvp.getClipTvText())
        Toast.makeText(context, context.getString(R.string.copied), Toast.LENGTH_SHORT).show()

        nothingHappenedCountdown = Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mainMvp.showNothingHappenedView()}
    }

}
package im.dacer.kata.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Canvas
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.baoyz.treasure.Treasure
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import im.dacer.kata.Config
import im.dacer.kata.R
import im.dacer.kata.core.data.HistoryDbHelper
import im.dacer.kata.core.data.HistoryHelper
import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.core.model.History
import im.dacer.kata.core.util.SchemeHelper
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
    private val appPref by lazy { MultiprocessPref(context) }
    private var refreshHistoryDis: Disposable? = null

    private val historyDbHelper by lazy { HistoryDbHelper(context) }
    private val db by lazy { historyDbHelper.readableDatabase }
    private var historyList: List<History>? = null
    val swipeListener = object: OnItemSwipeListener {
        override fun clearView(viewHolder: RecyclerView.ViewHolder?, pos: Int) {}

        override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            val history = historyList?.get(pos)
            if (history != null) {
                HistoryHelper.delete(db, history.id())
                Snackbar.make(mainMvp.getDecorView(), context.getString(R.string.deleted_sth, history.text()), Snackbar.LENGTH_SHORT)
                        .setAction(R.string.redo, {
                            HistoryHelper.save(db, history.text()!!)
                            refreshHistoryList()
                        })
                        .show()
            }
        }

        override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {}
        override fun onItemSwipeMoving(canvas: Canvas?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, isCurrentlyActive: Boolean) {}

    }

    fun onResume() {
        restartListenService()
        refreshHistoryList()
    }

    fun onStop() {
        nothingHappenedCountdown?.dispose()
        refreshHistoryDis?.dispose()
    }

    fun onDestroy() {
        db.close()
    }

    fun refreshHistoryList() {
        refreshHistoryDis?.dispose()
        if (appPref.tutorialFinished && treasure.cacheMax > 0) {
            refreshHistoryDis = Observable.fromCallable { HistoryHelper.get(db, treasure.cacheMax) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        historyList = it
                        if (historyList?.isNotEmpty() == true) {
                            mainMvp.showHistory(historyList!!)
                        }
                    }
        } else {
            mainMvp.showHistory(null)
        }
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

    fun onHistoryClicked(position: Int) {
        val history = historyList?.get(position)
        history?.run {
            SchemeHelper.startKata(context, this.text()!!)
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
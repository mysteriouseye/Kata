package im.dacer.kata

import android.app.Application
import android.util.Log

import im.dacer.kata.core.BigBang
import im.dacer.kata.core.action.CopyAction
import im.dacer.kata.core.action.ShareAction
import im.dacer.kata.service.ListenClipboardService
import io.reactivex.Observable
import timber.log.Timber

import com.baoyz.treasure.Treasure
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.Callable

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        BigBang.registerAction(BigBang.ACTION_SEARCH, SearchEngine.getSearchAction(this))
        BigBang.registerAction(BigBang.ACTION_COPY, CopyAction.create())
        BigBang.registerAction(BigBang.ACTION_SHARE, ShareAction.create())
        val config = Treasure.get(this, Config::class.java)
        BigBang.registerAction(BigBang.ACTION_BACK, if (config.isAutoCopy) CopyAction.create() else null)

        Observable.fromCallable { SegmentEngine.setup(this) }.subscribeOn(Schedulers.io()).subscribe()

        BigBang.setStyle(config.itemSpace, config.lineSpace, config.itemTextSize)

        if (config.isListenClipboard) {
            ListenClipboardService.start(this)
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            // todo use Fabric
        }
    }
}

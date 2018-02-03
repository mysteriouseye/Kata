package im.dacer.kata

import android.app.Application
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.baoyz.treasure.Treasure
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.leakcanary.LeakCanary
import im.dacer.kata.service.ListenClipboardService
import io.fabric.sdk.android.Fabric
import okhttp3.OkHttpClient
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        Fabric.with(this, Crashlytics())


        val okHttpClient = OkHttpClient().newBuilder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
        Stetho.initializeWithDefaults(this)

        val config = Treasure.get(this, Config::class.java)
//        BigBang.registerAction(BigBang.ACTION_BACK, if (config.isShowFloatDialog) CopyAction.create() else null)

        if (config.isListenClipboard) {
            ListenClipboardService.start(this)
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            Crashlytics.logException(t)
        }
    }
}

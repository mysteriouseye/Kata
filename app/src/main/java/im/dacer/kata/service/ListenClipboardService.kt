package im.dacer.kata.service

import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import im.dacer.kata.SegmentEngine
import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.core.extension.findUrl
import im.dacer.kata.core.util.SchemeHelper
import im.dacer.kata.core.view.FloatingView
import im.dacer.kata.segment.util.hasKanjiOrKana
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ListenClipboardService : Service() {

    private var mClipboardManager: ClipboardManager? = null
    private val mOnPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener { showAction() }
    private var mFloatingView: FloatingView? = null
    private val appPref: MultiprocessPref by lazy { MultiprocessPref(this) }

    private fun showAction() {
        val primaryClip = mClipboardManager!!.primaryClip
        if (primaryClip != null && primaryClip.itemCount > 0 && "BigBang" != primaryClip.description.label) {
            val text = primaryClip.getItemAt(0).coerceToText(this)
            if (text.isEmpty()) { return }

            if (appPref.useWebParser && text.toString().findUrl() != null) {
                SchemeHelper.startKataFloatDialog(this, text.toString())
                return
            }


            if (!text.toString().hasKanjiOrKana()) { return }

            if (!appPref.showFloatDialog || text.length > SchemeHelper.SHOW_FLOAT_MAX_TEXT_COUNT) {
                mFloatingView!!.mText = text.toString()
                mFloatingView!!.show()
            } else {
                SchemeHelper.startKataFloatDialog(this, text.toString())
            }
        }
    }

    override fun onCreate() {
        mClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mClipboardManager!!.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener)
        mFloatingView = FloatingView(this)

        Observable.fromCallable { SegmentEngine.setup(this) }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        mClipboardManager!!.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener)
    }

    override fun onBind(intent: Intent): IBinder? = null

    companion object {

        fun start(context: Context) {
            val serviceIntent = Intent(context, ListenClipboardService::class.java)
            context.startService(serviceIntent)
        }

        fun stop(context: Context) {
            val serviceIntent = Intent(context, ListenClipboardService::class.java)
            context.stopService(serviceIntent)
        }
    }

}
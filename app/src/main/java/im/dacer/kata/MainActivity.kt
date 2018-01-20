package im.dacer.kata

import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.baoyz.treasure.Treasure
import im.dacer.kata.data.DictImporter
import im.dacer.kata.service.ListenClipboardService
import im.dacer.kata.widget.PopupView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity(), PopupView.PopupListener {
    private val treasure by lazy { Treasure.get(this, Config::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popupView.listener = this
        clipTv.setOnLongClickListener {
            popupView.show(Point((clipTv.width / 2), clipTv.y.toInt()- bigbangTipTv.height))
            return@setOnLongClickListener true
        }
        val dbImporter = DictImporter(applicationContext)
        if (!dbImporter.isDataBaseExists) {
            bigbangTipTv.setText(R.string.initializing_database)
            Observable.fromCallable{ dbImporter.importDataBaseFromAssets() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ bigbangTipTv.setText(R.string.bigbang_hold_tip) }, { Timber.e(it) })
        }


    }

    override fun onResume() {
        super.onResume()
        if (treasure.isListenClipboard && !isMyServiceRunning(ListenClipboardService::class.java)) {
            ListenClipboardService.start(this)
        }
    }

    override fun popupClicked() {
        val service = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        service.primaryClip = ClipData.newPlainText("", clipTv.text.toString())
        Toast.makeText(this, getString(im.dacer.kata.core.R.string.copied), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE).any { serviceClass.name == it.service.className }
    }
}

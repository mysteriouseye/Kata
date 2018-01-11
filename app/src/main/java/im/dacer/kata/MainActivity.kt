package im.dacer.kata

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import im.dacer.kata.core.SchemeHelper
import im.dacer.kata.data.DBImporter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clipboardService = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val primaryClip = clipboardService.primaryClip
        if (primaryClip != null && primaryClip.itemCount > 0) {
            clipTv.text = primaryClip.getItemAt(0).text
        }

        clipTv.setOnLongClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, SchemeHelper.getUri(clipTv.text.toString())))
            true
        }

        val dbImporter = DBImporter(applicationContext)
        if (!dbImporter.isDataBaseExists) {
            bigbangTipTv.setText(R.string.initializing_database)
            Observable.fromCallable{ dbImporter.importDataBaseFromAssets() }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ bigbangTipTv.setText(R.string.bigbang_hold_tip) }, { Timber.e(it) })
        }
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
}

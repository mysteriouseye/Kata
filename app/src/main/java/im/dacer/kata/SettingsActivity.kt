package im.dacer.kata

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.baoyz.treasure.Treasure
import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.service.ListenClipboardService
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private var mConfig: Config? = null
    private var appPref: MultiprocessPref? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        appPref = MultiprocessPref(this)
        mConfig = Treasure.get(this, Config::class.java)

        searchEngine.setOnClickListener {
            AlertDialog.Builder(this@SettingsActivity).setItems(SearchEngine.getSupportSearchEngineList()) { _, which ->
                appPref!!.searchEngine = SearchEngine.getSupportSearchEngineList()[which]
                updateUI()
            }.show()
        }

        showFloatDialogSwit.setOnCheckedChangeListener { _, isChecked ->
            appPref!!.showFloatDialog = isChecked
            updateUI()
        }

        listenClipboardSwitch.setOnCheckedChangeListener { _, isChecked ->
            mConfig!!.isListenClipboard = isChecked
            if (mConfig!!.isListenClipboard) {
                ListenClipboardService.start(applicationContext)
            } else {
                ListenClipboardService.stop(applicationContext)
            }
            updateUI()
        }

        bigbangStyle.setOnClickListener { startActivity(Intent(this@SettingsActivity, StyleActivity::class.java)) }
        updateUI()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateUI() {
        searchEngineTv.text = appPref!!.searchEngine
        showFloatDialogSwit.isChecked = appPref!!.showFloatDialog
        listenClipboardSwitch.isChecked = mConfig!!.isListenClipboard
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

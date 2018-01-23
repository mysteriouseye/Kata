package im.dacer.kata

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.baoyz.treasure.Treasure
import im.dacer.kata.core.BigBang
import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.service.ListenClipboardService
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private var mConfig: Config? = null
    private var bigBang: BigBang? = null
    private var appPref: MultiprocessPref? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        bigBang = BigBang(this)
        appPref = MultiprocessPref(this)

        searchEngine.setOnClickListener {
            AlertDialog.Builder(this@SettingsActivity).setItems(SearchEngine.getSupportSearchEngineList()) { _, which ->
                appPref!!.setSearchEngine(SearchEngine.getSupportSearchEngineList()[which])
                bigBang!!.registerAction(BigBang.ACTION_SEARCH, SearchEngine.getSearchAction(applicationContext))
                updateUI()
            }.show()
        }

//        autoCopySwitch!!.setOnCheckedChangeListener { _, isChecked ->
//            mConfig!!.isAutoCopy = isChecked
//            BigBang.registerAction(BigBang.ACTION_BACK, if (mConfig!!.isAutoCopy) CopyAction.create() else null)
//            updateUI()
//        }

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
        mConfig = Treasure.get(this, Config::class.java)
        updateUI()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateUI() {
        searchEngineTv.text = appPref!!.getSearchEngine()
//        autoCopySwitch?.isChecked = mConfig!!.isAutoCopy
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

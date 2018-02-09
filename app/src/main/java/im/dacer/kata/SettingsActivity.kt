package im.dacer.kata

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.baoyz.treasure.Treasure
import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.core.util.LangUtils
import im.dacer.kata.core.util.WebParser
import im.dacer.kata.service.ListenClipboardService
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val mConfig by lazy { Treasure.get(this, Config::class.java) }
    private val appPref by lazy { MultiprocessPref(this) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        searchEngine.setOnClickListener {
            AlertDialog.Builder(this@SettingsActivity)
                    .setItems(SearchEngine.getSupportSearchEngineList()) { _, which ->
                appPref.searchEngine = SearchEngine.getSupportSearchEngineList()[which]
                updateUI()
            }.show()
        }

        enhancedModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            appPref.enhancedMode = isChecked
            refreshService()
            updateUI()
        }

        translationTarget.setOnClickListener {
            AlertDialog.Builder(this@SettingsActivity)
                    .setItems(LangUtils.LANG_LIST) { _, which ->
                appPref.targetLang = LangUtils.LANG_KEY_LIST[which]
                updateUI()
            }.show()
        }

        webPageParser.setOnClickListener {
            AlertDialog.Builder(this@SettingsActivity)
                    .setItems(WebParser.getParserNameArray(this)) { _, which ->
                appPref.webParser = WebParser.Parser.values()[which]
                updateUI()
            }.show()
        }

        showFloatDialogSwit.setOnCheckedChangeListener { _, isChecked ->
            appPref.showFloatDialog = isChecked
            updateUI()
        }

        listenClipboardSwitch.setOnCheckedChangeListener { _, isChecked ->
            mConfig.isListenClipboard = isChecked
            refreshService()
            updateUI()
        }

        bigbangStyle.setOnClickListener { startActivity(Intent(this@SettingsActivity, StyleActivity::class.java)) }
        updateUI()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun refreshService() {
        if (mConfig.isListenClipboard) {
            ListenClipboardService.restart(applicationContext)
        } else {
            ListenClipboardService.stop(applicationContext)
        }
    }

    private fun updateUI() {
        searchEngineTv.text = appPref.searchEngine
        showFloatDialogSwit.isChecked = appPref.showFloatDialog
        listenClipboardSwitch.isChecked = mConfig.isListenClipboard
        webPageParserTv.setText(appPref.webParser.stringRes)
        enhancedModeSwitch.isChecked = appPref.enhancedMode
        translationTargetTv.text = LangUtils.getLangByKey(appPref.targetLang)

        enhancedModeSwitch.isEnabled = listenClipboardSwitch.isChecked
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

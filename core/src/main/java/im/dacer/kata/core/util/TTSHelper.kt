package im.dacer.kata.core.util

import android.app.Activity
import android.content.Context
import android.speech.tts.TextToSpeech
import com.afollestad.materialdialogs.MaterialDialog
import im.dacer.kata.core.R
import java.util.*


/**
 * Created by Dacer on 01/02/2018.
 */
class TTSHelper(val appContext: Context) {
    private var available = false
    private val initListener = TextToSpeech.OnInitListener {
        if (it == TextToSpeech.SUCCESS) {
            val result = this.tts.setLanguage(Locale.JAPAN)
            if (result == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                available = true
            }
        }
    }
    private val tts: TextToSpeech = TextToSpeech(appContext, initListener)


    fun play(activity: Activity, string: String) {
        if (available) {
            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null)
        } else {
            MaterialDialog.Builder(activity)
                    .title(R.string.tts_error_title)
                    .content(R.string.tts_error_content)
                    .positiveText(android.R.string.ok)
                    .onPositive { _, _ -> }
                    .show()
        }
    }

    fun onDestroy() {
        tts.stop()
        tts.shutdown()
    }


}
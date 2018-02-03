package im.dacer.kata.core.util

import android.app.AlertDialog
import android.content.Context
import android.speech.tts.TextToSpeech
import im.dacer.kata.core.R
import java.util.*


/**
 * Created by Dacer on 01/02/2018.
 */
class TTSHelper(val context: Context) {
    private var available = false
    private val initListener = TextToSpeech.OnInitListener {
        if (it == TextToSpeech.SUCCESS) {
            val result = this.tts.setLanguage(Locale.JAPAN)
            if (result == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                available = true
            }
        }
    }
    private val tts: TextToSpeech = TextToSpeech(context, initListener)


    fun play(string: String) {
        if (available) {
            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null)
        } else {
            AlertDialog.Builder(context)
                    .setTitle(R.string.tts_error_title)
                    .setMessage(R.string.tts_error_content)
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show()
        }
    }

    fun onDestroy() {
        tts.stop()
        tts.shutdown()
    }


}
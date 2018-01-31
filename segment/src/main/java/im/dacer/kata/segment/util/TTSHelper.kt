package im.dacer.kata.segment.util

import android.media.AudioManager
import android.media.MediaPlayer

/**
 * Created by Dacer on 01/02/2018.
 */
class TTSHelper {
    private var mediaPlayer = MediaPlayer()

    init {
        @Suppress("DEPRECATION")
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }

    fun play(string: String) {
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(getTTSUrl(string))
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.prepareAsync()
    }

    fun onDestroy() {
        mediaPlayer.release()
    }


    companion object {
        fun getTTSUrl(string: String) = "https://translate.google.com/translate_tts?ie=UTF-8&q=$string&tl=ja&client=tw-ob"
    }
}
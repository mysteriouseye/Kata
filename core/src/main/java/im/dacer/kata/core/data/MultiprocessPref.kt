package im.dacer.kata.core.data

import android.content.Context
import im.dacer.kata.SearchEngine
import im.dacer.kata.core.model.BigBangStyle
import net.grandcentrix.tray.TrayPreferences

/**
 * Created by Dacer on 15/01/2018.
 */
class MultiprocessPref(context: Context): TrayPreferences(context, "Kata", 1) {


    fun getLineSpace(): Int = bigBangStyle.lineSpace

    fun getItemSpace(): Int = bigBangStyle.itemSpace

    fun getItemTextSize(): Int = bigBangStyle.textSize

    var bigBangStyle: BigBangStyle
        get() = BigBangStyle.getFrom(getString(BIG_BANG_STYLE, ""))
        set(value) { put(BIG_BANG_STYLE, value.toReadableString()) }

    var searchEngine: String
        get() = getString(SEARCH_ENGINE, SearchEngine.GOOGLE)!!
        set(value) { put(SEARCH_ENGINE, value) }


    var hideFurigana: Boolean
        get() = getBoolean(HIDE_FURIGANA, false)
        set(value) { put(HIDE_FURIGANA, value) }

    var showFloatDialog: Boolean
        get() = getBoolean(SHOW_FLOAT_DIALOG, false)
        set(value) { put(SHOW_FLOAT_DIALOG, value) }

    companion object {
        private const val BIG_BANG_STYLE = "pref_big_bang_style"
        private const val SEARCH_ENGINE = "pref_search_engine"
        private const val HIDE_FURIGANA = "pref_hide_furigana"
        private const val SHOW_FLOAT_DIALOG = "pref_show_float_dialog"
    }
}
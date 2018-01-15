package im.dacer.kata.core.data

import android.content.Context
import im.dacer.kata.SearchEngine
import im.dacer.kata.core.model.BigBangStyle
import net.grandcentrix.tray.TrayPreferences

/**
 * Created by Dacer on 15/01/2018.
 */
class MultiprocessPref(context: Context): TrayPreferences(context, "Kata", 1) {


    fun getLineSpace(): Int = getBigBangStyle().lineSpace

    fun getItemSpace(): Int = getBigBangStyle().itemSpace

    fun getItemTextSize(): Int = getBigBangStyle().textSize

    fun getBigBangStyle(): BigBangStyle{
        return BigBangStyle.getFrom(getString(BIG_BANG_STYLE, ""))
    }

    fun setBigBangStyle(bigBangStyle: BigBangStyle) {
        put(BIG_BANG_STYLE, bigBangStyle.toReadableString())
    }

    fun getSearchEngine(): String {
        return getString(SEARCH_ENGINE, SearchEngine.GOOGLE)!!
    }

    fun setSearchEngine(string: String) {
        put(SEARCH_ENGINE, string)
    }

    companion object {
        private const val BIG_BANG_STYLE = "pref_big_bang_style"
        private const val SEARCH_ENGINE = "pref_search_engine"
    }
}
package im.dacer.kata.core.data

import android.database.sqlite.SQLiteDatabase
import im.dacer.kata.core.model.DictEntry
import timber.log.Timber

/**
 * Created by Dacer on 12/01/2018.
 */
class SearchHelper(private val db: SQLiteDatabase) {

    fun search(text: String): List<DictEntry> {
        Timber.e("search $text")
        return if (kanjiInside(text)) {
            searchKanji(text)
        } else {
            searchReading(text)
        }
    }


    private fun searchKanji(kanji: String): List<DictEntry> {
        val result = arrayListOf<DictEntry>()
        val query = DictEntry.FACTORY.search_kanji(kanji)
        db.rawQuery(query.statement, query.args).use { cursor ->
            while (cursor?.moveToNext() == true) {
                result.add(DictEntry.SELECT_ALL_MAPPER.map(cursor))
            }
        }
        return result
    }

    private fun searchReading(reading: String): List<DictEntry> {
        val result = arrayListOf<DictEntry>()
        val query = DictEntry.FACTORY.search_reading(reading)
        db.rawQuery(query.statement, query.args).use { cursor ->
            while (cursor?.moveToNext() == true) {
                result.add(DictEntry.SELECT_ALL_MAPPER.map(cursor))
            }
        }
        return result
    }

    private fun kanjiInside(text: String): Boolean {
        return text.matches(Regex(".*[\\u4e00-\\u9faf]+.*"))
    }
}
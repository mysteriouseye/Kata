package im.dacer.kata.core.data

import android.database.sqlite.SQLiteDatabase
import im.dacer.kata.core.model.DictEntry
import im.dacer.kata.core.model.DictKanji
import im.dacer.kata.core.model.DictReading

/**
 * Created by Dacer on 12/01/2018.
 */
class SearchHelper(private val db: SQLiteDatabase) {

    //todo deal with meanning with comma
    fun search(text: String): List<DictEntry> {
        val idInEntryList = if (kanjiInside(text)) {
            searchKanji(text)
        } else {
            searchReading(text)
        }
        return idInEntryList.mapNotNull { it?.let { it1 -> searchEntry(it1) } }
    }

    private fun searchKanji(kanji: String): List<Long?> {
        val result = arrayListOf<Long?>()
        val query = DictKanji.FACTORY.search(kanji)
        db.rawQuery(query.statement, query.args).use { cursor ->
            while (cursor?.moveToNext() == true) {
                result.add(DictKanji.SELECT_ALL_MAPPER.map(cursor).id_in_entry())
            }
        }
        return result
    }

    private fun searchReading(reading: String): List<Long?> {
        val result = arrayListOf<Long?>()
        val query = DictReading.FACTORY.search(reading)
        db.rawQuery(query.statement, query.args).use { cursor ->
            while (cursor?.moveToNext() == true) {
                result.add(DictReading.SELECT_ALL_MAPPER.map(cursor).id_in_entry())
            }
        }
        return result
    }

    private fun searchEntry(id: Long): DictEntry? {
        val query = DictEntry.FACTORY.search(id)
        db.rawQuery(query.statement, query.args).use { cursor ->
            while (cursor?.moveToNext() == true) {
                return DictEntry.SELECT_ALL_MAPPER.map(cursor)
            }
        }
        return null
    }



    private fun kanjiInside(text: String): Boolean {
        return text.matches(Regex(".*[\\u4e00-\\u9faf]+.*"))
    }
}
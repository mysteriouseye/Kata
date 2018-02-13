package im.dacer.kata.core.data

import android.database.sqlite.SQLiteDatabase
import im.dacer.kata.core.model.History
import im.dacer.kata.core.model.HistoryModel

/**
 * Created by Dacer on 13/02/2018.
 */
class HistoryHelper {

    companion object {

        fun save(db: SQLiteDatabase, text: String) {
            val insertRow = HistoryModel.Insert_row(db)
            insertRow.bind(text)
            try {
                insertRow.program.executeInsert()
            } catch (e: Exception) {
                throw e
            }
        }

        fun delete(db: SQLiteDatabase, id: Long) {
            val deleteRow = HistoryModel.Delete_row(db)
            deleteRow.bind(id)
            try {
                deleteRow.program.executeUpdateDelete()
            } catch (e: Exception) {
                throw e
            }
        }

        fun get(db: SQLiteDatabase, limit: Int): List<History> {
            val result = arrayListOf<History>()
            val query = History.FACTORY.select_limit(limit.toLong())
            db.rawQuery(query.statement, query.args).use { cursor ->
                while (cursor?.moveToNext() == true) {
                    result.add(History.SELECT_ALL_MAPPER.map(cursor))
                }
            }
            return result
        }
    }

}
package im.dacer.kata.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Created by Dacer on 10/01/2018.
 * import JMDict.sqlite3
 */

class DBImporter(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val dbFile = context.getDatabasePath(DB_NAME)

    val isDataBaseExists: Boolean
        get() = dbFile.exists()

    /**
     * return false if db is existed
     */
    fun importDataBaseFromAssets(): Boolean {
        if (isDataBaseExists) return false
        val myInput = getFileFromZip(context.assets.open(ASSET_DB_FILE_NAME))

        val myOutput = FileOutputStream(dbFile)
        val buffer = ByteArray(1024)
        var length: Int = myInput.read(buffer)
        while (length > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
        return true
    }

    @Throws(IOException::class)
    private fun getFileFromZip(zipFileStream: InputStream): ZipInputStream {
        val zis = ZipInputStream(zipFileStream)
        val ze = zis.nextEntry
        if (ze != null) {
            Timber.i("extracting file: ${ze.name}")
        }
        return zis
    }

    override fun onCreate(arg0: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        val DB_NAME = "JMDict"
        val ASSET_DB_FILE_NAME = "$DB_NAME.sqlite3.zip"
        val DB_VERSION = 1
    }
}
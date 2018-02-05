package im.dacer.kata.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import im.dacer.kata.core.ui.BigBangActivity
import java.net.URLEncoder

/**
 * Created by Dacer on 09/01/2018.
 */
class SchemeHelper {
    companion object {
        const val SHOW_FLOAT_MAX_TEXT_COUNT = 12

        fun startKata(c: Context, text: String, preselectedIndex: Int = -1) {
            val intent = Intent(Intent.ACTION_VIEW, getUri(text, preselectedIndex))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            c.startActivity(intent)
        }

        fun startKataFloatDialog(c: Context, text: String) {
            val intent = Intent(Intent.ACTION_VIEW, getFloatUri(text))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            c.startActivity(intent)
        }

        private fun getUri(text: String, preselectedIndex: Int = 0) =
                Uri.parse("kata://?" +
                        "${BigBangActivity.EXTRA_TEXT}=${URLEncoder.encode(text, "utf-8")}" +
                        "&${BigBangActivity.PRESELECTED_INDEX}=$preselectedIndex")
        private fun getFloatUri(text: String) =
                Uri.parse("kata-float://?${BigBangActivity.EXTRA_TEXT}=${URLEncoder.encode(text, "utf-8")}")
    }
}
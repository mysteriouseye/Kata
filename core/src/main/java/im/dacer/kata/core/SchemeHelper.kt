package im.dacer.kata.core

import android.net.Uri
import java.net.URLEncoder

/**
 * Created by Dacer on 09/01/2018.
 */
class SchemeHelper {
    companion object {
        fun getUri(text: String) =
                Uri.parse("bigBang://?extra_text=${URLEncoder.encode(text, "utf-8")}")
    }
}
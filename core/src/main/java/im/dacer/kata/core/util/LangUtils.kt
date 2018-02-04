package im.dacer.kata.core.util

import com.rx2androidnetworking.Rx2AndroidNetworking
import im.dacer.kata.core.data.MultiprocessPref
import im.dacer.kata.core.extension.urlEncode
import im.dacer.kata.core.model.DictEntry
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.util.*

/**
 * Created by Dacer on 03/02/2018.
 * https://ctrlq.org/code/19909-google-translate-apicc
 * Thanks Google
 */
class LangUtils(private val appPre: MultiprocessPref) {

    val shouldTranslation get() = appPre.targetLang != DEFAULT_TARGET_LANG_KEY

    fun fetchTranslation(dictEntry: DictEntry): Observable<String> {
        val targetLang = appPre.targetLang

        if (targetLang == DEFAULT_TARGET_LANG_KEY) {
            return Observable.just(dictEntry.gloss())
        }

        //since people in China cannot use Google..
//        if (targetLang == "zh-CN") {
//            return Observable.just(dictEntry.gloss_cn())
//        }

        return Rx2AndroidNetworking.get(getTranslationUrl(targetLang, dictEntry.gloss()!!))
                .build()
                .stringObservable
                .map {
                    val array = JSONArray(it)
                    val result = array.getJSONArray(0).getJSONArray(0).getString(0)
                    return@map result
                            .replace("，", ", ")
                            .split(", ")
                            .distinct()
                            .joinToString(", ")
                }
                .subscribeOn(Schedulers.io())
    }

    companion object {
        fun getTranslationUrl(target: String, source: String) =
                "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=$target&dt=t&q=${source.urlEncode()}"

        fun isZhCN(): Boolean = Locale.getDefault().language == "zh" && Locale.getDefault().country == "CN"

        fun getLangByKey(key: String): String {
            val index = LANG_KEY_LIST.toList().indexOf(key)
            return if (index == -1 || index > LANG_LIST.size) {
                "English"
            } else {
                LANG_LIST[index]
            }
        }

        const val DEFAULT_TARGET_LANG_KEY = "en"
        val LANG_LIST = arrayOf("English", "Afrikaans", "Albanian", "Arabic", "Azerbaijani", "Basque", "Belarusian", "Bengali", "Bulgarian", "Catalan", "Chinese Simplified", "Chinese Traditional ", "Croatian", "Czech", "Danish", "Dutch", "Esperanto", "Estonian", "Filipino", "Finnish", "French", "Galician", "Georgian", "German", "Greek", "Gujarati", "Haitian Creole", "Hebrew", "Hindi", "Hungarian", "Icelandic", "Indonesian", "Irish", "Italian", "Japanese", "Kannada", "Korean", "Latin", "Latvian", "Lithuanian", "Macedonian", "Malay", "Maltese", "Norwegian", "Persian", "Polish", "Portuguese", "Romanian", "Russian", "Serbian", "Slovak", "Slovenian", "Spanish", "Swahili", "Swedish", "Tamil", "Telugu", "Thai", "Turkish", "Ukrainian", "Urdu", "Vietnamese", "Welsh", "Yiddish")
        val LANG_KEY_LIST = arrayOf("en", "af", "sq", "ar", "az", "eu", "be", "bn", "bg", "ca", "zh-CN", "zh-TW", "hr", "cs", "da", "nl", "eo", "et", "tl", "fi", "fr", "gl", "ka", "de", "el", "gu", "ht", "iw", "hi", "hu", "is", "id", "ga", "it", "ja", "kn", "ko", "la", "lv", "lt", "mk", "ms", "mt", "no", "fa", "pl", "pt", "ro", "ru", "sr", "sk", "sl", "es", "sw", "sv", "ta", "te", "th", "tr", "uk", "ur", "vi", "cy", "yi")
    }
}
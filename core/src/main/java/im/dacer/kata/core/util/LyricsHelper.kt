package im.dacer.kata.core.util

import com.rx2androidnetworking.Rx2AndroidNetworking
import im.dacer.kata.core.model.MusicSearchResult
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dacer on 26/02/2018.
 */
object LyricsHelper {
    private val BASE_URL = "http://music.dacer.im"
    private val SEARCH_URL = "$BASE_URL/search"
    private val LYRIC_URL = "$BASE_URL/lyric"


    fun search(keywords: String): Observable<MusicSearchResult.Result> {
        return Rx2AndroidNetworking.get(SEARCH_URL)
                .addQueryParameter("type", "1")
                .addQueryParameter("keywords", keywords)
                .build()
                .getObjectObservable(MusicSearchResult::class.java)
                .map { return@map it.result }
                .subscribeOn(Schedulers.io())
    }

    fun getLyric(id: Long): Observable<String> {
        return Rx2AndroidNetworking.get(LYRIC_URL)
                .addQueryParameter("id", id.toString())
                .build()
                .jsonObjectObservable
                .map { return@map it.getJSONObject("lrc")
                        .getString("lyric")
                        .replace(Regex(pattern = "\\[\\d\\d:\\d\\d.\\d+]"), "")}
                .subscribeOn(Schedulers.io())
    }
}
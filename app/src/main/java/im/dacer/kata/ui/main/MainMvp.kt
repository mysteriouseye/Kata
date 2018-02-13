package im.dacer.kata.ui.main

/**
 * Created by Dacer on 13/02/2018.
 */
interface MainMvp {
    fun setBigbangTipTv(strId: Int)
    fun catchError(throwable: Throwable)
    fun showNothingHappenedView()
    fun getClipTvText(): String
}
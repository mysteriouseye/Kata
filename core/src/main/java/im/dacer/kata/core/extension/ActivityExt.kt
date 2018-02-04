package im.dacer.kata.core.extension

import android.app.Activity
import android.widget.Toast
import timber.log.Timber

/**
 * Created by Dacer on 04/02/2018.
 */
fun Activity.toast(string: String?) {
    if (string == null) return
    runOnUiThread { Toast.makeText(this, string, Toast.LENGTH_LONG).show() }
}

fun Activity.timberAndToast(throwable: Throwable) {
    Timber.e(throwable)
    toast(throwable.message)
}
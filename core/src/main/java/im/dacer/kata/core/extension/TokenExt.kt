package im.dacer.kata.core.extension

import com.atilika.kuromoji.ipadic.Token

/**
 * Created by Dacer on 13/01/2018.
 */

 fun Token.getSubtitle(): String {
    val list = arrayListOf<String>()
    conjugationType.addToList(list)
    partOfSpeechLevel2.addToList(list)
    partOfSpeechLevel3.addToList(list)
    partOfSpeechLevel4.addToList(list)

    if (list.isEmpty()) {
        return partOfSpeechLevel1
    }
    return "$partOfSpeechLevel1 (${list.joinToString(", ")})"
}

private fun String.addToList(list: ArrayList<String>) {
    if (!isAsteriskOrEmpty()) list.add(this)
}

private fun String.isAsteriskOrEmpty(): Boolean {
    return this.isBlank() || this.replace(" ", "") == "*"
}

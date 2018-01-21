package im.dacer.kata.segment.model

import im.dacer.kata.segment.util.isKana

/**
 * Created by Dacer on 10/01/2018.
 */
data class KanjiResult(val surface: String, val baseForm: String = "", val furigana: String = "") {

    /**
     * remove the exist kana in the surface for furigana
     * e.g
     * 　　　　うちあげ　　 うちあ
     *      　打ち上げ　→　打ち上げ
     */
    val furiganaForDisplay: String
    val furiganaDisplayOffset: Float

    init {
        var result = furigana
        var startCount = 0
        var endCount = 0

        surface.toCharArray()
                .takeWhile { it.isKana() }
                .forEach { startCount+=1 }

        result = result.replaceRange(0, startCount, "")

        if (result.isNotEmpty()) {
            surface.toCharArray().reversed()
                    .takeWhile { it.isKana() }
                    .forEach { endCount+=1 }

            result = result.replaceRange(result.length - endCount, result.length, "")

        }

        var offset = startCount - endCount.toFloat()
        if (offset > 0) {
            offset -= 0.4f
        } else if (offset < 0) {
            offset += 0.4f
        }
        furiganaDisplayOffset = offset
        furiganaForDisplay = result
    }
}
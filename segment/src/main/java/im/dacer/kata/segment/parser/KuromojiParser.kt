package im.dacer.kata.segment.parser

import com.atilika.kuromoji.TokenizerBase
import com.atilika.kuromoji.ipadic.Tokenizer

import im.dacer.kata.segment.SimpleParser
import im.dacer.kata.segment.model.KanjiResult

/**
 * Created by Dacer on 09/01/2018.
 */

class KuromojiParser : SimpleParser() {

    private val tokenizer = Tokenizer.Builder().mode(TokenizerBase.Mode.NORMAL).build()

    override fun parseSync(text: String): List<KanjiResult> {
        val tokens = tokenizer.tokenize(text)
        return tokens.map {
            KanjiResult(it.surface, it.baseForm, it.pronunciation, emptyList())
        }
    }
}

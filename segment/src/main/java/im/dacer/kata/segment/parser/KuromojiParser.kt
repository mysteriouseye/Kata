package im.dacer.kata.segment.parser

import com.atilika.kuromoji.ipadic.Tokenizer

import im.dacer.kata.segment.SimpleParser

/**
 * Created by Dacer on 09/01/2018.
 */

class KuromojiParser : SimpleParser() {

    private val tokenizer = Tokenizer()

    override fun parseSync(text: String): Array<String> {
        val tokens = tokenizer.tokenize(text)
        return tokens.map { it.surface }.toTypedArray()
    }
}

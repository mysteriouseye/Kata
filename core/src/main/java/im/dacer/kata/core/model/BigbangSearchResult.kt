package im.dacer.kata.core.model

/**
 * Created by Dacer on 01/03/2018.
 * dictReadingList is needed ONLY when the word for searching is kanji
 */

data class BigbangSearchResult(val dictEntryList: List<DictEntry>, val dictReadingList: List<DictReading>?)
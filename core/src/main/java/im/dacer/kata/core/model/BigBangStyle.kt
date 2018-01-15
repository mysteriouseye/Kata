package im.dacer.kata.core.model

/**
 * Created by Dacer on 15/01/2018.
 */
data class BigBangStyle(val itemSpace: Int, val lineSpace: Int, val textSize: Int) {

    fun toReadableString(): String {
        return "$itemSpace,$lineSpace,$textSize"
    }

    companion object {
        fun getDefault() :BigBangStyle = BigBangStyle(10, 0, 15)

        fun getFrom(str: String?) :BigBangStyle {
            if (str == null) return getDefault()
            return try {
                val params = str.split(",")
                BigBangStyle(params[0].toInt(), params[1].toInt(), params[2].toInt())
            } catch (e: Exception) {
                getDefault()
            }
        }
    }
}
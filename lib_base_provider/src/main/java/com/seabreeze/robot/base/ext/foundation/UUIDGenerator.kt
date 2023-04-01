package com.seabreeze.robot.base.ext.foundation

import java.util.*

/**
 * User: milan
 * Time: 2019/3/27 2:12
 * Des:
 */
object UUIDGenerator {
    val uuid: String
        get() = UUID.randomUUID().toString()

    //获得指定数量的UUID
    fun getUUID(number: Int): Array<String?>? {
        if (number < 1) {
            return null
        }
        val array = arrayOfNulls<String>(number)
        for (i in 0 until number) {
            array[i] = uuid
        }
        return array
    }
}

object LetterGenerator {

    private val ALL_LETTER = lazy {
        arrayOf(
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z"
        )
    }.value


    fun allLetter(size: Int): List<String> {
        if (size <= 0) {
            return mutableListOf()
        }
        val results = mutableListOf<String>()
        for (i in 0..size) {
            results.add(inLetter(i))
        }
        return results
    }

    fun inLetter(index: Int): String {
        if (index >= ALL_LETTER.size) {
            val sb = StringBuilder()
            val remainder = index / ALL_LETTER.size - 1
            val aliquot = index % ALL_LETTER.size
            for (i in 0..remainder) {
                val value = ALL_LETTER[i]
                sb.append(value)
            }
            val inLetter = inLetter(aliquot)
            return sb.append(inLetter).toString()
        }
        return ALL_LETTER[index]
    }

}
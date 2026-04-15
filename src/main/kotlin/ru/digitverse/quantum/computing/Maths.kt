package ru.digitverse.quantum.computing

import kotlin.math.pow

/**
 * The constant that represent error in evaluations.
 */
const val EPSILON = 10e-10

/**
 * @receiver A number.
 * @param digitNum The digit count to represent in binary.
 * @return Binary representation of the number.
 */
fun Number.toBinary(digitNum: Int): String {
    return toInt().toString(2).padStart(digitNum, '0')
}

/**
 * @receiver A number.
 * @param size The size of the result array.
 * @return Binary array representing the number.
 */
fun Number.toBinaryArray(size: Int): IntArray {
    var tempX = this.toInt()
    val arr = IntArray(size)
    for (i in size - 1 downTo 0) {
        arr[i] = tempX % 2
        tempX /= 2
    }
    return arr
}

/**
 * @receiver A binary array.
 * @return A decimal integer represented by the array.
 */
fun IntArray.toInt(): Int {
    var r = 0
    for (i in indices) {
        r += (this[size - 1 - i] * 2.0.pow(i.toDouble())).toInt()
    }
    return r
}
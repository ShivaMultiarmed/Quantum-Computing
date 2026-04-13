package ru.digitverse.quantum.computing

import kotlin.math.pow

fun intToBinaryString(x: Int, digitNum: Int): String {
    return Integer.toBinaryString(x).padStart(digitNum, '0')
}

fun doubleToBinaryString(x: Double, digitNum: Int): String {
    return intToBinaryString(x.toInt(), digitNum)
}

fun intToBinaryArray(x: Int, num: Int): IntArray {
    var tempX = x
    val arr = IntArray(num)
    for (i in num - 1 downTo 0) {
        arr[i] = tempX % 2
        tempX /= 2
    }
    return arr
}

fun binaryArrayToInt(a: IntArray): Int {
    var r = 0
    for (i in a.indices) {
        r += (a[a.size - 1 - i] * 2.0.pow(i.toDouble())).toInt()
    }
    return r
}
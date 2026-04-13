package ru.digitverse.quantum.computing

import kotlin.math.log

operator fun Matrix.unaryPlus() = this

operator fun Matrix.unaryMinus() = Matrix(
    coefficient = -coefficient,
    matrix = matrix
)

operator fun Double.times(other: Matrix): Matrix {
    return Matrix(
        coefficient = this * other.coefficient,
        matrix = other.matrix
    )
}

operator fun Matrix.times(other: Matrix): Matrix = Matrix(
    coefficient = coefficient * other.coefficient,
    matrix = matrix * other.matrix
)

operator fun Array<DoubleArray>.times(other: Array<DoubleArray>): Array<DoubleArray> {
    val rowsA = size
    val colsA = this[0].size
    val rowsB = other.size
    val colsB = other[0].size
    require(colsA == rowsB) {
        "Number of columns in the first matrix must match the number of the rows in the second one. Now they are $colsA and $colsB respectively."
    }
    return Array(rowsA) { i ->
        DoubleArray(colsB) { j ->
            var item = 0.0
            for (k in 0 until colsA) {
                item += this[i][k] * other[k][j]
            }
            item
        }
    }
}

fun Matrix.tensor(other: Matrix): Matrix = Matrix(
    coefficient = coefficient * other.coefficient,
    matrix = matrix.tensor(other.matrix)
)

fun Array<DoubleArray>.tensor(other: Array<DoubleArray>): Array<DoubleArray> {
    val rowsA = size
    val colsA = this[0].size
    val rowsB = other.size
    val colsB = other[0].size
    val result = Array(rowsA * rowsB) {
        DoubleArray(colsA * colsB)
    }
    for (i in 0 until rowsA) {
        for (j in 0 until colsA) {
            for (x in 0 until rowsB) {
                for (y in 0 until colsB) {
                    result[i * rowsB + x][j * colsB + y] = this[i][j] * other[x][y]
                }
            }
        }
    }
    return result
}

fun Int.getQubitDigitCount() = log(toDouble(), 2.0) + 1
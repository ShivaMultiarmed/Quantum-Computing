package ru.digitverse.quantum.computing

import kotlin.math.abs
import kotlin.math.log
import kotlin.math.sqrt

/**
 * Base for quantum functions, [Vector]s, [Register]s and [Qubit]s.
 *
 * @author Mikhail Shell
 * @since 1.0.0
 * @property coefficient A coefficient before a matrix
 * @property matrix A 2D array representing a matrix
 */

open class Matrix(
    var coefficient: Double,
    var matrix: Array<DoubleArray>
) {
    override fun equals(other: Any?): Boolean { // TODO: normalize on check
        if (this === other) return true
        if (other !is Matrix) return false
        if (abs(coefficient - other.coefficient) > EPSILON) return false
        if (matrix.size != other.matrix.size) return false
        for (i in matrix.indices) {
            if (matrix[i].size != other.matrix[i].size) return false
            for (j in matrix[i].indices) {
                if (abs(matrix[i][j] - other.matrix[i][j]) > EPSILON) {
                    return false
                }
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = coefficient.hashCode()
        result = 31 * result + matrix.contentDeepHashCode()
        return result
    }

    override fun toString(): String {
        return buildString {
            append(coefficient)
            append(" x \n")
            for (row in matrix) {
                for (cell in row) {
                    append(cell)
                    append("\t")
                }
                append("\n")
            }
        }
    }

    /**
     * Factory methods for standard [Matrix]es.
     */
    companion object Factory {
        /**
         * @return A 2x2 NOT-[Matrix].
        */
        fun not(): Matrix = Matrix(
            coefficient = 1.0,
            matrix = arrayOf(
                doubleArrayOf(0.0, 1.0),
                doubleArrayOf(1.0, 0.0)
            )
        )

        /**
         * @param n Side length of the returned [Matrix].
         * @return An identity [Matrix] of n x n dimensions.
         */
        fun identity(n: Int = 2): Matrix = Matrix(
            coefficient = 1.0,
            matrix = Array(n) { i ->
                DoubleArray(n) { j ->
                    if (i == j) 1.0 else 0.0
                }
            }
        )

        /**
         * @param n Side length of the returned [Matrix].
         * @return A [Matrix] of n x n dimensions filled with zeros.
         */
        fun zero(n: Int = 2): Matrix = Matrix(
            coefficient = 1.0,
            matrix = Array(n) {
                DoubleArray(n)
            }
        )

        /**
         * @return A Hadamard-transform [Matrix] of 2 x 2 dimensions.
         */
        fun hadamard(): Matrix = Matrix(
            coefficient = 1 / sqrt(2.0),
            matrix = arrayOf(
                doubleArrayOf(1.0, 1.0),
                doubleArrayOf(1.0, -1.0)
            )
        )
    }
}

/**
 * @receiver A number.
 * @return The count of digits of the decimal number in binary representation.
 */

fun Int.getQubitDigitCount() = log(toDouble(), 2.0).toInt() + 1
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

/**
 * @receiver [Matrix] A
 * @param other [Matrix] B
 * @return A [Matrix] - tensor product of two [Matrix]es.
 */

fun Matrix.tensor(other: Matrix): Matrix = Matrix(
    coefficient = coefficient * other.coefficient,
    matrix = matrix.tensor(other.matrix)
)

/**
 * @receiver A 2D array.
 * @param other 2D array
 * @return A 2D - tensor product of two 2D arrays.
 */

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

fun Array<DoubleArray>.toVector(): DoubleArray {
    require(this[0].size == 1) {
        "A vector should have only one column."
    }
    return map { it[0] }.toDoubleArray()
}

fun DoubleArray.toMatrix(): Array<DoubleArray> {
    return Array(size) { i ->
        DoubleArray(1) { this[i] }
    }
}
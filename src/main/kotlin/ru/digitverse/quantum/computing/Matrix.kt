package ru.digitverse.quantum.computing

import kotlin.math.sqrt

open class Matrix(
    var coefficient: Double,
    var matrix: Array<DoubleArray>
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            !is Matrix -> false
            else -> other.coefficient == coefficient && matrix.contentDeepEquals(other.matrix)
        }
    }

    override fun hashCode(): Int {
        var result = coefficient.hashCode()
        result = 31 * result + matrix.contentDeepHashCode()
        return result
    }

    override fun toString(): String {
        return buildString {
            for (row in matrix) {
                for (cell in row) {
                    append(cell)
                    append("\t")
                }
                append("\n")
            }
        }
    }

    companion object Factory {
        fun not(): Matrix = Matrix(
            coefficient = 1.0,
            matrix = arrayOf(
                doubleArrayOf(0.0, 1.0),
                doubleArrayOf(1.0, 0.0)
            )
        )

        fun identity(n: Int = 2): Matrix = Matrix(
            coefficient = 1.0,
            matrix = Array(n) { i ->
                DoubleArray(n) { j ->
                    if (i == j) 1.0 else 0.0
                }
            }
        )

        fun zero(n: Int = 2): Matrix = Matrix(
            coefficient = 1.0,
            matrix = Array(n) {
                DoubleArray(n)
            }
        )

        fun hadamard(): Matrix = Matrix(
            coefficient = 1 / sqrt(2.0),
            matrix = arrayOf(
                doubleArrayOf(1.0, 1.0),
                doubleArrayOf(1.0, -1.0)
            )
        )
    }
}
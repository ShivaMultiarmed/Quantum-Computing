package ru.digitverse.quantum.computing

/**
 * A class that represents a vector.
 */
open class Vector (
    coefficient: Double,
    vector: DoubleArray
): Matrix(coefficient, vector.toMatrix())

fun Matrix.toVector(): Vector {
    require(matrix[0].size == 1) {
        "Matrix must have one column to be convertable to a vector"
    }
    return Vector(coefficient, DoubleArray(matrix.size){ i -> matrix[i][0] })
}

operator fun Matrix.times(other: Vector): Vector = Vector(
    coefficient = coefficient * other.coefficient,
    vector = (matrix * other.matrix).toVector()
)

fun Vector.tensor(other: Vector): Vector = Vector(
    coefficient = coefficient * other.coefficient,
    vector = matrix.tensor(other.matrix).toVector()
)
package ru.digitverse.quantum.computing

open class Vector(
    K: Double,
    V: DoubleArray
): Matrix(K, V.toMatrix())

fun DoubleArray.toMatrix(): Array<DoubleArray> {
    return Array(size) { i ->
        DoubleArray(1) { this[i] }
    }
}

fun Matrix.toVector(): Vector {
    require(matrix[0].size == 1) {
        "Matrix must have one column to be convertable to a vector"
    }
    return Vector(coefficient, DoubleArray(matrix.size){ i -> matrix[i][0] })
}
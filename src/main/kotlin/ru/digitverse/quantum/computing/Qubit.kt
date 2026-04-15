package ru.digitverse.quantum.computing

import kotlin.math.sqrt

/**
 * A class that represents a qubit
 *
 * @author Mikhail Shell
 * @since 1.0.0
 */
class Qubit(
    coefficient: Double,
    a: Double,
    b: Double
) : Register(coefficient, doubleArrayOf(a, b)) {
    /**
     * Factory methods to create most common states of [Qubit]s.
     */
    companion object Factory {
        fun zero(): Qubit {
            return Qubit(1.0, 1.0, 0.0)
        }

        fun one(): Qubit {
            return Qubit(1.0, 0.0, 1.0)
        }

        fun superPosPlus(): Qubit {
            return Qubit(1 / sqrt(2.0), 1.0, 1.0)
        }

        fun superPosMinus(): Qubit {
            return Qubit(1 / sqrt(2.0), 1.0, -1.0)
        }
    }
}

fun Matrix.toQubit(): Qubit {
    return Qubit(coefficient, matrix[0][0], matrix[1][0])
}

operator fun Matrix.times(other: Qubit): Qubit {
    val (a, b) = (matrix * other.matrix).toVector()
    return Qubit(
        coefficient = coefficient * other.coefficient,
        a = a,
        b = b
    )
}
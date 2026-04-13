package ru.digitverse.quantum.computing

import kotlin.math.sqrt

class Qubit(
    K: Double,
    a: Double,
    b: Double
): Register(K, doubleArrayOf(a, b)) {
    init {
//        val magnitudeSquared = (K * a).pow(2) + (K * b).pow(2)
//        val error = 1e-6
//        require(abs(magnitudeSquared - 1) < error) {
//            "Invalid Qubit"
//        }
    }

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
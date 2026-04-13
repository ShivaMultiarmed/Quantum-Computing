package ru.digitverse.quantum.computing

import kotlin.math.log

open class Register(
    K: Double,
    V: DoubleArray
) : Vector(K, V) {
    init {
        require(checkPower(V)) {
            "Register's size must be of the power of 2"
        }
    }

    private fun checkPower(V: DoubleArray): Boolean {
        var indicator = V.size.toDouble() // indicates if the array size is of power of 2
        while (indicator > 1) {
            indicator /= 2
            if (indicator != indicator.toInt().toDouble()) { // not integer
                return false
            }
        }
        return true
    }

    override fun toString(): String {
        return buildString {
            if (coefficient != 1.0) {
                append(coefficient)
                append("(")
            }
            for (i in matrix.indices) {
                val value = matrix[i][0]
                if (value != 0.0) {
                    val lastChar = if (isNotEmpty()) this[length - 1] else null

                    if (lastChar != '(') {
                        if (value > 0) {
                            append('+')
                        } else {
                            append('-')
                        }
                    } else if (value < 0) {
                        append('-')
                    }

                    val absValue = Math.abs(value)
                    if (absValue != 1.0) {
                        append(absValue)
                    }

                    append("|")
                    append(intToBinaryString(i, qubitsNumber()))
                    append(">")
                }
            }
            if (coefficient != 1.0) {
                append(")")
            }
        }
    }

    companion object Factory {
        fun from(string: String): Register {
            val coeffRegex = """^([\d.]+)""".toRegex()
            val k = coeffRegex.find(string)?.groupValues?.get(1)?.toDouble() ?: 1.0

            val stateRegex = """([+-]?)\|([01]+)>""".toRegex()
            val matches = stateRegex.findAll(string).toList()

            require(matches.isNotEmpty()) {
                "Invalid Register"
            }

            val firstState = matches.first().groupValues[2]
            val vectorSize = 1 shl firstState.length
            val v = DoubleArray(vectorSize)

            for (match in matches) {
                val (sign, state) = match.destructured
                val index = state.toInt(2)
                v[index] = if (sign == "-") -1.0 else 1.0
            }

            return Register(k, v)
        }
    }
}

fun Register.qubitsNumber(): Int = log(matrix.size.toDouble(), 2.0).toInt()
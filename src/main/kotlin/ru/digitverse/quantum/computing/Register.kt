package ru.digitverse.quantum.computing

import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A class that represents a register.
 *
 * @author Mikhail Shell
 * @since 1.0.0
 */
open class Register(
    coefficient: Double,
    register: DoubleArray
) : Vector(coefficient, register) {
    init {
        require(checkPower(register.size)) {
            "Register's size must be of the power of 2"
        }
    }

    override fun toString(): String {
        return buildString {
            if (this@Register.coefficient != 1.0) {
                append(this@Register.coefficient)
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
                    append(i.toBinary(qubitsNumber()))
                    append(">")
                }
            }
            if (this@Register.coefficient != 1.0) {
                append(")")
            }
        }
    }

    operator fun get(index: Int): Qubit {
        val qubitCount = qubitsNumber()
        if (index !in 0 until qubitCount) {
            throw IndexOutOfBoundsException("Invalid Qubit's index.")
        }
        val binaryRegisterStates = matrix.mapIndexed { i, row ->
            i to row[0]
        }.filter { (_, x) ->
            x > 0.0
        }.map { (i, _) ->
            i.toBinary(qubitCount)
        }
        val a = sqrt(binaryRegisterStates.count { it[index] == '0' }.toDouble()/ binaryRegisterStates.size)
        val b = sqrt(1 - a.pow(2))
        return Qubit(1.0, a, b)
    }

    /**
     * Factory methods to create [Register] instances.
     */
    companion object Factory {
        fun from(vararg qubits: Qubit): Register {
            var register = qubits[0].toRegister()
            for (i in 1 until qubits.size) {
                register = register.tensor(qubits[i]).toRegister()
            }
            return register
        }
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

/**
 * @receiver A checked register
 * @return The count of qubits inside the register
 */
fun Register.qubitsNumber(): Int = log(matrix.size.toDouble(), 2.0).toInt()

fun Matrix.toRegister(): Register {
    require(matrix[0].size == 1 && checkPower(matrix.size)) {
        "Matrix must have one column to be convertable to a vector"
    }
    return Register(coefficient, DoubleArray(matrix.size){ i -> matrix[i][0] })
}

/**
 * @param array The tested array.
 * @return true if the provided array is of size of power of 2 and false otherwise
 */
private fun checkPower(size: Int): Boolean {
    var indicator = size.toDouble() // indicates if the array size is of power of 2
    while (indicator > 1) {
        indicator /= 2
        if (indicator != indicator.toInt().toDouble()) { // not integer
            return false
        }
    }
    return true
}

operator fun Matrix.times(other: Register): Register = Register(
    coefficient = coefficient * other.coefficient,
    register = (matrix * other.matrix).toVector()
)

fun Register.tensor(other: Register): Register = Register(
    coefficient = coefficient * other.coefficient,
    register = matrix.tensor(other.matrix).toVector()
)
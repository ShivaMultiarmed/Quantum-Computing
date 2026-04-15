package ru.digitverse.quantum.computing

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Clock

/**
 * Observer of single [Qubit]s and [Qubit]s in [Register]s.
 *
 * @author Mikhail Shell
 * @since 1.0.0
 */
class Observer {
    private val random = Random(Clock.System.now().toEpochMilliseconds())

    /**
     * Observes a [Qubit] and make it collapse into one of classic states: 1 or 0.
     * @param qubit A qubit to observe. This instance changes its internal states of coefficient and matrix
     * @return Classic state of a [Qubit]: 1 or 0
     */
    fun observe(qubit: Qubit): Int {
        val a = qubit.coefficient * qubit.matrix[0][0]
        val p = a.pow(2.0) // Probability of zero
        val state = random.nextDouble() > p
        qubit.matrix[0][0] = (if (state) 0 else 1).toDouble()
        qubit.matrix[1][0] = (if (state) 1 else 0).toDouble()
        qubit.coefficient = 1.0
        return if (qubit.matrix[0][0] == 0.0) 1 else 0
    }

    /**
     * Observes a [Qubit] and make it collapse into one of classic states: 1 or 0 but does it with axes' rotation.
     * @param qubit A qubit to observe. This instance changes its internal states of coefficient and matrix
     * @return Classic state of a [Qubit]: 1 or 0
     */
    fun observeWithRotation(qubit: Qubit): Int {
        val qubitTransformed = (Matrix.hadamard() * qubit).toQubit()
        qubit.coefficient = qubitTransformed.coefficient
        qubit.matrix = qubitTransformed.matrix
        return observe(qubit)
    }

    /**
     * Observes a [Register] and make it collapse into one of possible classic states.
     * @param register A [Register] to observe. This instance changes its internal states of coefficient and matrix
     */
    fun observe(register: Register) {
        val percent = random.nextDouble()
        var minPercent: Double
        var maxPercent = 0.0
        var i = 0
        var k = 0
        while (i < register.matrix.size) {
            if (register.matrix[i][0] != 0.0) {
                k++
                minPercent = maxPercent
                maxPercent = minPercent + (register.matrix[i][0] * register.coefficient).pow(2.0)
                register.matrix[i][0] = (if (percent in minPercent ..< maxPercent) 1 else 0).toDouble()
            }
            i++
        }
        register.coefficient = 1.0
    }

    /**
     * Observes a [Register] and make one of its [Qubit]s collapse into one of classic states.
     * @param register A [Register] to observe. This instance changes its internal states of coefficient and matrix
     * @param index A zero-based index of a [Qubit] in the [Register]
     */
    fun observeQubitInRegister(register: Register, index: Int) {
        var a = 0.0
        var b = 0.0
        val qNum = register.qubitsNumber()

        for (i in register.matrix.indices) {
            if (register.matrix[i][0] != 0.0) {
                val qubitString = i.toBinary(qNum)
                if (qubitString[index] == '0') b++ else a++
            }
        }

        a /= (a + b)
        b = 1.0 - a

        val q = Qubit(1.0, a, b)
        val result = observe(q).toString(2).first()

        var c = 0.0
        for (i in register.matrix.indices) {
            if (register.matrix[i][0] != 0.0) {
                val qubitString = i.toBinary(qNum)
                if (qubitString[index] == result) {
                    c++
                } else {
                    register.matrix[i][0] = 0.0
                }
            }
        }

        register.coefficient = 1.0 / sqrt(c)
    }
}
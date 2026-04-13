package ru.digitverse.quantum.computing

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Clock

class Observer {
    private val random = Random(Clock.System.now().toEpochMilliseconds())
    fun observe(qubit: Qubit): Int {
        val a = qubit.coefficient * qubit.matrix[0][0]
        val p = a.pow(2.0) // Probability of zero
        val state = random.nextDouble() > p
        qubit.matrix[0][0] = (if (state) 0 else 1).toDouble()
        qubit.matrix[1][0] = (if (state) 1 else 0).toDouble()
        qubit.coefficient = 1.0
        return if (qubit.matrix[0][0] == 0.0) 1 else 0
    }
    fun observeH(qubit: Qubit): Int {
        val qubitTransformed = (Matrix.hadamard() * qubit).toQubit()
        qubit.coefficient = qubitTransformed.coefficient
        qubit.matrix = qubitTransformed.matrix
        return observe(qubit)
    }
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
    fun observeQubitInRegister(register: Register, index: Int) {
        var a = 0.0
        var b = 0.0
        val qNum = register.qubitsNumber()

        for (i in register.matrix.indices) {
            if (register.matrix[i][0] != 0.0) {
                val qubitString = intToBinaryString(i, qNum);
                if (qubitString[index] == '0') b++ else a++
            }
        }

        a /= (a + b)
        b = 1.0 - a

        val q = Qubit(1.0, a, b)
        val result = Integer.toBinaryString(observe(q)).first() // Assuming observe returns 0 or 1

        var c = 0.0
        for (i in register.matrix.indices) {
            if (register.matrix[i][0] != 0.0) {
                val qubitString = intToBinaryString(i, qNum)
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
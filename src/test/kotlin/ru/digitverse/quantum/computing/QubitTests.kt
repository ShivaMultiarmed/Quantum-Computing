package ru.digitverse.quantum.computing

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class QubitTests {
    @Test
    fun qubitToRegister1() {
        val qubit1 = Qubit(1.0, 1.0, 0.0)
        val qubit2 = Qubit(1.0, 1.0, 0.0)
        val expected = Register(1.0, doubleArrayOf(1.0, 0.0, 0.0, 0.0))
        val real = qubit1.tensor(qubit2)
        assertEquals(expected, real)
    }

    @Test
    fun qubitToRegister2() {
        val qubit1 = Qubit(1 / sqrt(2.0), 1.0, 1.0)
        val qubit2 = Qubit(1.0, 1.0, 0.0)
        val expected = Register(1 / sqrt(2.0), doubleArrayOf(1.0, 0.0, 1.0, 0.0))
        val real = qubit1.tensor(qubit2)
        assertEquals(expected, real)
    }

    @Test
    fun testHadamardTransform() {
        val q = Qubit.zero()
        val h = Matrix.hadamard()
        val expected = Qubit.superPosPlus()
        val real = h * q
        assertEquals(expected, real)
    }

    @Test
    fun testCreatingRegister() {
        val expected = (1 / sqrt(2.0)).toString() + "(|00>+|10>)"
        val register = Register(1 / sqrt(2.0), doubleArrayOf(1.0, 0.0, 1.0, 0.0))
        val real = register.toString()
        assertEquals(expected, real)
    }

    @Test
    fun testObserving() {
        val observer = Observer()
        repeat(10) { _ ->
            val q = Qubit.superPosPlus()
            println(q)
            observer.observe(q)
            println(q)
        }
    }
}
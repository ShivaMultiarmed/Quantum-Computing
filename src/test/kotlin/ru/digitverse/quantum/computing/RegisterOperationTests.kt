package ru.digitverse.quantum.computing

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.sqrt


class RegisterOperationTests {
    @Test
    fun testObservingRegister() {
        val register = Register(1 / sqrt(2.0), doubleArrayOf(0.0, 0.0, 1.0, 1.0))
        val observer = Observer()
        println(register)
        observer.observe(register)
        println(register)
    }

    @Test
    fun testObservingQubitInRegister() {
        val register = Register(1 / sqrt(3.0), doubleArrayOf(1.0, 1.0, 0.0, 1.0))
        println(register)
        val observer = Observer()
        observer.observeQubitInRegister(register, 1)
        println(register)
    }

    @Test
    fun testTransformingRegisterFromString() {
        val states = "0.577(-|01>-|10>+|11>)"
        val real = Register.from(states)
        val expected = Register(0.577, doubleArrayOf(0.0, -1.0, -1.0, 1.0))
        Assertions.assertEquals(expected, real)
    }
}
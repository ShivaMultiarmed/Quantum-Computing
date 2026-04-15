package ru.digitverse.quantum.computing

import org.junit.jupiter.api.Test

class FunctionTests {
    @Test
    fun testFunction1() {
        val x1 = Qubit.superPosPlus()
        val x2 = Qubit.superPosPlus()
        val y1 = Qubit.zero()
        val register = x1.tensor(x2).tensor(y1).toRegister()
        val fs = arrayOf(
            doubleArrayOf(0.0),
            doubleArrayOf(1.0),
            doubleArrayOf(1.0),
            doubleArrayOf(1.0)
        )
        val function = Function.create(fs, 2)
        println(function)
        val result = (function * register).toRegister()
        println(result)
    }
}
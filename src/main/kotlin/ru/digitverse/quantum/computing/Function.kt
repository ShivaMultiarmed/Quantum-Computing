package ru.digitverse.quantum.computing

import kotlin.math.pow

/**
 * A class that represent a quantum function.
 *
 * @author Mikhail Shell
 * @since 1.0.0
 */
class Function private constructor(
    matrix: Array<DoubleArray>
) : Matrix(1.0, matrix) {
    companion object Factory {
        /**
         * Generates a [Function] instance.
         * @param results A matrix of results for each function passed.
         * @param argsNumber Number of arguments
         * @param rowsNumber Number of rows
         *
         * @return A [Function] matrix representing the results from truth table
         */
        fun create(
            results: Array<DoubleArray>,
            argsNumber: Int = (results.size - 1).getQubitDigitCount(),
            rowsNumber: Int = 2.0.pow(argsNumber).toInt()
        ): Function {
            return Function(
                generateMatrix(argsNumber, results, rowsNumber)
            )
        }
    }
}

/**
 * Generates a matrix of arguments for a function.
 *
 * @param argsNumber Number of arguments
 * @param rowsNumber Number of rows
 *
 * @return 2D-array of all possible combinations of arguments
 */
private fun generateArguments(
    argsNumber: Int,
    rowsNumber: Int = 2.0.pow(argsNumber).toInt()
): Array<IntArray> {
    return Array(rowsNumber) { i ->
        var x = i
        val row = IntArray(argsNumber)
        for (j in argsNumber - 1 downTo 0) {
            row[j] = x % 2
            x /= 2
        }
        row
    }
}

/**
 * Generates a matrix of arguments for a function.
 *
 * @param argsNumber Number of arguments
 * @param rowsNumber Number of rows
 *
 * @return 2D-array of all possible combinations of arguments
 */
private fun generateDoubleArguments(
    argsNumber: Int,
    rowsNumber: Int = 2.0.pow(argsNumber).toInt()
): Array<DoubleArray> {
    return generateArguments(argsNumber, rowsNumber).map { row ->
        row.map { it.toDouble() }.toDoubleArray()
    }.toTypedArray()
}

/**
 * Generates a 2D array for a function
 *
 * @param argsNumber Number of arguments from a truth table of a function
 * @param matrix Truth table of a function
 * @param rowsNumber Number of rows of the result 2D array
 *
 * @return Function matrix in 2D-array
 */
private fun generateMatrix(
    argsNumber: Int,
    matrix: Array<DoubleArray>,
    rowsNumber: Int = 2.0.pow(argsNumber).toInt()
): Array<DoubleArray> {
    val truthTable = createTruthTable(matrix, argsNumber, rowsNumber)
    val functionsCount = matrix[0].size
    val newArgsNumber = argsNumber + functionsCount

    val arguments = generateDoubleArguments(newArgsNumber)
    val functionMatrixSide = 2.0.pow(newArgsNumber).toInt()
    val functionMatrix = Array(functionMatrixSide) { DoubleArray(functionMatrixSide) }

    for (argumentIndex in arguments.indices) {
        for (colIndex in argsNumber until arguments[argumentIndex].size) {
            val y = arguments[argumentIndex][colIndex].toInt()
            val f = evaluateFunctionInPoint(argumentIndex, colIndex, truthTable, arguments, argsNumber)
            arguments[argumentIndex][colIndex] = (y xor f).toDouble()
        }
        val rowAsInts = IntArray(arguments[argumentIndex].size) {
            arguments[argumentIndex][it].toInt()
        }
        val columnIndex = rowAsInts.toInt()
        functionMatrix[argumentIndex][columnIndex] = 1.0
    }
    return functionMatrix
}

/**
 * @param argumentIndex Row index in truth table
 * @param functionIndex Index of the function needed
 * @param truthTable Truth table
 * @param results Results of truth table
 * @param argsNumber Number of arguments in the original truth table
 *
 * @return Result of function evaluation: 1 or 0
 */
private fun evaluateFunctionInPoint(
    argumentIndex: Int,
    functionIndex: Int,
    truthTable: Array<DoubleArray>,
    results: Array<DoubleArray>,
    argsNumber: Int
): Int {
    var cursor = 0 // Chooses a row in the initial matrix to evaluate the function
    for (j in 0 until argsNumber) {
        while (cursor < truthTable.size) {
            if (truthTable[cursor][j] == results[argumentIndex][j]) {
                break // Found the wanted row with the result where all the arguments are relevant
            } else {
                cursor++ // Otherwise, we take the next row
            }
        }
    }
    if (cursor == truthTable.size) cursor--
    return truthTable[cursor][functionIndex].toInt()
}

private fun createTruthTable(
    results: Array<DoubleArray>,
    argsNumber: Int,
    rowsNumber: Int = 2.0.pow(argsNumber).toInt()
): Array<DoubleArray> {
    val args = generateArguments(argsNumber, rowsNumber)
    val funsNumber = results[0].size
    return Array(rowsNumber) { i ->
        DoubleArray(argsNumber + funsNumber) { j ->
            if (j < argsNumber) {
                args[i][j].toDouble()
            } else {
                results[i][j - argsNumber]
            }
        }
    }
}
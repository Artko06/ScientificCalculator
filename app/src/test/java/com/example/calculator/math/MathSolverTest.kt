package com.example.calculator.math

import com.example.calculator.math.model.MathResult
import org.junit.Test

class MathSolverTest {
    private val solver = MathSolver()

    @Test
    fun `test complex expression`() {
        val result = solver.solve("(1323 + 5!) + 4^5")
        assert(result is MathResult.Success && result.answer == "2467")
    }

    @Test
    fun `test unary minus`() {
        val result = solver.solve("-5 + 2 * 3")
        assert(result is MathResult.Success && result.answer == "1")
    }

    @Test
    fun `test division by zero`() {
        val result = solver.solve("10 / 0")
        assert(result is MathResult.Error)
    }
}
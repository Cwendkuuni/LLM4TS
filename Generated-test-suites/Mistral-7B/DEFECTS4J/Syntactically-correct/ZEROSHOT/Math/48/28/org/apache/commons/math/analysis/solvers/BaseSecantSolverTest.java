package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnbracketedException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    /** The solver to test. */
    private BaseSecantSolver solver;

    /** The function to test against. */
    private UnivariateFunction function;

    /** The minimum value. */
    private double min;

    /** The maximum value. */
    private double max;

    /** The allowed solution. */
    private AllowedSolution allowedSolution;

    /** The constructor parametrized with default values. */
    public BaseSecantSolverTest(UnivariateFunction function, double min, double max) {
        this.function = function;
        this.min = min;
        this.max = max;
        this.solver = new RegulaFalsiSolver(DEFAULT_ABSOLUTE_ACCURACY);
        this.allowedSolution = AllowedSolution.ANY_SIDE;
    }

    /** The constructor parametrized with absolute accuracy. */
    @Parameters(name = "{index}: absoluteAccuracy={0}, method={1}")
    public static Collection<Object[]> dataWithAbsoluteAccuracy() {
        return Arrays.asList(new Object[][]{
                {new MyFunction(), 0.1, 1.0},
                {new MyFunction(), 2.0, 3.0},
                {new MyFunction(), -2.0, -1.0},
                {new MyFunction(), -3.0, -4.0},
        });
    }

    /** The constructor parametrized with relative accuracy and absolute accuracy. */
    @Parameters(name = "{index}: relativeAccuracy={0}, absoluteAccuracy={1}, method={2}")
    public static Collection<Object[]> dataWithRelativeAndAbsoluteAccuracy() {
        return Arrays.asList(new Object[][]{
                {new MyFunction(), 0.1, 1e-5, RegulaFalsiSolver.Method.REGULA_FALSI},
                {new MyFunction(), 0.1, 1e-5, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 0.1, 1e-5, RegulaFalsiSolver.Method.PEGASUS},
                {new MyFunction(), 1e-3, 1e-6, RegulaFalsiSolver.Method.REGULA_FALSI},
                {new MyFunction(), 1e-3, 1e-6, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 1e-3, 1e-6, RegulaFalsiSolver.Method.PEGASUS},
                {new MyFunction(), 1e-5, 1e-9, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 1e-5, 1e-9, RegulaFalsiSolver.Method.PEGASUS},
                {new MyFunction(), 1e-6, 1e-12, RegulaFalsiSolver.Method.PEGASUS},
                {new MyFunction(), 1e-7, 1e-13, RegulaFalsiSolver.Method.PEGASUS},
        });
    }

    /** The constructor parametrized with maximum number of evaluations. */
    @Parameters(name = "{index}: maxEval={0}, method={1}")
    public static Collection<Object[]> dataWithMaxEval() {
        return Arrays.asList(new Object[][]{
                {new MyFunction(), 10, RegulaFalsiSolver.Method.REGULA_FALSI},
                {new MyFunction(), 20, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 30, RegulaFalsiSolver.Method.PEGASUS},
        });
    }

    /** The constructor parametrized with allowed solution. */
    @Parameters(name = "{index}: allowedSolution={0}, method={1}")
    public static Collection<Object[]> dataWithAllowedSolution() {
        return Arrays.asList(new Object[][]{
                {new MyFunction(), 0.1, 1.0, AllowedSolution.BELOW_SIDE, RegulaFalsiSolver.Method.REGULA_FALSI},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.ABOVE_SIDE, RegulaFalsiSolver.Method.REGULA_FALSI},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.LEFT_SIDE, RegulaFalsiSolver.Method.REGULA_FALSI},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.RIGHT_SIDE, RegulaFalsiSolver.Method.REGULA_FALSI},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.ANY_SIDE, RegulaFalsiSolver.Method.REGULA_FALSI},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.BELOW_SIDE, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.ABOVE_SIDE, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.LEFT_SIDE, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.RIGHT_SIDE, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.ANY_SIDE, RegulaFalsiSolver.Method.ILLINOIS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.BELOW_SIDE, PegasusSolver.Method.PEGASUS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.ABOVE_SIDE, PegasusSolver.Method.PEGASUS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.LEFT_SIDE, PegasusSolver.Method.PEGASUS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.RIGHT_SIDE, PegasusSolver.Method.PEGASUS},
                {new MyFunction(), 0.1, 1.0, AllowedSolution.ANY_SIDE, PegasusSolver.Method.PEGASUS},
        });
    }

    @Before
    public void setUp() {
        // No setup needed.
    }

    @Test
    public void testSolve() {
        // Test solver with default method, absolute accuracy.
        double actual = solver.solve(function, min, max);
        double expected = function.root(min, max);
        assertEquals(expected, actual, Precision.SAFE_EPSILON);

        // Test solver with absolute accuracy.
        solver = new RegulaFalsiSolver(1e-5);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        assertEquals(expected, actual, 1e-5);

        // Test solver with relative accuracy and absolute accuracy.
        solver = new RegulaFalsiSolver(1e-3, 1e-6);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        double relError = Precision.relErrorEstimate(expected, actual);
        assertEquals(expected, actual, 1e-3 + Precision.safeMultiple(1e-6, relError));

        // Test solver with maximum number of evaluations.
        solver = new RegulaFalsiSolver(1000);
        assertThrows(MaxEvalExceededException.class, () -> { solver.solve(function, min, max); });

        // Test solver with allowed solution.
        solver = new RegulaFalsiSolver(1e-5, AllowedSolution.BELOW_SIDE);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        if (expected < 0) {
            assertEquals(expected, actual, 1e-5);
        } else {
            assertThrows(UnbracketedException.class, () -> { solver.solve(function, min, max); });
        }

        solver = new RegulaFalsiSolver(1e-5, AllowedSolution.ABOVE_SIDE);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        if (expected > 0) {
            assertEquals(expected, actual, 1e-5);
        } else {
            assertThrows(UnbracketedException.class, () -> { solver.solve(function, min, max); });
        }

        solver = new RegulaFalsiSolver(1e-5, AllowedSolution.LEFT_SIDE);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        if (expected <= min) {
            assertEquals(expected, actual, 1e-5);
        } else {
            assertThrows(NumberIsTooSmallException.class, () -> { solver.solve(function, min, max); });
        }

        solver = new RegulaFalsiSolver(1e-5, AllowedSolution.RIGHT_SIDE);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        if (expected >= max) {
            assertEquals(expected, actual, 1e-5);
        } else {
            assertThrows(NumberIsTooLargeException.class, () -> { solver.solve(function, min, max); });
        }

        solver = new RegulaFalsiSolver(1e-5, AllowedSolution.ANY_SIDE);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        assertEquals(expected, actual, 1e-5);

        // Test solver with Illinois method and absolute accuracy.
        solver = new IllinoisSolver(1e-5);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        assertEquals(expected, actual, 1e-5);

        // Test solver with Pegasus method and absolute accuracy.
        solver = new PegasusSolver(1e-5);
        actual = solver.solve(function, min, max);
        expected = function.root(min, max);
        assertEquals(expected, actual, 1e-5);
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x - 2;
        }

        public double root(double min, double max) {
            return Math.sqrt(2);
        }
    }

}
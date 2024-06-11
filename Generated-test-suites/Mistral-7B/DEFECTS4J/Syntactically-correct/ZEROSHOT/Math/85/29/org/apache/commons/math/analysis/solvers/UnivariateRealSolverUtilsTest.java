package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.special.E;

/**
 * JUnit test file for UnivariateRealSolverUtils.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Tests the solve method with a simple function.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testSolveSimpleFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        double root = UnivariateRealSolverUtils.solve(function, -1.5, 1.5);
        assertEquals("Root not found", -0.5, root, 0.001);
    }

    /**
     * Tests the solve method with a function that has multiple roots.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testSolveFunctionWithMultipleRoots() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(2.0 * x);
            }
        };
        double root1 = UnivariateRealSolverUtils.solve(function, -3.0, 3.0);
        double root2 = UnivariateRealSolverUtils.solve(function, 0.0, 3.0);
        double root3 = UnivariateRealSolverUtils.solve(function, -3.0, 0.0);
        double epsilon = 0.001;
        assertEquals("Root 1 not found", -1.8369724681264292, root1, epsilon);
        assertEquals("Root 2 not found", 0.4224743961155141, root2, epsilon);
        assertEquals("Root 3 not found", -0.4224743961155141, root3, epsilon);
    }

    /**
     * Tests the solve method with a function that has no roots in the given range.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testSolveFunctionWithNoRoots() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 1;
            }
        };
        try {
            UnivariateRealSolverUtils.solve(function, -10.0, 10.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ce) {
            // Expected
        }
    }

    /**
     * Tests the bracket method with a simple function.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testBracketSimpleFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        double[] brackets = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        double a = brackets[0];
        double b = brackets[1];
        assertTrue("a not less than lower bound", a < -1.0);
        assertTrue("b not greater than upper bound", b > 1.0);
        assertTrue("a not equal to b", a != b);
        double root = UnivariateRealSolverUtils.solve(function, a, b);
        assertEquals("Root not found", -0.5, root, 0.001);
    }

    /**
     * Tests the bracket method with a function that has multiple roots.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testBracketFunctionWithMultipleRoots() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(2.0 * x);
            }
        };
        double[] brackets = UnivariateRealSolverUtils.bracket(function, -3.0, -1.0, 1.0);
        double a = brackets[0];
        double b = brackets[1];
        double[] roots = UnivariateRealSolverUtils.bracket(function, a, b, 1000);
        double root1 = roots[0];
        double root2 = roots[1];
        double epsilon = 0.001;
        assertEquals("Root 1 not found", -1.8369724681264292, root1, epsilon);
        assertEquals("Root 2 not found", 0.4224743961155141, root2, epsilon);
    }

    /**
     * Tests the bracket method with a function that has no roots in the given range.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testBracketFunctionWithNoRoots() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 1;
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(function, -10.0, 10.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException ce) {
            // Expected
        }
    }

    /**
     * Tests the midpoint method.
     */
    @org.junit.Test
    public void testMidpoint() {
        double a = 2.0;
        double b = 4.0;
        double expected = 3.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals("Midpoint not calculated correctly", expected, result, 0.001);
    }
}
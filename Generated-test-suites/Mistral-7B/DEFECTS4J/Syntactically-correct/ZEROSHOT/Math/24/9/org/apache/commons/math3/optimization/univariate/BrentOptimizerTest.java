package org.apache.commons.math3.optimization.univariate;

import static org.apache.commons.math3.util.Precision.equals;
import static org.apache.commons.math3.util.Precision.equalsWithinAbsTol;
import static org.apache.commons.math3.util.Precision.abs;
import static org.apache.commons.math3.util.FastMath.ulp;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;

import junit.framework.TestCase;

public class BrentOptimizerTest extends TestCase {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * .
     */
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * .
     */
    public void testBrentOptimizerWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new DummyConvergenceChecker());
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double)} .
     */
    public void testBrentOptimizerDefaultValues() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01);
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} .
     */
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariateFunction function = new TestFunction();

        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with user-defined
     * convergence checker.
     */
    public void testOptimizeWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MyConvergenceChecker());
        UnivariateFunction function = new TestFunction();

        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);

        // Test that the convergence checker was called.
        assertTrue(convergenceCheckerCalled);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with invalid input.
     */
    public void testOptimizeInvalidInput() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001);
        UnivariateFunction function = new TestFunction();

        try {
            optimizer.optimize(function, GoalType.MINIMIZE);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with invalid input (relative
     * tolerance too small).
     */
    public void testOptimizeInvalidInputRelTol() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
        UnivariateFunction function = new TestFunction();

        try {
            optimizer.optimize(function, GoalType.MINIMIZE);
            fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with invalid input
     * (absolute tolerance too small).
     */
    public void testOptimizeInvalidInputAbsTol() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001);
        UnivariateFunction function = new TestFunction();

        try {
            optimizer.optimize(function, GoalType.MINIMIZE);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with invalid input
     * (function which is not UnivariateFunction).
     */
    public void testOptimizeInvalidInputFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        Object invalidFunction = new Object();

        try {
            optimizer.optimize(invalidFunction, GoalType.MINIMIZE);
            fail();
        } catch (ClassCastException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with goal type other than
     * MINIMIZE.
     */
    public void testOptimizeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariateFunction function = new TestFunctionMax();

        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE);
        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals(4.5, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with goal type other than
     * MINIMIZE and user-defined convergence checker.
     */
    public void testOptimizeMaximizeWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MyConvergenceChecker());
        UnivariateFunction function = new TestFunctionMax();

        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE);
        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals(4.5, result.getValue(), 0.001);

        // Test that the convergence checker was called.
        assertTrue(convergenceCheckerCalled);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with narrow interval
     * (minimum value is greater than maximum value).
     */
    public void testOptimizeNarrowInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5);
        UnivariateFunction function = new TestFunction();

        try {
            optimizer.optimize(function, GoalType.MINIMIZE);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with narrow interval
     * (minimum value is greater than maximum value) and user-defined convergence
     * checker.
     */
    public void testOptimizeNarrowIntervalWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5, new DummyConvergenceChecker());
        UnivariateFunction function = new TestFunction();

        try {
            optimizer.optimize(function, GoalType.MINIMIZE);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with very narrow interval
     * (minimum value is almost equal to maximum value).
     */
    public void testOptimizeVeryNarrowInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0000001, 1.0000001);
        UnivariateFunction function = new TestFunction();

        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize()} with very narrow interval
     * (minimum value is almost equal to maximum value) and user-defined convergence
     * checker.
     */
    public void testOptimizeVeryNarrowIntervalWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0000001, 1.0000001, new MyConvergenceChecker());
        UnivariateFunction function = new TestFunction();

        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);

        // Test that the convergence checker was called.
        assertTrue(convergenceCheckerCalled);
    }

    private static class TestFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 1.0;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private static class TestFunctionMax implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) + 3.0;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private static boolean convergenceCheckerCalled = false;

    private static class DummyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return true;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            convergenceCheckerCalled = true;
            return FastMath.abs(previous.getValue() - current.getValue()) < 0.0001;
        }
    }
}
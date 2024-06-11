package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AckleyFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BentleyFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.RosenbrockFunction;

/**
 * Tests for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @org.junit.Test
    public void testBrentOptimizer() {
        // Test with default constructor
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new AckleyFunction();
        PointValuePair solution = optimizer.optimize(function, 0.0, 10.0, 1e-8, 1e-8, null);
        assertEquals(0.0, solution.getPoint(), 1e-7);
        assertEquals(-20.0, solution.getValue(), 1e-7);

        // Test with constructor with thresholds
        BrentOptimizer optimizerWithThresholds = new BrentOptimizer(1e-8, 1e-8);
        PointValuePair solutionWithThresholds = optimizerWithThresholds.optimize(function, 0.0, 10.0);
        assertEquals(0.0, solutionWithThresholds.getPoint(), 1e-7);
        assertEquals(-20.0, solutionWithThresholds.getValue(), 1e-7);

        // Test with constructor with thresholds and checker
        BrentOptimizer optimizerWithChecker = new BrentOptimizer(1e-8, 1e-8, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-5;
            }
        });
        PointValuePair solutionWithChecker = optimizerWithChecker.optimize(function, 0.0, 10.0);
        assertEquals(0.0, solutionWithChecker.getPoint(), 1e-7);
        assertEquals(-20.0, solutionWithChecker.getValue(), 1e-7);
        assertIterationCount(100, optimizerWithChecker);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double)}.
     */
    @org.junit.Test
    public void testOptimize() {
        // Test with simple function
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.AbsFunction();
        PointValuePair solution = optimizer.optimize(function, 0.0, 1.0, 1e-8, 1e-8, null);
        assertEquals(0.0, solution.getPoint(), 1e-7);
        assertEquals(0.0, solution.getValue(), 1e-7);

        // Test with function with two local minima
        UnivariateFunction twoMinimaFunction = new TwoMinimaFunction();
        PointValuePair solutionTwoMinima = optimizer.optimize(twoMinimaFunction, -5.0, 5.0, 1e-8, 1e-8, null);
        assertEquals(-3.0, solutionTwoMinima.getPoint(), 1e-7);
        assertEquals(4.012202145493302E-14, solutionTwoMinima.getValue(), 1e-12);

        // Test with function with no real root
        UnivariateFunction noRootFunction = new NoRootFunction();
        PointValuePair solutionNoRoot = optimizer.optimize(noRootFunction, -10.0, 10.0, 1e-8, 1e-8, null);
        assertNull(solutionNoRoot);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, double, ConvergenceChecker)}.
     */
    @org.junit.Test
    public void testOptimizeWithChecker() {
        // Test with simple function and custom checker
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.AbsFunction();
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-5;
            }
        };
        PointValuePair solution = optimizer.optimize(function, 0.0, 1.0, 1e-8, 1e-8, customChecker);
        assertEquals(0.0, solution.getPoint(), 1e-7);
        assertEquals(0.0, solution.getValue(), 1e-7);
        assertIterationCount(100, optimizer);

        // Test with function with two local minima and custom checker
        UnivariateFunction twoMinimaFunction = new TwoMinimaFunction();
        PointValuePair solutionTwoMinima = optimizer.optimize(twoMinimaFunction, -5.0, 5.0, 1e-8, 1e-8, customChecker);
        assertEquals(-3.0, solutionTwoMinima.getPoint(), 1e-7);
        assertEquals(4.012202145493302E-14, solutionTwoMinima.getValue(), 1e-12);
        assertIterationCount(101, optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, double)}.
     */
    @org.junit.Test
    public void testOptimizeWithDefaultGoalType() {
        // Test with simple function
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.AbsFunction();
        PointValuePair solution = optimizer.optimize(function, 0.0, 1.0, 1e-8, 1e-8, null, GoalType.MINIMIZE);
        assertEquals(0.0, solution.getPoint(), 1e-7);
        assertEquals(0.0, solution.getValue(), 1e-7);

        // Test with function with two local minima
        UnivariateFunction twoMinimaFunction = new TwoMinimaFunction();
        PointValuePair solutionTwoMinima = optimizer.optimize(twoMinimaFunction, -5.0, 5.0, 1e-8, 1e-8, null, GoalType.MINIMIZE);
        assertEquals(-3.0, solutionTwoMinima.getPoint(), 1e-7);
        assertEquals(4.012202145493302E-14, solutionTwoMinima.getValue(), 1e-12);

        // Test with function with no real root
        UnivariateFunction noRootFunction = new NoRootFunction();
        PointValuePair solutionNoRoot = optimizer.optimize(noRootFunction, -10.0, 10.0, 1e-8, 1e-8, null, GoalType.MINIMIZE);
        assertNull(solutionNoRoot);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, double, ConvergenceChecker, GoalType)}.
     */
    @org.junit.Test
    public void testOptimizeWithDefaultGoalTypeAndChecker() {
        // Test with simple function and custom checker
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.AbsFunction();
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-5;
            }
        };
        PointValuePair solution = optimizer.optimize(function, 0.0, 1.0, 1e-8, 1e-8, customChecker, GoalType.MINIMIZE);
        assertEquals(0.0, solution.getPoint(), 1e-7);
        assertEquals(0.0, solution.getValue(), 1e-7);
        assertIterationCount(100, optimizer);

        // Test with function with two local minima and custom checker
        UnivariateFunction twoMinimaFunction = new TwoMinimaFunction();
        PointValuePair solutionTwoMinima = optimizer.optimize(twoMinimaFunction, -5.0, 5.0, 1e-8, 1e-8, customChecker, GoalType.MINIMIZE);
        assertEquals(-3.0, solutionTwoMinima.getPoint(), 1e-7);
        assertEquals(4.012202145493302E-14, solutionTwoMinima.getValue(), 1e-12);
        assertIterationCount(101, optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, double, ConvergenceChecker, GoalType)}.
     */
    @org.junit.Test
    public void testOptimizeWithCustomGoalType() {
        // Test with simple function
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.AbsFunction();
        PointValuePair solution = optimizer.optimize(function, 0.0, 1.0, 1e-8, 1e-8, null, GoalType.MAXIMIZE);
        assertEquals(1.0, solution.getPoint(), 1e-7);
        assertEquals(1.0, solution.getValue(), 1e-7);

        // Test with function with two local minima
        UnivariateFunction twoMinimaFunction = new TwoMinimaFunction();
        PointValuePair solutionTwoMinima = optimizer.optimize(twoMinimaFunction, -5.0, 5.0, 1e-8, 1e-8, null, GoalType.MAXIMIZE);
        assertEquals(3.0, solutionTwoMinima.getPoint(), 1e-7);
        assertEquals(4.012202145493302E-14, solutionTwoMinima.getValue(), 1e-12);

        // Test with function with no real root
        UnivariateFunction noRootFunction = new NoRootFunction();
        PointValuePair solutionNoRoot = optimizer.optimize(noRootFunction, -10.0, 10.0, 1e-8, 1e-8, null, GoalType.MAXIMIZE);
        assertNull(solutionNoRoot);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, double, ConvergenceChecker, GoalType)}.
     */
    @org.junit.Test
    public void testOptimizeWithCustomGoalTypeAndChecker() {
        // Test with simple function and custom checker
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = new Math.AbsFunction();
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-5;
            }
        };
        PointValuePair solution = optimizer.optimize(function, 0.0, 1.0, 1e-8, 1e-8, customChecker, GoalType.MAXIMIZE);
        assertEquals(1.0, solution.getPoint(), 1e-7);
        assertEquals(1.0, solution.getValue(), 1e-7);
        assertIterationCount(100, optimizer);

        // Test with function with two local minima and custom checker
        UnivariateFunction twoMinimaFunction = new TwoMinimaFunction();
        PointValuePair solutionTwoMinima = optimizer.optimize(twoMinimaFunction, -5.0, 5.0, 1e-8, 1e-8, customChecker, GoalType.MAXIMIZE);
        assertEquals(3.0, solutionTwoMinima.getPoint(), 1e-7);
        assertEquals(4.012202145493302E-14, solutionTwoMinima.getValue(), 1e-12);
        assertIterationCount(101, optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, double, ConvergenceChecker, GoalType)}.
     */
    @org.junit.Test
    public void testOptimizeWithExceptionalInput() {
        // Test with negative threshold
        try {
            new BrentOptimizer(-0.1, 0.1);
            fail("Expected IllegalArgumentException with negative threshold");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with zero threshold
        try {
            new BrentOptimizer(0.0, 0.0);
            fail("Expected IllegalArgumentException with zero threshold");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with negative absolute threshold
        try {
            new BrentOptimizer(0.1, -0.1);
            fail("Expected IllegalArgumentException with negative absolute threshold");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with very small threshold
        try {
            new BrentOptimizer(1e-15, 1e-8);
            fail("Expected IllegalArgumentException with very small threshold");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null checker
        try {
            new BrentOptimizer(1e-8, 1e-8, null);
            fail("Expected NullPointerException with null checker");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with null function
        try {
            new BrentOptimizer(1e-8, 1e-8, null, null, new Math.AbsFunction(), null);
            fail("Expected NullPointerException with null function");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    private static void assertIterationCount(int expectedIterations, BrentOptimizer optimizer) {
        assertEquals(expectedIterations, optimizer.getIterationCount());
    }

}

class Math {

    static class AbsFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.abs(x);
        }
    }
}

class TwoMinimaFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        double value = 0.0;
        if (x < -2.0) {
            value = Math.pow(x + 2.0, 2) - 1.0;
        } else if (x > 2.0) {
            value = Math.pow(x - 2.0, 2) + 1.0;
        } else {
            value = 0.0;
        }
        return value;
    }
}
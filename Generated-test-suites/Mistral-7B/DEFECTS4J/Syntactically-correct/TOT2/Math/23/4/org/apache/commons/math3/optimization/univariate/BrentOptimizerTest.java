package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @org.junit.Test
    public void testBrentOptimizer() {
        final double eps = 1e-9;

        // Test with default constructor
        final BrentOptimizer optimizer = new BrentOptimizer(null);
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.sin(x)) - 1;
            }
        };
        final UnivariatePointValuePair result = optimizer.optimize(function, 0, 10, GoalType.MINIMIZE);
        assertEquals(Math.E, result.getPoint(), eps);

        // Test with constructor with different tolerances
        final BrentOptimizer optimizerWithTolerance
            = new BrentOptimizer(0.001, 0.01);
        final UnivariatePointValuePair resultWithTolerance
            = optimizerWithTolerance.optimize(function, 0, 10, GoalType.MINIMIZE);
        assertEquals(Math.E, resultWithTolerance.getPoint(), eps + 0.001);

        // Test with constructor with ConvergenceChecker
        final ConvergenceChecker<UnivariatePointValuePair> checker
            = new MaxEval(new MaxEval.DefaultMaxIter(100));
        final BrentOptimizer optimizerWithChecker
            = new BrentOptimizer(0.001, 0.01, checker);
        final UnivariatePointValuePair resultWithChecker
            = optimizerWithChecker.optimize(function, 0, 10, GoalType.MINIMIZE);
        assertEquals(Math.E, resultWithChecker.getPoint(), eps + 0.001);

        // Test with constructor with wrong input
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(-0.001, 0.01));
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(0.0, 0.0));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, GoalType)}.
     */
    @org.junit.Test
    public void testOptimize() {
        final double eps = 1e-9;

        // Test with default constructor
        final BrentOptimizer optimizer = new BrentOptimizer(null);
        final UnivariateFunction function = x -> Math.pow(Math.E, Math.sin(x)) - 1;
        final UnivariatePointValuePair result = optimizer.optimize(function, 0, 10, GoalType.MINIMIZE);
        assertEquals(Math.E, result.getPoint(), eps);

        // Test with function that returns NaN
        final UnivariateFunction functionNaN = x -> Double.NaN;
        final UnsupportedOperationException exception
            = assertThrows(UnsupportedOperationException.class, () -> optimizer.optimize(functionNaN, 0, 10, GoalType.MINIMIZE));
        assertEquals("Function value is not defined for input " + 0, exception.getMessage());

        // Test with function that returns infinity
        final UnivariateFunction functionInf = x -> Double.POSITIVE_INFINITY;
        final UnsupportedOperationException exceptionInf
            = assertThrows(UnsupportedOperationException.class, () -> optimizer.optimize(functionInf, 0, 10, GoalType.MINIMIZE));
        assertEquals("Function value is not defined for input " + 0, exceptionInf.getMessage());

        // Test with function that returns negative value
        final UnivariateFunction functionNeg = x -> -1 * Math.pow(Math.E, Math.sin(x));
        final UnivariatePointValuePair resultNeg
            = optimizer.optimize(functionNeg, 0, 10, GoalType.MAXIMIZE);
        assertEquals(-Math.E, resultNeg.getPoint(), eps);

        // Test with function that has a local minimum at the boundary
        final UnivariateFunction functionBoundary
            = x -> Math.pow(Math.E, Math.sin(x)) - E.EULER;
        final UnivariatePointValuePair resultBoundary
            = optimizer.optimize(functionBoundary, 0, 10, GoalType.MINIMIZE);
        assertEquals(0, resultBoundary.getPoint(), eps);

        // Test with function that has a local minimum at a root of cosine
        final UnivariateFunction functionCosine
            = x -> Math.pow(Math.E, Math.sin(Math.PI / 2 - x)) - 1;
        final UnivariatePointValuePair resultCosine
            = optimizer.optimize(functionCosine, 1, 2, GoalType.MINIMIZE);
        assertEquals(Math.PI / 2, resultCosine.getPoint(), eps);

        // Test with function that has a local minimum between two roots of cosine
        final UnivariateFunction functionCosine2
            = x -> Math.pow(Math.E, Math.sin(Math.PI / 4 - x)) - 1;
        final UnivariatePointValuePair resultCosine2
            = optimizer.optimize(functionCosine2, 0, 2, GoalType.MINIMIZE);
        assertEquals(Math.PI / 4, resultCosine2.getPoint(), eps);
    }
}
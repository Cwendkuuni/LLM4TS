package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateRealFunction;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(3, new double[]{-1.0, 1.0, 2.0});
        double min = -2.0;
        double max = 2.0;
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
            double expected = -1.0;
            assertEquals("Minimum not found correctly", expected, result, 1e-10);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("Exception should not be thrown for this test case");
        }

        // Test with a function which has no minimum or maximum in the given interval
        UnivariateRealFunction f2 = new PolynomialFunction(3, new double[]{1.0, 1.0, -1.0});
        try {
            optimizer.optimize(f2, GoalType.MINIMIZE, min, max);
            fail("No minimum or maximum found in the given interval");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        try {
            optimizer.optimize(f2, GoalType.MAXIMIZE, min, max);
            fail("No maximum or minimum found in the given interval");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test with a function with multiple minima or maxima
        UnivariateRealFunction f3 = new PolynomialFunction(3, new double[]{-1.0, 4.0, 5.0});
        try {
            double result = optimizer.optimize(f3, GoalType.MINIMIZE, min, max);
            double expected = -3.0;
            assertEquals("Minimum not found correctly", expected, result, 1e-10);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("Exception should not be thrown for this test case");
        }

        try {
            double result = optimizer.optimize(f3, GoalType.MAXIMIZE, min, max);
            double expected = 3.0;
            assertEquals("Maximum not found correctly", expected, result, 1e-10);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("Exception should not be thrown for this test case");
        }

        // Test with a function which throws exceptions during optimization
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws NotStrictlyPositiveException, FunctionEvaluationException {
                throw new UnsupportedOperationException();
            }
        };
        try {
            optimizer.optimize(f4, GoalType.MINIMIZE, min, max);
            fail("Function evaluation exception should be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        } catch (MaxIterationsExceededException | FunctionEvaluationException | NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double)}.
     */
    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(3, new double[]{-1.0, 1.0, 2.0});
        double min = -2.0;
        double startValue = 0.0;
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, min, startValue);
            double expected = -1.0;
            assertEquals("Minimum not found correctly", expected, result, 1e-10);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("Exception should not be thrown for this test case");
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithStartValueAndInterval() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new PolynomialFunction(3, new double[]{-1.0, 1.0, 2.0});
        double min = -2.0;
        double max = 2.0;
        double startValue = 0.0;
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, min, startValue, max);
            double expected = -1.0;
            assertEquals("Minimum not found correctly", expected, result, 1e-10);
        } catch (MaxIterationsExceededException | NotStrictlyPositiveException | FunctionEvaluationException e) {
            fail("Exception should not be thrown for this test case");
        }
    }
}
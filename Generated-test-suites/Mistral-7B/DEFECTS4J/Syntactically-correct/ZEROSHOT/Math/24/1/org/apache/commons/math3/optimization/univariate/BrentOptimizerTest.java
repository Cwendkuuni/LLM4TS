package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * JUnit 4 test file for the BrentOptimizer class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with a custom ConvergenceChecker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        Function func = new Function() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 3) - 4 * FastMath.pow(x, 2) + x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.e-6, 1.e-6, new MyConvergenceChecker());
        UnivariateOptimizerResult result = optimizer.optimize(func, null, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(1.6741596163535214, 1.3325633820934943E-13);
        Assert.assertEquals(expected.getPoint(), result.getPoint(), 1.e-9);
        Assert.assertEquals(expected.getValue(), result.getValue(), 1.e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with the default ConvergenceChecker.
     */
    @Test
    public void testDefaultConvergenceChecker() {
        Function func = x -> Math.pow(x, 2) - 4 * x + 4;
        BrentOptimizer optimizer = new BrentOptimizer(1.e-6, 1.e-6);
        UnivariateOptimizerResult result = optimizer.optimize(func, null, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(-2.0, 0.0);
        Assert.assertEquals(expected.getPoint(), result.getPoint(), 1.e-9);
        Assert.assertEquals(expected.getValue(), result.getValue(), 1.e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with the default ConvergenceChecker and a function with multiple local minima.
     */
    @Test
    public void testMultipleLocalMinima() {
        Function func = x -> Math.pow(Math.abs(x), 2) - 1;
        BrentOptimizer optimizer = new BrentOptimizer(1.e-6, 1.e-6);
        Optional<UnivariateOptimizerResult> result = optimizer.optimize(func, null, null);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(-1.0, 0.0);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(expected.getPoint(), result.get().getPoint(), 1.e-9);
        Assert.assertEquals(expected.getValue(), result.get().getValue(), 1.e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double)}
     * with the default ConvergenceChecker and a function with a singularity.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSingularity() {
        Function func = x -> Math.log(Math.abs(x - 1));
        BrentOptimizer optimizer = new BrentOptimizer(1.e-12);
        optimizer.optimize(func, null, null);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with the default ConvergenceChecker and a function with a negative absolute threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeAbsoluteThreshold() {
        Function func = x -> x * x;
        BrentOptimizer optimizer = new BrentOptimizer(1.e-6, -1.e-6);
        optimizer.optimize(func, null, null);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with the default ConvergenceChecker and a function with a relative threshold smaller than machine precision.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        Function func = x -> x * x;
        BrentOptimizer optimizer = new BrentOptimizer(1.e-16, 1.e-6);
        optimizer.optimize(func, null, null);
    }

    /**
     * Custom ConvergenceChecker implementation to test the convergence criterion.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return Precondition.checkPositive("iteration", iteration, true) &&
                   Precondition.checkPositive("previous value", previous.getValue(), true) &&
                   Precondition.checkPositive("current value", current.getValue(), true) &&
                   FastMath.abs(current.getValue() - previous.getValue()) < 1.e-12;
        }
    }

    /**
     * Interface to check the positivity of a double value.
     */
    private static class Precondition {
        /**
         * Checks whether a given double value is positive.
         *
         * @param name the name of the value to check
         * @param value the value to check
         * @param requireNonNull if true, throws a NullPointerException if the value is null
         * @return true if the value is positive, otherwise an exception is thrown
         */
        public static boolean checkPositive(String name, double value, boolean requireNonNull) {
            if (requireNonNull && value == null) {
                throw new NullPointerException("The \"" + name + "\" must not be null.");
            }
            if (value <= 0) {
                throw new IllegalArgumentException("The \"" + name + "\" (" + value + ") must be positive.");
            }
            return true;
        }
    }

    /**
     * Function interface for testing purposes.
     */
    private interface Function {
        double value(double x);
    }
}
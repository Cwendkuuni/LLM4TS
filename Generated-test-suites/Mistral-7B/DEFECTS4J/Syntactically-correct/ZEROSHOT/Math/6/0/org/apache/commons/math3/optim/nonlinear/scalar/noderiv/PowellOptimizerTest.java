package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Assert;

import java.util.Arrays;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    /**
     * Minimum relative tolerance.
     */
    private static final double MIN_RELATIVE_TOLERANCE = 2 * Math.ulp(1d);
    /**
     * Relative threshold.
     */
    private final double relativeThreshold = 1e-5;
    /**
     * Absolute threshold.
     */
    private final double absoluteThreshold = 1e-5;

    /**
     * Test PowellOptimizer constructor with default convergence checking procedure.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test PowellOptimizer constructor with user-defined convergence checker.
     */
    @org.junit.Test
    public void testUserDefinedConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                double[] previousValues = previous.getPoint();
                double[] currentValues = current.getPoint();
                double[] currentGradient = new double[previousValues.length];
                double[] previousGradient = new double[previousValues.length];
                PowellOptimizer.PowellOptimizerTest.getGradient(previousValues, PowellOptimizer.this::computeObjectiveGradient);
                PowellOptimizer.PowellOptimizerTest.getGradient(currentValues, PowellOptimizer.this::computeObjectiveGradient);
                Arrays.systemOutPrintln("Previous: " + Arrays.toString(previousValues) + ", Gradient: " + Arrays.toString(previousGradient));
                Arrays.systemOutPrintln("Current: " + Arrays.toString(currentValues) + ", Gradient: " + Arrays.toString(currentGradient));
                return true;
            }
        });
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test PowellOptimizer minimum.
     */
    @org.junit.Test
    public void testMinimum() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new PowellOptimizerTestFunction());
        double[] point = result.getPoint();
        double value = result.getValue();
        Arrays.systemOutPrintln("Point: " + Arrays.toString(point) + ", Value: " + value);
        Assert.assertEquals(0.0, value, 1e-6);
    }

    /**
     * Test PowellOptimizer maximum.
     */
    @org.junit.Test
    public void testMaximum() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new PowellOptimizerTestFunctionMax());
        double[] point = result.getPoint();
        double value = result.getValue();
        Arrays.systemOutPrintln("Point: " + Arrays.toString(point) + ", Value: " + value);
        Assert.assertEquals(2.0, value, 1e-6);
    }

    /**
     * Test PowellOptimizer with custom convergence checker.
     */
    @org.junit.Test
    public void testCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                double[] previousValues = previous.getPoint();
                double[] currentValues = current.getPoint();
                double[] previousGradient = new double[previousValues.length];
                double[] currentGradient = new double[currentValues.length];
                PowellOptimizer.PowellOptimizerTest.getGradient(previousValues, PowellOptimizer.this::computeObjectiveGradient);
                PowellOptimizer.PowellOptimizerTest.getGradient(currentValues, PowellOptimizer.this::computeObjectiveGradient);
                Arrays.systemOutPrintln("Previous: " + Arrays.toString(previousValues) + ", Gradient: " + Arrays.toString(previousGradient));
                Arrays.systemOutPrintln("Current: " + Arrays.toString(currentValues) + ", Gradient: " + Arrays.toString(currentGradient));
                double[] difference = new double[previousValues.length];
                for (int i = 0; i < difference.length; i++) {
                    difference[i] = currentValues[i] - previousValues[i];
                }
                double norm = Math.sqrt(Math.dot(difference, difference));
                Arrays.systemOutPrintln("Norm: " + norm);
                return Math.abs(norm) < MIN_RELATIVE_TOLERANCE;
            }
        });
        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(new PowellOptimizerTestFunction());
        double[] point = result.getPoint();
        double value = result.getValue();
        Arrays.systemOutPrintln("Point: " + Arrays.toString(point) + ", Value: " + value);
        Assert.assertEquals(0.0, value, 1e-6);

        // Test with a non-converged optimization
        PowellOptimizer optimizer2 = new PowellOptimizer(relativeThreshold, absoluteThreshold, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return false;
            }
        });
        double[] startPoint2 = {1.0, 1.0};
        PointValuePair result2 = optimizer2.optimize(new PowellOptimizerTestFunction());
        double[] point2 = result2.getPoint();
        double value2 = result2.getValue();
        Arrays.systemOutPrintln("Point: " + Arrays.toString(point2) + ", Value: " + value2);
        Assert.fail();
    }

    /**
     * Test line search.
     */
    @org.junit.Test
    public void testLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
        double[] startPoint = {1.0, 1.0};
        double[] direction = {1.0, 1.0};
        PointValuePair result = optimizer.lineSearch(startPoint, direction);
        double[] point = result.getPoint();
        double value = result.getValue();
        double expectedValue = Math.sqrt(2.0) * Math.sqrt(Math.PI);
        Arrays.systemOutPrintln("Point: " + Arrays.toString(point) + ", Value: " + value);
        Assert.assertEquals(expectedValue, value, 1e-6);
    }

    /**
     * Test objective function gradient.
     */
    private static void getGradient(double[] values, MultivariateFunction function) {
        double[] gradient = new double[values.length];
        for (int i = 0; i < gradient.length; i++) {
            double[] testPoint = new double[]{values[i]};
            gradient[i] = function.value(testPoint, new double[values.length])[0] - function.value(values);
        }
    }

    /**
     * Test objective function.
     */
    private double computeObjectiveValue(double[] values) {
        return Math.pow(Math.sin(Math.sqrt(Math.pow(values[0], 2) + Math.pow(values[1], 2))), 2) + values[0] * values[1];
    }

    /**
     * Test objective function gradient.
     */
    private double computeObjectiveGradient(double[] values) {
        double[] gradient = new double[values.length];
        for (int i = 0; i < gradient.length; i++) {
            double[] testPoint = new double[]{values[i]};
            double dFdx = function.value(testPoint, new double[values.length], new double[]{values[0] + 1e-8, values[1]})[0] - function.value(values)[0];
            gradient[i] = dFdx;
        }
        return Math.sqrt(Math.pow(gradient[0], 2) + Math.pow(gradient[1], 2));
    }

    /**
     * Test function for PowellOptimizer.
     */
    private static class PowellOptimizerTestFunction implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            return PowellOptimizerTest.this.computeObjectiveValue(x);
        }

        @Override
        public int getNumberOfVariables() {
            return 2;
        }
    }

    /**
     * Test function for PowellOptimizer (maximum).
     */
    private static class PowellOptimizerTestFunctionMax implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            return PowellOptimizerTest.this.computeObjectiveValue(x) * -1;
        }

        @Override
        public int getNumberOfVariables() {
            return 2;
        }
    }
}
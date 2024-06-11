package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.InitialGuess.RandomInitialStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class BrentOptimizerTest {

    private static UnivariateOptimizer optimizer;
    private static Random random;

    @BeforeClass
    public static void setUp() {
        optimizer = new BrentOptimizer();
        random = new Random();
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() {
        UnivariateRealFunction function = new Function("x^2");
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(0.0, result, 1e-8);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeInitValue() {
        UnivariateRealFunction function = new Function("x^2");
        double initValue = 5.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, initValue);
        // The optimization result should be less than the initial value
        // because the function is decreasing.
        assertTrue(result < initValue);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a function that doesn't have a minimum in the given interval.
     */
    @Test
    public void testOptimizeNoMinimum() {
        UnivariateRealFunction function = new Function("x");
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        fail("Expected a MaxIterationsExceededException or FunctionEvaluationException.");
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a function that has a minimum outside the given interval.
     */
    @Test
    public void testOptimizeMinimumOutsideInterval() {
        UnivariateRealFunction function = new Function("x^2 - 4");
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        fail("Expected a MaxIterationsExceededException or FunctionEvaluationException.");
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a function that has a relative accuracy less than 2 macheps.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeRelativeAccuracyTooSmall() {
        UnivariateRealFunction function = new Function("x^2");
        optimizer.setRelativeAccuracy(1e-16);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Test with a function that has an absolute accuracy less than 0.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeAbsoluteAccuracyNegative() {
        UnivariateRealFunction function = new Function("x^2");
        optimizer.setAbsoluteAccuracy(-1e-8);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Test with a function that has a maximum number of iterations.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterations() {
        UnivariateRealFunction function = new Function("x^2");
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Test with a function that has a narrow interval.
     */
    @Test
    public void testOptimizeNarrowInterval() {
        UnivariateRealFunction function = new Function("x^2");
        double min = 0.0;
        double max = 0.1;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max);
        // The optimization result should be close to the minimum.
        assertTrue(result > min && result < max);
        // The optimization result should be accurate.
        assertEquals(0.0, result, 5e-5);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Test with a function that has a wide interval.
     */
    @Test
    public void testOptimizeWideInterval() {
        UnivariateRealFunction function = new Function("x^2");
        double min = -10.0;
        double max = 10.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max);
        // The optimization result should be close to the minimum.
        assertTrue(result > min && result < max);
        // The optimization result should be accurate.
        assertEquals(0.0, result, 1e-8);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Test with a random function.
     */
    @Test
    public void testOptimizeRandomFunction() {
        UnivariateRealFunction function = new RandomFunction();
        double min = -10.0;
        double max = 10.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max);
        // The optimization result should be close to the minimum.
        assertTrue(result > min && result < max);
        // The optimization result should be accurate.
        assertEquals(function.getMinimum(), result, 1e-8);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Test with a function that has multiple minima.
     */
    @Test
    public void testOptimizeMultipleMinima() {
        UnivariateRealFunction function = new FunctionMultipleMinima();
        double min = -10.0;
        double max = 10.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max);
        // The optimization result should be one of the minima.
        assertEquals(function.getMinima()[0], result, 1e-8);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Test with a function that throws a FunctionEvaluationException.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() {
        UnivariateRealFunction function = new FunctionEvaluationExceptionFunction();
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    private static class Function implements UnivariateRealFunction {
        private final String name;
        private final double minimum;

        public Function(String name) {
            this.name = name;
            this.minimum = 0.0;
        }

        public Function(String name, double minimum) {
            this.name = name;
            this.minimum = minimum;
        }

        @Override
        public double value(double x) {
            String functionName = this.name;
            if (functionName.startsWith("x^")) {
                double exponent = Double.parseDouble(functionName.substring(2));
                return Math.pow(x, exponent);
            }
            throw new UnsupportedOperationException();
        }

        public double getMinimum() {
            return this.minimum;
        }
    }

    private static class FunctionMultipleMinima extends Function {
        public FunctionMultipleMinima() {
            super("x^2 - 4 * x + 4");
        }

        @Override
        public double[] getMinima() {
            return new double[] {2.0, 1.0};
        }
    }

    private static class FunctionEvaluationExceptionFunction extends Function {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException();
        }
    }

    private static class RandomFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            double value = 0.0;
            for (int i = 0; i < 10; i++) {
                value += Math.random();
            }
            return value;
        }

        public double getMinimum() {
            return 0.0;
        }
    }
}
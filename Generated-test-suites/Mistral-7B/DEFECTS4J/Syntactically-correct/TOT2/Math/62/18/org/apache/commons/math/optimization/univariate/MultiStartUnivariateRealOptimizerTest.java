package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.initializer.UniformInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.statistics.DescriptiveStatistics;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartMinimization() throws Exception {
        // Test optimizer with simple test function and default parameters
        UnivariateRealFunction testFunction = new MyTestFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        PointValuePair result = multiStartOptimizer.optimize(testFunction, goal, min, max);

        assertArrayEquals(new double[]{result.getPoint()[0]}, new double[]{-0.23606798}, 0.001);
        assertEquals(result.getValue(), -1.23606798, 0.001);

        // Test optimizer with multiple starts and different bounds
        UnivariateRealFunction testFunction2 = new MyTestFunction2();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer2 =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal2 = GoalType.MINIMIZE;
        double min2 = -5.0;
        double max2 = 5.0;

        PointValuePair[] results = new PointValuePair[multiStartOptimizer2.getStarts()];
        for (int i = 0; i < multiStartOptimizer2.getStarts(); ++i) {
            results[i] = multiStartOptimizer2.optimize(testFunction2, goal2, min2, max2);
        }

        double[] xValues = new double[multiStartOptimizer2.getStarts()];
        double[] fValues = new double[multiStartOptimizer2.getStarts()];

        for (int i = 0; i < multiStartOptimizer2.getStarts(); ++i) {
            xValues[i] = results[i].getPoint()[0];
            fValues[i] = results[i].getValue();
        }

        assertArrayEquals(xValues, new double[]{-1.54128594, -0.65764944, 0.65764944, 1.54128594, 2.43561969}, 0.01);
        assertArrayEquals(fValues, new double[]{0.26422336, 0.14311168, 0.14311168, 0.26422336, 0.71112773}, 0.01);

        // Test error handling
        UnivariateRealFunction testFunction3 = new MyTestFunction3();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer3 =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal3 = GoalType.MINIMIZE;
        double min3 = -5.0;
        double max3 = 5.0;

        try {
            multiStartOptimizer3.optimize(testFunction3, goal3, min3, max3);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            multiStartOptimizer3.optimize(testFunction3, goal3, min3, max3, -1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // Test exception handling with custom exception
        UnivariateRealFunction testFunction4 = new MyTestFunction4();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer4 =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal4 = GoalType.MINIMIZE;
        double min4 = -5.0;
        double max4 = 5.0;

        try {
            multiStartOptimizer4.optimize(testFunction4, goal4, min4, max4);
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Expected
        }

        // Test exception handling with null function
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer5 =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        try {
            multiStartOptimizer5.optimize(null, goal, min, max);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMultiStartMaximization() throws Exception {
        // Test optimizer with simple test function and default parameters
        UnivariateRealFunction testFunction = new MyTestFunction();
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;
        PointValuePair result = multiStartOptimizer.optimize(testFunction, goal, min, max);

        assertArrayEquals(new double[]{result.getPoint()[0]}, new double[]{0.23606798}, 0.001);
        assertEquals(result.getValue(), 1.23606798, 0.001);

        // Test optimizer with multiple starts and different bounds
        UnivariateRealFunction testFunction2 = new MyTestFunction2();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer2 =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal2 = GoalType.MAXIMIZE;
        double min2 = -5.0;
        double max2 = 5.0;

        PointValuePair[] results = new PointValuePair[multiStartOptimizer2.getStarts()];
        for (int i = 0; i < multiStartOptimizer2.getStarts(); ++i) {
            results[i] = multiStartOptimizer2.optimize(testFunction2, goal2, min2, max2);
        }

        double[] xValues = new double[multiStartOptimizer2.getStarts()];
        double[] fValues = new double[multiStartOptimizer2.getStarts()];

        for (int i = 0; i < multiStartOptimizer2.getStarts(); ++i) {
            xValues[i] = results[i].getPoint()[0];
            fValues[i] = results[i].getValue();
        }

        assertArrayEquals(xValues, new double[]{2.43561969, 1.54128594, -1.54128594, -0.65764944, -0.65764944}, 0.01);
        assertArrayEquals(fValues, new double[]{0.71112773, 0.26422336, 0.26422336, 0.14311168, 0.14311168}, 0.01);

        // Test error handling
        UnivariateRealFunction testFunction3 = new MyTestFunction3();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer3 =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal3 = GoalType.MAXIMIZE;
        double min3 = -5.0;
        double max3 = 5.0;

        try {
            multiStartOptimizer3.optimize(testFunction3, goal3, min3, max3);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            multiStartOptimizer3.optimize(testFunction3, goal3, min3, max3, -1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // Test exception handling with custom exception
        UnivariateRealFunction testFunction4 = new MyTestFunction4();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer4 =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal4 = GoalType.MAXIMIZE;
        double min4 = -5.0;
        double max4 = 5.0;

        try {
            multiStartOptimizer4.optimize(testFunction4, goal4, min4, max4);
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Expected
        }

        // Test exception handling with null function
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer5 =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        try {
            multiStartOptimizer5.optimize(null, goal, min, max);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private static class MyTestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + 0.1 * x;
        }
    }

    private static class MyTestFunction2 implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(FastMath.PI * x), 2) + Math.pow(x - 2, 2);
        }
    }

    private static class MyTestFunction3 implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new ConvergenceException("Test ConvergenceException");
        }
    }

    private static class MyTestFunction4 implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new MathIllegalStateException("Test MathIllegalStateException");
        }
    }

    @Test
    public void testGetOptima() throws Exception {
        // Test optimizer with simple test function and default parameters
        UnivariateRealFunction testFunction = new MyTestFunction();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 5, new MersenneTwister());

        GoalType goal = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        multiStartOptimizer.optimize(testFunction, goal, min, max);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(optima[0].getPoint(), new double[]{0.23606798}, 0.001);
        assertEquals(optima[0].getValue(), -1.23606798, 0.001);
        assertNull(optima[1]);

        // Test optimizer with multiple starts and different bounds
        UnivariateRealFunction testFunction2 = new MyTestFunction2();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer2 =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 5, new MersenneTwister());

        GoalType goal2 = GoalType.MINIMIZE;
        double min2 = -5.0;
        double max2 = 5.0;
        multiStartOptimizer2.optimize(testFunction2, goal2, min2, max2);

        UnivariateRealPointValuePair[] optima2 = multiStartOptimizer2.getOptima();

        assertArrayEquals(optima2[0].getPoint(), new double[]{-0.65764944}, 0.01);
        assertEquals(optima2[0].getValue(), 0.14311168, 0.01);
        assertArrayEquals(optima2[1].getPoint(), new double[]{1.54128594}, 0.01);
        assertEquals(optima2[1].getValue(), 0.14311168, 0.01);
        assertNull(optima2[2]);
        assertNull(optima2[3]);
        assertNull(optima2[4]);
    }
}
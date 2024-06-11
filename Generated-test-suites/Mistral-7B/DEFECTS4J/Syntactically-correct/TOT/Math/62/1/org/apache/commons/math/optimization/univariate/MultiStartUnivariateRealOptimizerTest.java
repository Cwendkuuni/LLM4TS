package org.apache.commons.math.optimization.univariate;

import junit.framework.TestCase;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingMethod;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.random.RandomDataGenerators;
import org.apache.commons.math3.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest extends TestCase {

    /**
     * Test that the optimizer finds the minimum of a simple function with
     * multiple starts.
     * @throws Exception if an error occurs
     */
    public void testMinimumWithMultipleStarts() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x - 3.0, 2.0);
            }

            public double derivative(double x) {
                return 2.0 * (x - 3.0);
            }
        };

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerators.UniformRealGenerator(0.0, 1.0));

        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        PointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        double expected = 3.0;
        assertEquals(expected, result.getPoint(), 0.001);
        assertEquals(expected, result.getValue(), 0.001);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(10, optima.length);
        for (UnivariateRealPointValuePair pointValuePair : optima) {
            if (pointValuePair != null) {
                assertEquals(expected, pointValuePair.getPoint(), 0.001);
                assertEquals(expected, pointValuePair.getValue(), 0.001);
            }
        }
    }

    /**
     * Test that the optimizer finds the maximum of a simple function with
     * multiple starts.
     * @throws Exception if an error occurs
     */
    public void testMaximumWithMultipleStarts() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return -Math.pow(x + 3.0, 2.0);
            }

            public double derivative(double x) {
                return -2.0 * (x + 3.0);
            }
        };

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerators.UniformRealGenerator(0.0, 1.0));

        GoalType goal = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 10.0;

        PointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        double expected = -3.0;
        assertEquals(expected, result.getPoint(), 0.001);
        assertEquals(expected, result.getValue(), 0.001);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(10, optima.length);
        for (UnivariateRealPointValuePair pointValuePair : optima) {
            if (pointValuePair != null) {
                assertEquals(expected, pointValuePair.getPoint(), 0.001);
                assertEquals(expected, pointValuePair.getValue(), 0.001);
            }
        }
    }

    /**
     * Test that the optimizer throws an exception if the function is not
     * differentiable at a point.
     * @throws Exception if an error occurs
     */
    public void testUndifferentiability() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 0.0) {
                    throw new UnsupportedOperationException("Derivative is not defined at x = 0.");
                }
                return Math.pow(x, 2.0);
            }

            public double derivative(double x) {
                return 2.0 * x;
            }
        };

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerators.UniformRealGenerator(0.0, 1.0));

        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        try {
            multiStartOptimizer.optimize(f, goal, min, max);
            fail("An exception should have been thrown.");
        } catch (ConvergenceException e) {
            // Expected exception
        }

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertNull(optima);
    }

    /**
     * Test that the optimizer throws an exception if the function is not
     * bounded.
     * @throws Exception if an error occurs
     */
    public void testUnboundedFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 1.0 / x;
            }

            public double derivative(double x) {
                return -1.0 / (x * x);
            }
        };

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerators.UniformRealGenerator(0.0, 1.0));

        GoalType goal = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 10.0;

        try {
            multiStartOptimizer.optimize(f, goal, min, max);
            fail("An exception should have been thrown.");
        } catch (MaxEvalException e) {
            // Expected exception
        }

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertNull(optima);
    }
}
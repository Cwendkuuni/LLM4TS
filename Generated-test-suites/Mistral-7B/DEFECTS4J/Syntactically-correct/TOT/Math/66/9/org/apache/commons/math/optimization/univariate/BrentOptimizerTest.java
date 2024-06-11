package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializers.AverageInitializer;
import org.apache.commons.math3.optim.nonlinear.scalers.ExpScaler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function 1: {@code f(x) = x^3 - 2*x^2 + x}.
     */
    @Test
    public void testFunction1() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(-5, 5);
        PointValuePair result;

        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE, bounds);
            double expected = -1.0;
            Assert.assertEquals(expected, result.getPoint(), 1e-5);
            Assert.assertEquals(expected, result.getValue(), 1e-5);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Optimization should have converged: " + e.getMessage());
        }
    }

    /**
     * Test function 2: {@code f(x) = Math.exp(x) - x - 1}.
     */
    @Test
    public void testFunction2() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - x - 1;
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(-5, 5);
        bounds.setScaler(new ExpScaler());
        AverageInitializer init = new AverageInitializer(2);
        optimizer.setInitialSteps(init);

        try {
            PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, bounds);
            double expected = Math.log(e) - (1.0 + Math.E) / 2.0;
            Assert.assertEquals(expected, result.getPoint(), 1e-5);
            Assert.assertEquals(expected, result.getValue(), 1e-5);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Optimization should have converged: " + e.getMessage());
        }
    }

    /**
     * Test function 3: {@code f(x) = Math.pow(Math.abs(x), 2)}.
     */
    @Test
    public void testFunction3() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x), 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(-5, 5);

        try {
            PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, bounds);
            double expected = 0.0;
            Assert.assertEquals(expected, result.getPoint(), 1e-5);
            Assert.assertEquals(expected, result.getValue(), 1e-5);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Optimization should have converged: " + e.getMessage());
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Bounds should be strictly positive: " + e.getMessage());
        }
    }

    /**
     * Test function 4: {@code f(x) = 1 / x}.
     */
    @Test
    public void testFunction4() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new ArithmeticException("Divide by zero");
                }
                return 1.0 / x;
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(1, 10);

        try {
            PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, bounds);
            double expected = 1.0 / Double.MAX_VALUE;
            Assert.assertEquals(expected, result.getPoint(), 1e-5);
            Assert.assertEquals(expected, result.getValue(), 1e-5);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Optimization should have converged: " + e.getMessage());
        } catch (FunctionEvaluationException e) {
            Assert.assertTrue(e.getMessage().contains("Divide by zero"));
        }
    }
}
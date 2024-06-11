package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.initial_guess.RandomInitialGuess;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.NDerivativeUnivariateFunction;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.ValueChecker;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test for a simple multivariate function optimization problem.
     */
    @Test
    public void testSimpleFunction() {
        MultivariateFunction function = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-2.0, -1.0}}, 2);
        Optimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-6, 1e-6));

        PointValuePair result = optimizer.optimize(function);
        assertNotNull(result);
        assertEquals(-1.0, result.getPoint()[0], 1e-6);
        assertEquals(2.0, result.getPoint()[1], 1e-6);
        assertEquals(-3.0, result.getValue(), 1e-6);
    }

    /**
     * Test for a multivariate function optimization problem with linear
     * constraints.
     */
    @Test
    public void testConstraints() {
        MultivariateFunction function = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-2.0, -1.0}}, 2);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(2, 0.0));
        constraints.addConstraint(new LinearConstraint(1, 1.0));

        Optimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-6, 1e-6), constraints);

        PointValuePair result = optimizer.optimize(function);
        assertNotNull(result);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
        assertEquals(1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for a multivariate function optimization problem with a custom
     * convergence checker.
     */
    @Test
    public void testCustomChecker() {
        MultivariateFunction function = new MyCustomMultivariateFunction();
        Optimizer optimizer = new PowellOptimizer(1e-3, 1e-3, new MyCustomChecker());

        PointValuePair result = optimizer.optimize(function);
        assertNotNull(result);
        assertEquals(-1.0, result.getPoint()[0], 1e-3);
        assertEquals(2.0, result.getPoint()[1], 1e-3);
        assertEquals(-3.0, result.getValue(), 1e-3);
    }

    /**
     * Test for a multivariate function optimization problem with error
     * handling.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testException() {
        MultivariateFunction function = new PowellOptimizerFunction();
        Optimizer optimizer = new PowellOptimizer(-1e-6, 1e-6);

        optimizer.optimize(function);
    }

    /**
     * Test for a multivariate function optimization problem with an
     * incorrect dimension.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDimensionMismatch() {
        MultivariateFunction function = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}}, 3);
        Optimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-6, 1e-6));

        optimizer.optimize(function);
    }

    /**
     * Test for a multivariate function optimization problem with a
     * non-unary function.
     */
    @Test(expected = IllegalStateException.class)
    public void testNonUnaryFunction() {
        MultivariateFunction function = new MyNonUnaryFunction();
        Optimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-6, 1e-6));

        optimizer.optimize(function);
    }

    /**
     * Custom multivariate function for testing.
     */
    private static class MyCustomMultivariateFunction implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            return Math.pow(Math.sin(Math.PI * x[0]), 2.0) + Math.pow(Math.cos(Math.PI * x[1]), 2.0);
        }

        @Override
        public PointValuePair gradient(double[] x) {
            double[] grad = new double[2];
            grad[0] = -2.0 * Math.sin(Math.PI * x[0]) * Math.cos(Math.PI * x[0]);
            grad[1] = -2.0 * Math.cos(Math.PI * x[1]) * Math.sin(Math.PI * x[1]);
            return new PointValuePair(grad, 0.0);
        }
    }

    /**
     * Custom convergence checker for testing.
     */
    private static class MyCustomChecker implements ValueChecker<PointValuePair> {
        @Override
        public boolean isSatisfied(int iteration, PointValuePair previous, PointValuePair current) {
            return Math.abs(previous.getValue() - current.getValue()) < 1e-3;
        }
    }

    /**
     * Multivariate function for testing exceptions.
     */
    private static class PowellOptimizerFunction implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            throw new NumberIsTooSmallException(1.0);
        }

        @Override
        public PointValuePair gradient(double[] x) {
            // Not used
            return null;
        }
    }

    /**
     * Multivariate function for testing non-unary behaviour.
     */
    private static class MyNonUnaryFunction implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            return Math.sin(Math.PI * x[0] * x[1]);
        }

        @Override
        public PointValuePair gradient(double[] x) {
            // Not used
            return null;
        }
    }
}
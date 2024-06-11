package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragePointInitializer;
import org.apache.commons.math3.optim.initializer.GaussianInitializer;
import org.apache.commons.math3.optim.initializer.StartPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.junit.Test;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Tests the Powell optimizer with the Rosenbrock function.
     * @throws NotStrictlyPositiveException if the step size is not positive.
     */
    @Test
    public void testRosenbrock() throws NotStrictlyPositiveException {
        MultivariateFunction function = new RosenbrockFunction();
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        PointValuePair solution = optimizer.optimize(function, new AveragePointInitializer(2));
        assertEquals(new PointValuePair(new double[]{1.0, 1.0}, 0.0), solution);
    }

    /**
     * Tests the Powell optimizer with the Rastrigin function.
     * @throws NotStrictlyPositiveException if the step size is not positive.
     */
    @Test
    public void testRastrigin() throws NotStrictlyPositiveException {
        MultivariateFunction function = new RastriginFunction();
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        PointValuePair solution = optimizer.optimize(function, new GaussianInitializer(2));
        assertEquals(new PointValuePair(new double[]{0.0, 0.0}, 0.0), solution);
    }

    /**
     * Tests the Powell optimizer with the Ackley function.
     * @throws NotStrictlyPositiveException if the step size is not positive.
     */
    @Test
    public void testAckley() throws NotStrictlyPositiveException {
        MultivariateFunction function = new AckleyFunction();
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        PointValuePair solution = optimizer.optimize(function, new StartPointInitializer(new double[]{0.1, 0.1}));
        assertEquals(new PointValuePair(new double[]{0.0, 0.0}, 0.0), solution);
    }

    /**
     * Tests the Powell optimizer with a custom convergence checker.
     * @throws NotStrictlyPositiveException if the step size is not positive.
     */
    @Test
    public void testCustomChecker() throws NotStrictlyPositiveException {
        MultivariateFunction function = new RastriginFunction();
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4, new MyCustomConvergenceChecker());
        PointValuePair solution = optimizer.optimize(function, new StartPointInitializer(new double[]{0.1, 0.1}));
        assertEquals(new PointValuePair(new double[]{0.0, 0.0}, 0.0), solution);
    }

    /**
     * A custom convergence checker that stops the optimization when the
     * number of iterations reaches a given value.
     */
    private static class MyCustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        private final int maxIterations;

        public MyCustomConvergenceChecker(int maxIterations) {
            this.maxIterations = maxIterations;
        }

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return iteration >= maxIterations;
        }
    }

    /**
     * A simple function for testing.
     */
    private static class MySimpleFunction implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            return FunctionUtils.sum(point);
        }

        @Override
        public double[] gradient(double[] point) {
            return new double[]{1.0, 1.0};
        }

        @Override
        public double[][] jacobian(double[] point) {
            return new double[][]{new double[]{1.0, 1.0}, new double[]{1.0, 1.0}};
        }

        @Override
        public double[][] hessian(double[] point) {
            return new double[][]{new double[]{2.0, 1.0}, new double[]{1.0, 2.0}};
        }
    }

    /**
     * A test function for testing the Powell optimizer with custom objective
     * and gradient functions.
     */
    @Test
    public void testCustomObjectiveAndGradient() throws NotStrictlyPositiveException {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return new MySimpleFunction().value(point);
            }

            @Override
            public double[] gradient(double[] point) {
                return new MySimpleFunction().gradient(point);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4);
        PointValuePair solution = optimizer.optimize(function, new AveragePointInitializer(2));
        assertEquals(new PointValuePair(new double[]{0.0, 0.0}, 0.0), solution);
    }

    /**
     * A test function for testing the Powell optimizer with a custom line
     * search.
     * @throws NotStrictlyPositiveException if the step size is not positive.
     */
    @Test
    public void testCustomLineSearch() throws NotStrictlyPositiveException {
        MultivariateFunction function = new MySimpleFunction();
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4, null, null, new MyCustomLineSearch());
        PointValuePair solution = optimizer.optimize(function, new AveragePointInitializer(2));
        assertEquals(new PointValuePair(new double[]{0.0, 0.0}, 0.0), solution);
    }

    /**
     * A custom line search that uses a given step size.
     */
    private static class MyCustomLineSearch implements LineSearch {
        private final double stepSize;

        public MyCustomLineSearch(double stepSize) {
            this.stepSize = stepSize;
        }

        @Override
        public double search(double[] point, double[] direction) {
            return point[0] + stepSize;
        }

        @Override
        public PointValuePair optimize(double stepSize,
                                       double[] start,
                                       double[] direction,
                                       GoalType goalType,
                                       double lowerBound,
                                       double upperBound) {
            return new PointValuePair(new double[]{start[0] + stepSize},
                                      new MySimpleFunction().value(new double[]{start[0] + stepSize}));
        }
    }
}
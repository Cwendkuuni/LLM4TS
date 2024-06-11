package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.*;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testMinimizationWithDefaults() {
        MultivariateFunction function = new QuadraticFunction(new double[]{-1.0, -1.0}, 2.0);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function);
        double[] expectedSolution = new double[]{-1.0, -1.0};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimizationWithInvalidAbsoluteThreshold() {
        MultivariateFunction function = new IdentityFunction(new double[0]);
        PowellOptimizer optimizer = new PowellOptimizer(-1e-6, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimizationWithInvalidRelativeThreshold() {
        MultivariateFunction function = new IdentityFunction(new double[0]);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, -1e-6);
    }

    @Test
    public void testMinimizationWithCustomConvergenceChecker() {
        MultivariateFunction function = new QuadraticFunction(new double[]{-1.0, 1.0}, 4.0);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomConvergenceChecker());
        PointValuePair solution = optimizer.optimize(function);
        double[] expectedSolution = new double[]{-1.0, 1.0};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testMinimizationWithAveragedStartingPoint() {
        MultivariateFunction function = new QuadraticFunction(new double[]{1.0, 1.0}, 16.0);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function, new AveragedStartingPointGenerator(2));
        double[] expectedSolution = new double[]{1.0, 1.0};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testMinimizationWithCustomGoalType() {
        MultivariateFunction function = new QuadraticFunction(new double[]{1.0, 1.0}, 16.0);
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function, GoalType.MAXIMIZE);
        double[] expectedSolution = new double[]{1.0, 1.0};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 1e-6);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 1e-6);
        assertEquals(16.0, solution.getValue(), 1e-6);
    }

    private static class QuadraticFunction implements MultivariateFunction {
        private final double[] origin;
        private final double scale;

        public QuadraticFunction(double[] origin, double scale) {
            this.origin = origin;
            this.scale = scale;
        }

        @Override
        public double value(PointValuePair point) {
            double sum = 0.0;
            for (int i = 0; i < origin.length; i++) {
                double diff = point.getPoint()[i] - origin[i];
                sum += diff * diff;
            }
            return sum * scale;
        }

        @Override
        public PointValuePair gradient(PointValuePair point) {
            double[] gradient = new double[origin.length];
            for (int i = 0; i < origin.length; i++) {
                gradient[i] = 2.0 * (point.getPoint()[i] - origin[i]) * scale;
            }
            return new PointValuePair(point.getPoint(), gradient);
        }
    }

    private static class IdentityFunction implements UnivariateFunction {
        private final double[] origin;

        public IdentityFunction(double[] origin) {
            this.origin = origin;
        }

        @Override
        public double value(double x) {
            double sum = 0.0;
            for (int i = 0; i < origin.length; i++) {
                sum += Math.pow(x - origin[i], 2);
            }
            return sum;
        }
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iterationNumber,
                                PointValuePair previousValue, PointValuePair currentValue) {
            return (Math.abs(previousValue.getValue() - currentValue.getValue()) < 1e-6);
        }
    }
}
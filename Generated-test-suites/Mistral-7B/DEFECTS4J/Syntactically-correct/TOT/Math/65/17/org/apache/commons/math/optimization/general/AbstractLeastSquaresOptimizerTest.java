package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Override this method for testing, but don't forget to call super!
                VectorialPointValuePair result = super.doOptimize();
                // Perform additional assertions here if needed
                return result;
            }
        };
    }

    /**
     * Test optimization with default settings.
     */
    @Test
    public void testDefaultSettings() {
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] point) {
                return new double[]{point[0] * point[0] + point[1] * point[1],
                        point[0] * point[1] + point[1] * point[2],
                        point[0] * point[2] + point[1] * point[2]};
            }

            @Override
            public double[] jacobian(double[] point) {
                return new double[][]{
                        {2.0 * point[0], point[1], point[1]},
                        {point[1], 2.0 * point[1], point[2]},
                        {point[1], point[2], 2.0 * point[2]}};
            }
        };

        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        // The optimizer should have converged to an optimal point
        assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0}, result.getPoint(), 0.001);
        assertArrayEquals(target, result.getValue(), 0.001);
    }

    /**
     * Test optimization with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] point) {
                return new double[]{point[0] * point[0] + point[1] * point[1],
                        point[0] * point[1] + point[1] * point[2],
                        point[0] * point[2] + point[1] * point[2]};
            }

            @Override
            public double[] jacobian(double[] point) {
                return new double[][]{
                        {2.0 * point[0], point[1], point[1]},
                        {point[1], 2.0 * point[1], point[2]},
                        {point[1], point[2], 2.0 * point[2]}};
            }
        };

        VectorialConvergenceChecker customChecker = new VectorialConvergenceChecker() {
            @Override
            public boolean isConverged(double[] point, double[] value, double[] gradient, double threshold) {
                double maxResidual = 0;
                for (int i = 0; i < value.length; i++) {
                    maxResidual = Math.max(maxResidual, Math.abs(value[i]));
                }
                return maxResidual < threshold;
            }
        };

        optimizer.setConvergenceChecker(customChecker);
        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        // The optimizer should have converged to an optimal point
        assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0}, result.getPoint(), 0.001);
        assertArrayEquals(target, result.getValue(), 0.001);
    }

    /**
     * Test optimization with custom convergence threshold.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testCustomMaxIterations() {
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] point) {
                return new double[]{point[0] * point[0] + point[1] * point[1],
                        point[0] * point[1] + point[1] * point[2],
                        point[0] * point[2] + point[1] * point[2]};
            }

            @Override
            public double[] jacobian(double[] point) {
                return new double[][]{
                        {2.0 * point[0], point[1], point[1]},
                        {point[1], 2.0 * point[1], point[2]},
                        {point[1], point[2], 2.0 * point[2]}};
            }
        };

        optimizer.setMaxIterations(1);
        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        fail("Optimization should not have converged");
    }

    /**
     * Test optimization with custom maximum evaluations.
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testCustomMaxEvaluations() {
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] point) {
                return new double[]{point[0] * point[0] + point[1] * point[1],
                        point[0] * point[1] + point[1] * point[2],
                        point[0] * point[2] + point[1] * point[2]};
            }

            @Override
            public double[] jacobian(double[] point) {
                return new double[][]{
                        {2.0 * point[0], point[1], point[1]},
                        {point[1], 2.0 * point[1], point[2]},
                        {point[1], point[2], 2.0 * point[2]}};
            }
        };

        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, target, weights, startPoint);

        fail("Optimization should have thrown an exception");
    }

    /**
     * Test optimization with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        double[] target = {1.0};
        double[] weights = new double[2];
        double[] startPoint = {0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] point) {
                return new double[]{point[0] * point[0] + point[1] * point[1],
                        point[0] * point[1] + point[1] * point[2]};
            }

            @Override
            public double[] jacobian(double[] point) {
                return new double[][]{
                        {2.0 * point[0], point[1]},
                        {point[1], 2.0 * point[1] + point[2]}};
            }
        };

        optimizer.optimize(function, target, weights, startPoint);

        fail("Optimization should have thrown an exception");
    }

    /**
     * Test optimization with invalid function.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testInvalidFunction() {
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new DifferentiableMultivariateVectorialFunction() {
            @Override
            public double[] value(double[] point) {
                throw new FunctionEvaluationException(point, LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE,
                        point.length, 1);
            }

            @Override
            public double[] jacobian(double[] point) {
                throw new FunctionEvaluationException(point, LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE,
                        point.length, 1);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);

        fail("Optimization should have thrown an exception");
    }
}
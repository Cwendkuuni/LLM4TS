package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testOptimize_LinearRegression() {
        // Given
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.1, 4.2, 5.5, 6.1, 7.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};

        LinearRegressionOptimizer optimizer = new LinearRegressionOptimizer();

        // When
        VectorialPointValuePair result = optimizer.optimize(new LinearRegressionFunction(x), y, weights, new double[6]);

        // Then
        assertEquals(6, result.getValue().length);
        assertEquals(0.816496580927726, result.getValue()[0], 0.001); // intercept
        assertEquals(0.807278239261646, result.getValue()[1], 0.001); // slope
        assertEquals(1.0, result.getValue()[2], 0.001); // x1
        assertEquals(1.0, result.getValue()[3], 0.001); // x2
        assertEquals(1.0, result.getValue()[4], 0.001); // x3
        assertEquals(1.0, result.getValue()[5], 0.001); // x4

        double[] expectedResiduals = {-0.102796120285246, -0.079388464286858, -0.082185872602573, 0.011589249232204, 0.029211281043901};
        assertArrayEquals(expectedResiduals, result.getPoint(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_FunctionEvaluationException() {
        // Given
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.1, 4.2, 5.5, 6.1, 7.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};
        LinearRegressionFunction function = new LinearRegressionFunction(x);

        // When
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {};

        try {
            optimizer.optimize(function, y, weights, new double[6]);
        } catch (IllegalArgumentException e) {
            // When using an abstract class we should get an IllegalArgumentException
            // as the optimizer class doesn't have an implementation for LinearRegressionFunction
            fail("Expected FunctionEvaluationException but got " + e.getClass().getName());
            throw e;
        }
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize_ConvergenceException() {
        // Given
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.1, 4.2, 5.5, 6.1, 7.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[6]; // initial point out of bounds

        LinearRegressionOptimizer optimizer = new LinearRegressionOptimizer();

        // When
        optimizer.optimize(new LinearRegressionFunction(x), y, weights, startPoint);

        // Then
        fail("Expected OptimizationException but optimization completed");
    }

    @Test
    public void testOptimize_MaxIterationsExceededException() {
        // Given
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.1, 4.2, 5.5, 6.1, 7.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[6];

        LinearRegressionOptimizer optimizer = new LinearRegressionOptimizer();
        optimizer.setMaxIterations(0);

        // When
        try {
            optimizer.optimize(new LinearRegressionFunction(x), y, weights, startPoint);
            fail("Expected MaxIterationsExceededException but optimization completed");
        } catch (MaxIterationsExceededException e) {
            // Expected behavior
        }
    }

    @Test
    public void testOptimize_MaxEvaluationsExceededException() {
        // Given
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.1, 4.2, 5.5, 6.1, 7.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[6];

        LinearRegressionOptimizer optimizer = new LinearRegressionOptimizer();
        optimizer.setMaxEvaluations(0);

        // When
        try {
            optimizer.optimize(new LinearRegressionFunction(x), y, weights, startPoint);
            fail("Expected MaxEvaluationsExceededException but optimization completed");
        } catch (MaxEvaluationsExceededException e) {
            // Expected behavior
        }
    }

    @Test
    public void testOptimize_DifferentiableMultivariateVectorialFunction() {
        // Given
        double[] x = {Math.PI / 4, Math.PI / 2, Math.PI, 3.0, 4.0};
        double[] y = {Math.sin(x[0]) + 1.0, Math.sin(x[1]) + 1.0, Math.sin(x[2]) + 1.0, Math.sin(x[3]) + 1.0, Math.sin(x[4]) + 1.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0, 0.0, 0.0};

        SinFunction optimizer = new SinFunctionOptimizer();

        // When
        VectorialPointValuePair result = optimizer.optimize(new SinFunction(x), y, weights, startPoint);

        // Then
        assertEquals(5, result.getValue().length);
        assertEquals(0.540302305868761, result.getValue()[0], 0.001);
        assertEquals(-0.420302305868761, result.getValue()[1], 0.001);
        assertEquals(0.785398163397448, result.getValue()[2], 0.001);
        assertEquals(3.14159265358979, result.getValue()[3], 0.001);
        assertEquals(4.71238898038469, result.getValue()[4], 0.001);

        double[] expectedResiduals = {-0.061232343141593, -0.061232343141593, -0.061232343141593, -0.061232343141593, -0.061232343141593};
        assertArrayEquals(expectedResiduals, result.getPoint(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_FunctionEvaluationException() {
        // Given
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {2.1, 4.2, 5.5, 6.1, 7.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = new double[6];

        SinFunction optimizer = new SinFunctionOptimizer();

        // When
        try {
            optimizer.optimize(new LinearRegressionFunction(x), y, weights, startPoint);
            fail("Expected IllegalArgumentException but optimization completed");
        } catch (IllegalArgumentException e) {
            // Expected behavior
        }
    }

    private interface FunctionInterface {
        double[] value(double[] point) throws FunctionEvaluationException;
        double[] jacobian(double[] point) throws FunctionEvaluationException;
    }

    private static class SinFunction implements DifferentiableMultivariateVectorialFunction, FunctionInterface {
        private final double[] x;

        public SinFunction(double[] x) {
            this.x = x;
        }

        @Override
        public double[] value(double[] point) throws FunctionEvaluationException {
            double[] result = new double[x.length];
            for (int i = 0; i < x.length; ++i) {
                result[i] = Math.sin(point[i]);
            }
            return result;
        }

        @Override
        public double[] jacobian(double[] point) throws FunctionEvaluationException {
            double[] result = new double[x.length][x.length];
            for (int i = 0; i < x.length; ++i) {
                result[i][i] = Math.cos(point[i]);
            }
            return result;
        }
    }
}
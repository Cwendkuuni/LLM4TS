package org.apache.commons.math.optimization.fitting.test;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.nonlinear.levenbergmarquardt.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.stat.function.Gaussian;
import org.apache.commons.math3.stat.function.ParametricFunction;
import org.apache.commons.math3.stat.statistics.DescriptiveStatistics;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Tests for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test fitting a Gaussian function to a set of points.
     */
    @Test
    public void testFitGaussian() {
        double[] x = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976,
                       4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716,
                       4.08237071, 4.08366408, 4.08531585, 4.0875083, 4.08915135,
                       4.0903238, 4.0911227, 4.09163794, 4.09202353, 4.09235358 };
        double[] y = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0,
                      3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0,
                      437481.0, 354983.0, 288198.0, 202645.0, 121552.0, 69253.0, 43252.0,
                      28856.0, 17324.0, 10163.0, 5921.0, 3742.0, 2371.0, 1435.0, 878.0, 549.0,
                      346.0, 204.0, 119.0, 69.0, 44.0, 28.0, 17.0, 10.0, 6.0, 4.0 };

        Double[] weights = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[x.length];
        for (int i = 0; i < x.length; i++) {
            observations[i] = new WeightedObservedPoint(x[i], y[i], weights[i]);
        }

        UnconstrainedMinimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        PointValuePair result = fitter.fit();

        ParametricFunction model = new Gaussian.Parametric();
        double[] testX = { 3.9, 4.0, 4.1 };
        double[] testY = model.values(testX, result.getParameterVector());

        // Test values for the testX
        for (int i = 0; i < testX.length; i++) {
            double expected = testY[i];
            assertEquals("Error for test X: " + testX[i], expected,
                         fitter.value(testX[i], result.getParameterVector()), 0.001);
        }

        // Test gradient for the testX
        double[] testGrad = model.gradient(testX[0], result.getParameterVector());
        double[] testGradExpected = new double[] { -testGrad[0], 1.0, -(testGrad[1] + 1.0) / 2.0 };
        assertArrayEquals("Error for test X gradient: " + testX[0], testGradExpected,
                          fitter.gradient(testX[0], result.getParameterVector()), 0.001);

        // Test values for the x in the observations (without weights)
        for (WeightedObservedPoint obs : observations) {
            double expected = obs.getY();
            double value = fitter.value(obs.getX(), result.getParameterVector());
            assertEquals("Error for observation X: " + obs.getX() + ", Y: " + obs.getY(),
                         expected, value, 0.01);
        }

        // Test residuals
        double[] residuals = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            double expected = y[i] - model.value(x[i], result.getParameterVector());
            residuals[i] = Math.abs(expected);
        }
        double[] residualsSorted = new double[residuals.length];
        System.arraycopy(residuals, 0, residualsSorted, 0, residuals.length);
        Arrays.sort(residualsSorted);
        double rms = new DescriptiveStatistics(residuals).getStandardDeviation();
        double tolerance = rms * 3;
        for (int i = 0; i < residuals.length; i++) {
            double residual = residuals[i];
            assertTrue("Error for residual: " + residuals[i], residual <= tolerance);
        }

        // Test fitting a Gaussian function with initial guesses
        double[] initialGuess = { 1.0, 4.0, 0.5 };
        PointValuePair fitResult = fitter.fit(initialGuess);
        testValuesAndGradient(testX, testY, fitResult);

        // Test fitting a Gaussian function with default initial guesses
        PointValuePair fitResult2 = fitter.fit();
        testValuesAndGradient(testX, testY, fitResult2);

        // Test fitting a Gaussian function with out of range initial guesses
        try {
            double[] initialGuessOutOfRange = { 1.0, -1.0, 0.5 };
            fitter.fit(initialGuessOutOfRange);
            fail("Expected NumberIsTooSmallException with negative initialguess[0]");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        try {
            double[] initialGuessOutOfRange = { 1.0, 10.0, 0.5 };
            fitter.fit(initialGuessOutOfRange);
            fail("Expected OutOfRangeException with initialguess[1] out of bounds");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            double[] initialGuessOutOfRange = { 1.0, 4.0, -0.5 };
            fitter.fit(initialGuessOutOfRange);
            fail("Expected NotStrictlyPositiveException with negative initialguess[2]");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    /**
     * Helper method for testing the values and gradient for a given set of X values.
     *
     * @param testX X values for testing.
     * @param testY expected Y values for the given X values.
     * @param result result of the fitting process.
     */
    private void testValuesAndGradient(double[] testX, double[] testY, PointValuePair result) {
        for (int i = 0; i < testX.length; i++) {
            double expected = testY[i];
            double value = fitter.value(testX[i], result.getParameterVector());
            assertEquals("Error for test X: " + testX[i], expected, value, 0.001);

            double[] gradient = fitter.gradient(testX[i], result.getParameterVector());
            double[] gradientExpected = new double[] { -testGrad[0] * testX[i],
                                                        1.0 + testGrad[1] * testX[i],
                                                        -(testGrad[1] + 1.0) / 2.0 };
            assertArrayEquals("Error for test X gradient: " + testX[i], gradientExpected,
                              gradient, 0.001);
        }
    }
}
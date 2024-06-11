package org.apache.commons.math.optimization.fitting.test;

import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializers.ArrayInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BFGS;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.initializers.DenseInitializer;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.fitting.GaussianFitter;
import org.apache.commons.math3.optim.fitting.ParameterGuesser;
import org.apache.commons.math3.optim.fitting.WeightedObservedPoint;
import org.apache.commons.math3.optim.fitting.FunctionFittingTest;
import static org.junit.Assert.*;

/**
 * Test for {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    /**
     * Test fitting a Gaussian function with known parameters.
     */
    @Test
    public void testFitGaussian() {
        double[] points = { 1.0, 2.5, 2.0, 3.5, 3.0, 4.5, 4.0, 5.5, 5.0 };
        double[] weights = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 };
        double[] expected = { 5.5, 2.5, 0.8 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i], weights[i]);
        }
        GaussianFitter fitter = new GaussianFitter(new BFGS());
        fitter.addObservedPoints(observations);
        PointValuePair result = fitter.fit();
        assertEquals(3, result.getDimension());
        assertArrayEquals(expected, result.getPoint(), 0.001);
    }

    /**
     * Test fitting a Gaussian function with wrong initial guess.
     */
    @Test(expected = IllegalStateException.class)
    public void testFitGaussianWrongInitialGuess() {
        double[] points = { 1.0, 2.5, 2.0, 3.5, 3.0, 4.5, 4.0, 5.5, 5.0 };
        double[] weights = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 };
        double[] initialGuess = { 1.0, 2.0, 3.0 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i], weights[i]);
        }
        GaussianFitter fitter = new GaussianFitter(new BFGS());
        fitter.addObservedPoints(observations);
        fitter.fit(initialGuess);
    }

    /**
     * Test fitting a Gaussian function with missing points.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianMissingPoints() {
        double[] points = { 1.0, 2.5, 2.0, 3.5, 3.0 };
        double[] weights = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i], weights[i]);
        }
        GaussianFitter fitter = new GaussianFitter(new BFGS());
        fitter.addObservedPoints(observations);
        try {
            fitter.fit();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test guessing parameters with known points.
     */
    @Test
    public void testGuessParameters() {
        double[] points = { 1.0, 2.5, 2.0, 3.5, 3.0, 4.5, 4.0, 5.5, 5.0 };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i], 1.0);
        }
        ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(observations);
        double[] expected = guesser.guess();
        double[] actual = guesser.guess();
        assertEquals(3, expected.length);
        assertArrayEquals(expected, actual, 0.001);
    }

    /**
     * Test guessing parameters with no points.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGuessParametersNoPoints() {
        ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(null);
        try {
            guesser.guess();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test maximizing a Gaussian function using known parameters and bounds.
     */
    @Test
    public void testMaximizeGaussian() {
        double[] initialGuess = { 1.0, 2.0, 1.0 };
        ObjectiveFunction objective = new GaussianFunction(5.0, 2.5, 0.8);
        SimpleBounds bounds = new SimpleBounds(0.1, 10.0, 1.0, 10.0, 0.1, 10.0);
        MaxEval optimizer = new MaxEval(new BFGS(), bounds, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(objective, initialGuess);
        assertEquals(3, result.getDimension());
        assertArrayEquals(new double[] { 5.5, 2.5, 0.8 }, result.getPoint(), 0.001);
    }

    /**
     * Test maximizing a Gaussian function using wrong initial guess and bounds.
     */
    @Test(expected = IllegalStateException.class)
    public void testMaximizeGaussianWrongInitialGuessAndBounds() {
        double[] initialGuess = { 1.0, 2.0, 1.0 };
        ObjectiveFunction objective = new GaussianFunction(5.0, 2.5, 0.8);
        SimpleBounds bounds = new SimpleBounds(0.1, 10.0, -1.0, 10.0, 0.1, 10.0);
        MaxEval optimizer = new MaxEval(new BFGS(), bounds, GoalType.MAXIMIZE);
        try {
            optimizer.optimize(objective, initialGuess);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test maximizing a Gaussian function with no bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMaximizeGaussianNoBounds() {
        double[] initialGuess = { 1.0, 2.0, 1.0 };
        ObjectiveFunction objective = new GaussianFunction(5.0, 2.5, 0.8);
        MaxEval optimizer = new MaxEval(new BFGS(), null, GoalType.MAXIMIZE);
        try {
            optimizer.optimize(objective, initialGuess);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Gaussian function to optimize.
     */
    private static class GaussianFunction implements ObjectiveFunction {
        private final double mean;
        private final double norm;
        private final double sigma;

        /**
         * Constructor.
         *
         * @param mean Mean value.
         * @param norm Norm value.
         * @param sigma Standard deviation value.
         */
        public GaussianFunction(double mean, double norm, double sigma) {
            this.mean = mean;
            this.norm = norm;
            this.sigma = sigma;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double value(double[] point) {
            if (point.length != 3) {
                throw new IllegalArgumentException("Point should have 3 components");
            }
            return norm * Math.exp(-Math.pow(point[0] - mean, 2) / (2 * Math.pow(sigma, 2)));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double[] gradient(double[] point) {
            if (point.length != 3) {
                throw new IllegalArgumentException("Point should have 3 components");
            }
            double[] grad = new double[3];
            grad[0] = -2 * (point[0] - mean) / (2 * Math.pow(sigma, 2)) * norm * Math.exp(-Math.pow(point[0] - mean, 2) / (2 * Math.pow(sigma, 2)));
            grad[1] = 0.0;
            grad[2] = 0.0;
            return grad;
        }
    }
}
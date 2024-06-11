package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.variable.RealVariable;
import org.apache.commons.math3.optim.nonlinear.vector.linear.ArrayRealVector;
import org.apache.commons.math3.optim.nonlinear.vector.linear.RealVector;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        final double[] xData = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        final double[] yData = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);

        for (int i = 0; i < xData.length; i++) {
            fitter.addObservedPoint(xData[i], yData[i]);
        }

        double[] parameters = fitter.fit();
        ParametricUnivariateRealFunction gaussian = new Gaussian.Parametric();
        RealVector expectedParameters = new ArrayRealVector(new double[] { 1.0, 4.0, 0.5 });

        assertEquals(3, parameters.getDimension());
        assertArrayEquals(expectedParameters.toArray(), parameters, 0.001);
        PointValuePair result = optimizer.optimize(gaussian, new RealVector[] { parameters }, new MaxEval() {});

        double norm = result.getPoint().getEntry(0);
        double mean = result.getPoint().getEntry(1);
        double sigma = result.getPoint().getEntry(2);

        assertEquals(parameters[0], norm, 0.001);
        assertEquals(parameters[1], mean, 0.001);
        assertEquals(parameters[2], sigma, 0.001);

        ParametricUnivariateRealFunction fittedGaussian = new ParametricUnivariateRealFunction() {
            @Override
            public double value(double x, double[] p) {
                return gaussian.value(x, p);
            }

            @Override
            public double[] gradient(double x, double[] p) {
                return gaussian.gradient(x, p);
            }
        };

        RealVector xValues = new ArrayRealVector(xData);
        RealVector yValues = new ArrayRealVector(yData);
        RealVector expectedYValues = new ArrayRealVector(new double[xData.length]);

        for (int i = 0; i < xData.length; i++) {
            expectedYValues.setEntry(i, gaussian.value(xData[i], parameters));
        }

        RealVector residuals = new ArrayRealVector(yValues.subtract(expectedYValues));

        double sumOfSquares = residuals.dotProduct(residuals);
        double sumOfWeights = xValues.getDimension() * Math.PI * Erf.erf(sigma / Math.sqrt(2.0));

        assertEquals(sumOfSquares / sumOfWeights, 1.0, 0.001);
        assertEquals(new StandardDeviation().evaluate(yValues), new StandardDeviation().evaluate(expectedYValues), 0.01);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullObservedPoints() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoint(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullXData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullYData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] xData = { 1.0, 2.0, 3.0 };
        fitter.addObservedPoints(xData, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoint(-1.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] xData = { 1.0, 2.0, 3.0 };
        fitter.addObservedPoint(1.0, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new double[] { -1.0, 1.0, -1.0 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGuesser() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFitWithoutData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithZeroSizeData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(new double[0][], new double[0][]);
        assertThrows(IllegalArgumentException.class, () -> fitter.fit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNullInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithTooFewInitialGuesses() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new double[] { 1.0 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNegativeInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new double[] { -1.0, 1.0, -1.0 });
    }

    @Test
    public void testFitWithEmptyData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[] { 1.0, 1.0, 1.0 };
        fitter.fit(initialGuess);
        double[] result = fitter.getFittedParameters();
        assertEquals(3, result.length);
        assertEquals(1.0, result[0], 0.001);
        assertEquals(1.0, result[1], 0.001);
        assertEquals(1.0, result[2], 0.001);
    }

    @Test
    public void testFitWithSinglePointData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[] { 1.0, 1.0, 1.0 };
        fitter.addObservedPoint(1.0, 1.0);
        fitter.fit(initialGuess);
        double[] result = fitter.getFittedParameters();
        assertEquals(3, result.length);
        assertEquals(1.0, result[0], 0.001);
        assertEquals(1.0, result[1], 0.001);
        assertEquals(0.0, result[2], 0.001);
    }

    @Test
    public void testFitWithTwoPointsData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[] { 1.0, 1.0, 1.0 };
        fitter.addObservedPoint(1.0, 1.0);
        fitter.addObservedPoint(2.0, 4.0);
        fitter.fit(initialGuess);
        double[] result = fitter.getFittedParameters();
        assertEquals(3, result.length);
        assertEquals(1.223235869251625, result[0], 0.01);
        assertEquals(1.5, result[1], 0.01);
        assertEquals(0.842912130425056, result[2], 0.01);
    }

    @Test
    public void testFitWithMultiplePointsData() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[] { 1.0, 1.0, 1.0 };
        for (int i = 0; i < 12; i++) {
            fitter.addObservedPoint(i + 1.0, Math.pow((i + 1.0), 2.0));
        }
        fitter.fit(initialGuess);
        double[] result = fitter.getFittedParameters();
        assertEquals(3, result.length);
        assertEquals(6.615121044228384, result[0], 0.01);
        assertEquals(2.0, result[1], 0.01);
        assertEquals(1.846910553655943, result[2], 0.01);
    }

    @Test
    public void testFitWithMultiplePointsDataAndWeights() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[] { 1.0, 1.0, 1.0 };
        double[] weights = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0 };
        for (int i = 0; i < 12; i++) {
            fitter.addObservedPoint(i + 1.0, Math.pow((i + 1.0), 2.0), weights[i]);
        }
        fitter.fit(initialGuess);
        double[] result = fitter.getFittedParameters();
        assertEquals(3, result.length);
        assertEquals(6.615121044228384, result[0], 0.01);
        assertEquals(2.0, result[1], 0.01);
        assertEquals(1.846910553655943, result[2], 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidInitialGuess() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new double[] { -1.0, 1.0, -1.0 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidInitialGuessBounds() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        RealVariable[] variables = new RealVariable[] { new RealVariable("norm", false, 0.0, Double.POSITIVE_INFINITY),
                new RealVariable("mean", false, -Double.MAX_VALUE, Double.MAX_VALUE),
                new RealVariable("sigma", false, 0.0, Double.POSITIVE_INFINITY) };
        fitter.fit(new double[] { 0.0, 1.0, 0.0 }, new SimpleBounds(variables));
    }
}
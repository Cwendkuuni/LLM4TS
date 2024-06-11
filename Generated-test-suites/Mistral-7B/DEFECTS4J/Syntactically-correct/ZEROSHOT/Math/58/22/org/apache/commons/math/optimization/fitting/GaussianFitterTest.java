package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FreemanShapire;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Gradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NewtonRaphsonLineSearch;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GaussianFitterTest {

    private static final double EPSILON = 1e-10;
    private static final double MAX_ITERATIONS = 1000;

    private WeightedObservedPoint[] observations;
    private double[] parameters;

    @Before
    public void setUp() {
        Double[] xValues = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
        Double[] yValues = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};
        observations = new WeightedObservedPoint[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            observations[i] = new WeightedObservedPoint(xValues[i], yValues[i], 1.0);
        }
    }

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(new FreemanShapire());
        fitter.fit(parameters = fitter.fit(new Gaussian.Parametric().getInitialGuess()));
        Assert.assertNotNull(parameters);
        Assert.assertArrayEquals(new double[]{1.0, 4.05326982, 0.14692602}, parameters, 0.001);
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new FreemanShapire());
        double[] initialGuess = new double[]{1.2, 4.1, 0.2};
        parameters = fitter.fit(initialGuess);
        Assert.assertArrayEquals(new double[]{1.0, 4.05326982, 0.14692602}, parameters, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithInvalidObservedPoints() {
        WeightedObservedPoint[] invalidObservations = new WeightedObservedPoint[0];
        new GaussianFitter(new FreemanShapire()).fit(invalidObservations);
    }

    @Test
    public void testGuess() {
        ParameterGuesser guesser = new ParameterGuesser(observations);
        parameters = guesser.guess();
        Assert.assertNotNull(parameters);
        Assert.assertArrayEquals(new double[]{1.0, 4.05326982, 0.14692602}, parameters, 0.001);
    }

    @Test
    public void testFunction() {
        final GaussianFitter fitter = new GaussianFitter(new FreemanShapire());
        final UnivariateFunction function = fitter.getFunction();

        PointValuePairChecker pvpChecker = new PointValuePairChecker(1e-8);
        Gradient gradient = new NewtonRaphsonLineSearch(new MaxEval(MAX_ITERATIONS, GoalType.ABS_ABS), pvpChecker);

        // Test function value
        for (WeightedObservedPoint point : observations) {
            double value = FunctionUtils.value(function, point.getX());
            Assert.assertEquals("Function value for point (" + point.getX() + ", " + point.getY() + ")",
                               point.getY(), value, EPSILON);
        }

        // Test gradient
        for (WeightedObservedPoint point : observations) {
            double[] gradientValues = FunctionUtils.gradient(function, point.getX());
            double norm = Math.sqrt(gradientValues[0] * gradientValues[0] +
                                    gradientValues[1] * gradientValues[1] +
                                    gradientValues[2] * gradientValues[2]);
            Assert.assertEquals("Gradient norm for point (" + point.getX() + ", " + point.getY() + ")",
                                Math.sqrt(3.0 / (2.0 * Math.PI)), norm, EPSILON);
            Assert.assertEquals("Gradient x component for point (" + point.getX() + ", " + point.getY() + ")",
                                -(point.getX() - parameters[1]) / (2.0 * parameters[2] * parameters[2]),
                                gradientValues[0], EPSILON);
            Assert.assertEquals("Gradient y component for point (" + point.getX() + ", " + point.getY() + ")",
                                -1.0 / (2.0 * parameters[2] * Math.sqrt(2.0 * Math.PI)),
                                gradientValues[1], EPSILON);
            Assert.assertEquals("Gradient z component for point (" + point.getX() + ", " + point.getY() + ")",
                                (point.getX() - parameters[1]) / (2.0 * parameters[2] * parameters[2] * parameters[2]),
                                gradientValues[2], EPSILON);
        }
    }

    @Test
    public void testFunctionFittedValuesAndDerivatives() {
        GaussianFitter fitter = new GaussianFitter(new FreemanShapire());
        fitter.fit(parameters = fitter.fit(new Gaussian.Parametric().getInitialGuess()));

        final UnivariateFunction function = fitter.getFunction();

        PointValuePairChecker pvpChecker = new PointValuePairChecker(1e-8);
        Gradient gradient = new NewtonRaphsonLineSearch(new MaxEval(MAX_ITERATIONS, GoalType.ABS_ABS), pvpChecker);

        double x = 4.05326982;
        double y = function.value(x);
        double[] gradientValues = FunctionUtils.gradient(function, x);

        Assert.assertEquals("Function value for x = " + x, y, observations[5].getY(), EPSILON);
        Assert.assertEquals("Gradient x component for x = " + x,
                            -(x - parameters[1]) / (2.0 * parameters[2] * parameters[2]),
                            gradientValues[0], EPSILON);
        Assert.assertEquals("Gradient y component for x = " + x,
                            -1.0 / (2.0 * parameters[2] * Math.sqrt(2.0 * Math.PI)),
                            gradientValues[1], EPSILON);
        Assert.assertEquals("Gradient z component for x = " + x,
                            (x - parameters[1]) / (2.0 * parameters[2] * parameters[2] * parameters[2]),
                            gradientValues[2], EPSILON);

        Assert.assertEquals("Function value at x = " + parameters[1] + " (mean)",
                            0.0, function.value(parameters[1]), EPSILON);
        Assert.assertEquals("Gradient x component at x = " + parameters[1] + " (mean)",
                            -Math.sqrt(2.0 * Math.PI) / (parameters[2] * 2.0),
                            gradientValues[0], EPSILON);
        Assert.assertEquals("Gradient y component at x = " + parameters[1] + " (mean)",
                            -1.0 / (2.0 * Math.PI),
                            gradientValues[1], EPSILON);
        Assert.assertEquals("Gradient z component at x = " + parameters[1] + " (mean)",
                            -Math.sqrt(2.0 * Math.PI) / (parameters[2] * parameters[2]),
                            gradientValues[2], EPSILON);

        Assert.assertEquals("Function value at x = " + (parameters[1] + parameters[2]),
                            parameters[0], function.value(parameters[1] + parameters[2]), EPSILON);
        Assert.assertEquals("Gradient x component at x = " + (parameters[1] + parameters[2]),
                            -(parameters[1] + parameters[2] - parameters[1]) / (2.0 * parameters[2] * parameters[2]),
                            gradientValues[0], EPSILON);
        Assert.assertEquals("Gradient y component at x = " + (parameters[1] + parameters[2]),
                            -1.0 / (2.0 * Math.PI),
                            gradientValues[1], EPSILON);
        Assert.assertEquals("Gradient z component at x = " + (parameters[1] + parameters[2]),
                            (parameters[1] + parameters[2] - parameters[1]) / (2.0 * parameters[2] * parameters[2] * parameters[2]),
                            gradientValues[2], EPSILON);

        Assert.assertEquals("Function value at x = " + (parameters[1] - parameters[2]),
                            parameters[0], function.value(parameters[1] - parameters[2]), EPSILON);
        Assert.assertEquals("Gradient x component at x = " + (parameters[1] - parameters[2]),
                            -(parameters[1] - parameters[2] - parameters[1]) / (2.0 * parameters[2] * parameters[2]),
                            gradientValues[0], EPSILON);
        Assert.assertEquals("Gradient y component at x = " + (parameters[1] - parameters[2]),
                            -1.0 / (2.0 * Math.PI),
                            gradientValues[1], EPSILON);
        Assert.assertEquals("Gradient z component at x = " + (parameters[1] - parameters[2]),
                            (parameters[1] - parameters[2] - parameters[1]) / (2.0 * parameters[2] * parameters[2] * parameters[2]),
                            gradientValues[2], EPSILON);
    }
}
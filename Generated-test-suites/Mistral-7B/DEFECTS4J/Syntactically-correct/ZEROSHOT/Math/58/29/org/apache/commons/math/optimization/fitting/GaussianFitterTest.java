package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.function.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.WeightedObservedPoint;
import org.apache.commons.math3.optimizer.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.optimizer.MaxEval;
import org.apache.commons.math3.optimizer.SimpleBoundingBoxMinimizer;
import org.apache.commons.math3.optimizer.SimpleBounds;
import org.junit.Test;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test fitting with custom optimizer.
     */
    @Test
    public void testFitWithCustomOptimizer() {
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(2.0, 1.0, 1.0),
                new WeightedObservedPoint(3.0, 3.0, 1.0),
                new WeightedObservedPoint(4.0, 10.0, 1.0),
                new WeightedObservedPoint(5.0, 30.0, 1.0),
                new WeightedObservedPoint(6.0, 60.0, 1.0),
                new WeightedObservedPoint(7.0, 100.0, 1.0)
        };
        DifferentiableMultivariateVectorOptimizer optimizer = new SimpleBoundingBoxMinimizer(
                new SimpleBounds(-100, 100), new MaxEval(1000));
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        double[] parameters = fitter.fit();
        assertEquals(3, parameters.length);
        ParametricUnivariateRealFunction fittedFunction = new ParametricUnivariateRealFunction() {
            private final ParametricUnivariateRealFunction function = new Gaussian.Parametric(parameters);

            public double value(double x) {
                return function.value(x);
            }

            public double[] gradient(double x) {
                return function.gradient(x);
            }
        };
        PointValuePair fitResult = fitter.getValue(new WeightedObservedPoint(1.0, 0.0));
        double tolerance = 0.001;
        assertEquals(function.value(1.0), fitResult.getValue(), tolerance);
        assertArrayEquals(function.gradient(1.0), fitResult.getGradient(), tolerance);
    }

    /**
     * Test fitting with default optimizer.
     */
    @Test
    public void testFitWithDefaultOptimizer() {
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(2.0, 1.0, 1.0),
                new WeightedObservedPoint(3.0, 3.0, 1.0),
                new WeightedObservedPoint(4.0, 10.0, 1.0),
                new WeightedObservedPoint(5.0, 30.0, 1.0),
                new WeightedObservedPoint(6.0, 60.0, 1.0),
                new WeightedObservedPoint(7.0, 100.0, 1.0)
        };
        GaussianFitter fitter = new GaussianFitter();
        fitter.addObservedPoints(observations);
        double[] parameters = fitter.fit();
        assertEquals(3, parameters.length);
        ParametricUnivariateRealFunction fittedFunction = new ParametricUnivariateRealFunction() {
            private final ParametricUnivariateRealFunction function = new Gaussian.Parametric(parameters);

            public double value(double x) {
                return function.value(x);
            }

            public double[] gradient(double x) {
                return function.gradient(x);
            }
        };
        PointValuePair fitResult = fitter.getValue(new WeightedObservedPoint(1.0, 0.0));
        double tolerance = 0.001;
        assertEquals(function.value(1.0), fitResult.getValue(), tolerance);
        assertArrayEquals(function.gradient(1.0), fitResult.getGradient(), tolerance);
    }

    /**
     * Test exception when calling {@code fit()} with less than 3 observations.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testExceptionLessThanThreeObservations() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(2.0, 1.0, 1.0)
        };
        GaussianFitter fitter = new GaussianFitter();
        fitter.addObservedPoints(observations);
        fitter.fit();
    }
}
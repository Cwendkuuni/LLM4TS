package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.MultivariateVectorFunction;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceListener;
import org.apache.commons.math3.optim.nonlinear.convergence.IterationNumber;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleBoundsCheck;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleStoppingCondition;
import org.apache.commons.math3.optim.nonlinear.gradient.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFit() throws NotStrictlyPositiveException {
        NonlinearOptimizer optimizer = new GaussNewtonOptimizer();
        ConvergenceChecker checker = new SimpleBoundsCheck(1e-10, 1e-10);
        ConvergenceListener listener = new IterationNumber(1000);
        Double[] initialGuess = new Double[] { 1.0, 2.0, 0.5 };
        WeightedObservedPoint[] points = {
                new WeightedObservedPoint(1.0, 1.0),
                new WeightedObservedPoint(2.0, 3.0),
                new WeightedObservedPoint(3.0, 6.0),
                new WeightedObservedPoint(4.0, 9.0),
                new WeightedObservedPoint(5.0, 12.0)
        };

        CurveFitter fitter = new GaussianFitter(optimizer);
        for (WeightedObservedPoint point : points) {
            fitter.addObservedPoint(point.getX(), point.getY());
        }
        Double[] parameters = fitter.fit(initialGuess);

        ParametricUnivariateRealFunction actualFunction = new Gaussian.Parametric(parameters);
        MultivariateVectorFunction gradientFunction = new Gaussian.ParametricGradient(parameters);
        PointValuePair expectedResult = optimizer.optimize(new GaussianFitterFunction(points), initialGuess, new SimpleBounds(new Double[] { 0.1, 0.1, 0.1 }), null, null, checker, null, listener);
        Double[] expectedParameters = expectedResult.getPoint();

        assertEquals(expectedParameters[0], parameters[0], 1e-10);
        assertEquals(expectedParameters[1], parameters[1], 1e-10);
        assertEquals(expectedParameters[2], parameters[2], 1e-10);

        for (WeightedObservedPoint point : points) {
            assertEquals(actualFunction.value(point.getX()), point.getY(), 1e-10);
        }

        Double[] gradient = gradientFunction.value(new Double[]{ 4.0 });
        assertTrue(Arrays.equals(new Double[] { -parameters[1] / (2.0 * parameters[2] * parameters[2]), 1.0 / (2.0 * parameters[2] * parameters[2]), -1.0 / (2.0 * parameters[2] * parameters[2]) }, gradient, 1e-10));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullInitialGuess() {
        NonlinearOptimizer optimizer = new GaussNewtonOptimizer();
        CurveFitter fitter = new GaussianFitter(optimizer);
        Double[] initialGuess = null;
        WeightedObservedPoint[] points = {
                new WeightedObservedPoint(1.0, 1.0),
                new WeightedObservedPoint(2.0, 3.0),
                new WeightedObservedPoint(3.0, 6.0),
                new WeightedObservedPoint(4.0, 9.0),
                new WeightedObservedPoint(5.0, 12.0)
        };
        fitter.fit(initialGuess);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullPoints() {
        NonlinearOptimizer optimizer = new GaussNewtonOptimizer();
        CurveFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] points = null;
        Double[] initialGuess = { 1.0, 2.0, 0.5 };
        fitter.fit(points, initialGuess);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPoints() {
        NonlinearOptimizer optimizer = new GaussNewtonOptimizer();
        CurveFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] points = {};
        Double[] initialGuess = { 1.0, 2.0, 0.5 };
        fitter.fit(points, initialGuess);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialGuess() {
        NonlinearOptimizer optimizer = new GaussNewtonOptimizer();
        CurveFitter fitter = new GaussianFitter(optimizer);
        Double[] initialGuess = { 0.0, 2.0, 0.5 };
        WeightedObservedPoint[] points = {
                new WeightedObservedPoint(1.0, 1.0),
                new WeightedObservedPoint(2.0, 3.0),
                new WeightedObservedPoint(3.0, 6.0),
                new WeightedObservedPoint(4.0, 9.0),
                new WeightedObservedPoint(5.0, 12.0)
        };
        fitter.fit(initialGuess);
    }

    private static class GaussianFitterFunction implements MultivariateVectorFunction {
        private final WeightedObservedPoint[] points;

        public GaussianFitterFunction(WeightedObservedPoint[] points) {
            this.points = points;
        }

        @Override
        public DoubleValue[] value(Double[] x) {
            DoubleValue[] result = new DoubleValue[points.length];
            for (int i = 0; i < points.length; i++) {
                result[i] = new DoubleValue((new Gaussian.Parametric(x)).value(points[i].getX()));
            }
            return result;
        }

        @Override
        public DoubleValue[] gradient(Double[] x) {
            DoubleValue[] result = new DoubleValue[points.length * 3];
            for (int i = 0, j = 0; i < points.length; i++) {
                Double[] gradient = new Gaussian.ParametricGradient(x).value(points[i].getX());
                for (int k = 0; k < 3; k++) {
                    result[j++] = new DoubleValue(gradient[k]);
                }
            }
            return result;
        }
    }
}
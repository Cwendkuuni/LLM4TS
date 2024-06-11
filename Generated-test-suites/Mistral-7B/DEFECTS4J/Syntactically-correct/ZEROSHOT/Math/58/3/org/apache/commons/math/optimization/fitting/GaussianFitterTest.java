package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedInitialGuess;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.points.SimplePoints;
import org.apache.commons.math3.optim.initializer.RandomInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FiniteDifferenceDerivativeFactory;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.initializer.RandomInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FiniteDifferenceDerivativeFactory;
import org.apache.commons.math3.optim.points.SimplePoints;
import org.apache.commons.math3.optim.fitting.CurveFitter;
import org.apache.commons.math3.optim.fitting.GaussianFitter;
import org.apache.commons.math3.optim.fitting.WeightedObservedPoint;
import org.apache.commons.math3.special.StatFunctions;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GaussianFitterTest {

    private static final double EPSILON = 1.0e-12;
    private static final double RELATIVE_ERROR = 1.0e-6;

    private DifferentiableMultivariateVectorOptimizer optimizer;
    private ParametricUnivariateRealFunction gaussian;
    private WeightedObservedPoint[] points;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(1.0E-6, new AveragedInitialGuess(3),
                new FiniteDifferenceDerivativeFactory());
        gaussian = new Gaussian.Parametric();
        points = new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
                new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
                new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
                new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
                new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
                new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
                new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
                new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
                new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
                new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
                new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
                new WeightedObservedPoint(4.08366408, 620014.0, 1.0),
                new WeightedObservedPoint(4.08820832, 385586.0, 1.0)
        };
    }

    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] result = fitter.fit(new double[]{0.5, 4.0, 0.2});
        assertArrayEquals(new double[]{4.04421621, 4.0, 0.25703363}, result, EPSILON);
    }

    @Test
    public void testFitInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] result = fitter.fit();
        double[] initialGuess = {1.0, 4.0, 1.0};
        double[] expectedResult = new double[]{4.04421621, 4.0, 0.25703363};
        fitter.setInitialGuess(initialGuess);
        result = fitter.fit();
        assertArrayEquals(expectedResult, result, EPSILON);
    }

    @Test
    public void testFitGaussianFunction() {
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            public double value(PointValuePair p) {
                double x = p.getPoint()[0];
                double y = p.getValue();
                double[] params = gaussian.fit(points);
                return Math.pow(gaussian.value(x, params) - y, 2);
            }

            public int getNumberOfVariables() {
                return 3;
            }
        };
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new double[]{1.0, 4.0, 1.0};
        double[] result = fitter.fit(initialGuess, objectiveFunction, GoalType.MINIMIZE);
        double[] expectedResult = new double[]{4.04421621, 4.0, 0.25703363};
        assertArrayEquals(expectedResult, result, EPSILON);
    }

    @Test
    public void testFitInitialGuessRandom() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] initialGuess = new RandomInitialGuess(Gaussian.class, 3, 0.0, 10.0).getInitialPoints(3)[0];
        double[] result = fitter.fit(initialGuess);
        double[] expectedResult = new double[]{4.04421621, 4.0, 0.25703363};
        assertArrayEquals(expectedResult, result, EPSILON);
    }

    @Test
    public void testFitWithFewerPoints() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] reducedPoints = new WeightedObservedPoint[2];
        System.arraycopy(points, 0, reducedPoints, 0, 2);
        double[] result = fitter.fit(reducedPoints);
        assertArrayEquals(new double[]{3.98658634, 3.9972357, 0.35551129}, result, RELATIVE_ERROR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitEmptyObservations() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitZeroWeightObservations() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] zeroWeightPoints = new WeightedObservedPoint[1];
        zeroWeightPoints[0] = new WeightedObservedPoint(4.0, 5.0, 0.0);
        fitter.fit(zeroWeightPoints);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeWeightObservations() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] negativeWeightPoints = new WeightedObservedPoint[1];
        negativeWeightPoints[0] = new WeightedObservedPoint(4.0, 5.0, -1.0);
        fitter.fit(negativeWeightPoints);
    }
}
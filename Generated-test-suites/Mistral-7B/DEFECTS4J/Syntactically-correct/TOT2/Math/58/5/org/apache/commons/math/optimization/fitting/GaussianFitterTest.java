package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonMethod;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleStoppingCondition;
import org.apache.commons.math3.optim.points.PointValuePairList;
import org.apache.commons.math3.optim.initialization.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.initialization.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.PayneRiddersMethod;
import org.apache.commons.math3.optim.nonlinear.function.UnivariateFunction;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.GammaFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Category(org.apache.commons.math.optimization.fitting.GaussianFitterTest.class)
public class GaussianFitterTest {

    private static final double EPSILON = 1e-10;

    private WeightedObservedPoint[] observations;
    private double[] parameters;

    @Before
    public void setup() {
        observations = new WeightedObservedPoint[] {
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
                new WeightedObservedPoint(4.08694624, 347516.0, 1.0),
                new WeightedObservedPoint(4.09139648, 213128.0, 1.0),
                new WeightedObservedPoint(4.09523944, 118364.0, 1.0),
                new WeightedObservedPoint(4.1, 10000.0, 1.0),
                new WeightedObservedPoint(4.10494834, 4148.0, 1.0),
                new WeightedObservedPoint(4.12, 100.0, 1.0),
                new WeightedObservedPoint(4.15, 0.0, 1.0),
                new WeightedObservedPoint(4.18, -100.0, 1.0),
                new WeightedObservedPoint(4.2, -4148.0, 1.0),
                new WeightedObservedPoint(4.25, -10000.0, 1.0)
        };
        parameters = new double[] {10000.0, 4.1, 2.0};
    }

    @Test
    public void testFitSimple() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonMethod());
        fitter.addObservedPoint(observations);
        double[] result = fitter.fit();
        Assert.assertArrayEquals(expectedArray(parameters), result, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonMethod());
        fitter.fit(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitNoObservations() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonMethod());
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonMethod());
        double[] initialGuess = new double[] {15000.0, 4.2, 3.0};
        double[] result = fitter.fit(initialGuess);

        Assert.assertArrayEquals(expectedArray(parameters), result, EPSILON);
    }

    @Test
    public void testFitWithInitialGuessOutOfRange() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonMethod());
        double[] initialGuess = new double[] {-1000.0, 4.2, 3.0};
        double[] result = fitter.fit(initialGuess);

        Assert.assertArrayEquals(expectedArray(parameters), result, EPSILON);
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitWithObservationOutOfRange() {
        WeightedObservedPoint obs = new WeightedObservedPoint(Double.POSITIVE_INFINITY, 0.0, 1.0);
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonMethod());
        fitter.addObservedPoint(observations);
        fitter.addObservedPoint(obs);
        fitter.fit();
    }

    @Test
    public void testFitWithWeightedObservedPoints() {
        WeightedObservedPoint[] weightedObservedPoints = new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0, 2.0),
                new WeightedObservedPoint(4.03128248, 984167.0, 3.0),
                new WeightedObservedPoint(4.03839603, 1887233.0, 1.5)
        };
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonMethod());
        fitter.addObservedPoints(weightedObservedPoints);
        double[] result = fitter.fit();

        Assert.assertArrayEquals(expectedArray(parameters), result, EPSILON);
    }

    @Test
    public void testFitWithMultipleInitialGuesses() {
        double[][] initialGuesses = new double[][] {
                {15000.0, 4.2, 3.0},
                {20000.0, 4.0, 2.5},
                {12000.0, 4.1, 2.2}
        };
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonMethod());
        double[][] results = fitter.fit(initialGuesses);

        for (double[] result : results) {
            Assert.assertArrayEquals(expectedArray(parameters), result, EPSILON);
        }
    }

    @Test
    public void testFitWithMultipleObservations() {
        GaussianFitter fitter = new GaussianFitter(new PayneRiddersMethod());
        fitter.addObservedPoint(observations[0]);
        fitter.addObservedPoint(observations[observations.length - 1]);
        double[] result = fitter.fit();

        Assert.assertArrayEquals(expectedArray(parameters), result, EPSILON);
    }

    @Test
    public void testFitWithMinimizer() {
        UnconstrainedMinimizer minimizer = new GaussNewtonMethod();
        StoppingCondition stoppingCondition = new SimpleStoppingCondition(1e-6, 1000);
        PointValuePairList solutions = new PointValuePairList();
        UnivariateFunction function = new GaussianFittingFunction(observations);

        AveragedStartingPointGenerator generator = new AveragedStartingPointGenerator(10);
        InitialGuess initialGuess = generator.getInitialGuess(function, new SimpleBounds(0.0, 20.0), 3);
        PointValuePair pair = minimizer.minimize(function, initialGuess, stoppingCondition);
        double[] result = pair.getPoint();

        Assert.assertArrayEquals(expectedArray(parameters), result, EPSILON);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(1.0, 2.5, 1.0),
                new WeightedObservedPoint(2.0, 1.5, 1.0),
                new WeightedObservedPoint(3.0, 0.5, 1.0),
                new WeightedObservedPoint(4.0, -0.5, 1.0),
                new WeightedObservedPoint(5.0, -2.5, 1.0)
        };
        ParameterGuesser parameterGuesser = new ParameterGuesser(observations);
        double[] result = parameterGuesser.guess();

        Assert.assertArrayEquals(new double[] {5.0, 3.0, 1.391367232}, result, EPSILON);
    }

    @Test
    public void testParameterGuesserWithInvalidObservations() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                null,
                new WeightedObservedPoint(Double.NaN, Double.NaN, 1.0),
                new WeightedObservedPoint(Double.POSITIVE_INFINITY, 0.0, 1.0),
                new WeightedObservedPoint(4.0, Double.NaN, 1.0)
        };
        ParameterGuesser parameterGuesser = new ParameterGuesser(observations);

        try {
            parameterGuesser.guess();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private static double[] expectedArray(double[] array) {
        double[] result = new double[array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        return result;
    }

    private static class GaussianFittingFunction implements UnivariateFunction {
        private final WeightedObservedPoint[] observations;

        public GaussianFittingFunction(WeightedObservedPoint[] observations) {
            this.observations = observations;
        }

        @Override
        public double value(double x) throws OutOfRangeException {
            double sum = 0.0;
            for (WeightedObservedPoint point : observations) {
                sum += Math.pow(point.getWeight(), 2) * Math.pow(new Gaussian(parameters[0], parameters[1]).value(point.getX()) - point.getY(), 2);
            }
            return sum;
        }

        @Override
        public double[] gradient(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    private double[] parameters;
}
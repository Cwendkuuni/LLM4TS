package org.apache.commons.math.optimization.fitting;

import junit.framework.TestCase;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.function.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializer.AveragedInitialGuess;
import org.apache.commons.math3.optim.initializer.GaussianInitialGuess;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.convergence.Threshold;
import org.apache.commons.math3.optim.nonlinear.fitness.LeastSquaresFitnessFunction;
import org.apache.commons.math3.optim.nonlinear.gradient.GaussianDerivativeFunction;
import org.apache.commons.math3.optim.nonlinear.optimize.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.points.ExtremeValuePoint;
import org.apache.commons.math3.optim.points.MaxEval;
import org.apache.commons.math3.optim.points.PointsValuePair;
import org.apache.commons.math3.optim.restart.Restart;
import org.apache.commons.math3.optim.restart.RestartPolicy;
import org.apache.commons.math3.optim.restart.RestartValue;
import org.apache.commons.math3.optim.restart.SimpleRestartPolicy;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Tests for {@link GaussianFitter}.
 */
public class GaussianFitterTest extends TestCase {

    /**
     * Test fitting a Gaussian distribution to a set of points.
     */
    public void testFitExample() {
        final double[][] data = {
            { 4.0254623, 531026.0 },
            { 4.03128248, 984167.0 },
            { 4.03839603, 1887233.0 },
            { 4.04421621, 2687152.0 },
            { 4.05132976, 3461228.0 },
            { 4.05326982, 3580526.0 },
            { 4.05779662, 3439750.0 },
            { 4.0636168, 2877648.0 },
            { 4.06943698, 2175960.0 },
            { 4.07525716, 1447024.0 },
            { 4.08237071, 717104.0 },
            { 4.08366408, 620014.0 }
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[data.length];
        for (int i = 0; i < data.length; i++) {
            observations[i] = new WeightedObservedPoint(data[i][0], data[i][1], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        double[] result = fitter.fit();

        double expected[] = { 2.0, 4.05, 0.25 };
        assertEquals("Wrong norm", expected[0], result[0], 1e-12);
        assertEquals("Wrong mean", expected[1], result[1], 1e-12);
        assertEquals("Wrong sigma", expected[2], result[2], 1e-12);
    }

    /**
     * Test fitting a Gaussian distribution with invalid input.
     */
    public void testFitInvalidData() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());

        // Null data
        try {
            fitter.fit((WeightedObservedPoint[]) null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Empty data
        try {
            fitter.fit(new WeightedObservedPoint[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Data without at least 3 points
        try {
            WeightedObservedPoint[] data = new WeightedObservedPoint[2];
            fitter.fit(data);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Data with non-positive initial guess
        GaussianFitter fitter2 = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = { 1.0, 4.0, -1.0 };
        try {
            fitter2.fit(initialGuess);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Data with invalid input function
        try {
            GaussianFitter fitter3 = new GaussianFitter(null);
            fitter3.fit();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the parameter guessing.
     */
    public void testGuessing() {
        double[][] data = {
            { 4.05, 1.0},
            { 4.1, 100.0},
            { 4.15, 10000.0},
            { 4.2, 1000000.0},
            { 4.25, 10000000.0},
            { 4.3, 100000000.0},
            { 4.35, 1000000000.0},
            { 4.4, 10000000000.0},
            { 4.45, 100000000000.0},
            { 4.5, 1000000000000.0}
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[data.length];
        for (int i = 0; i < data.length; i++) {
            observations[i] = new WeightedObservedPoint(data[i][0], data[i][1], 1.0);
        }

        double[] initialGuess = new GaussianFitter(new LevenbergMarquardtOptimizer()).guess(observations);

        double expected[] = { 1.0e10, 4.275, 0.525 };
        assertEquals("Wrong norm", expected[0], initialGuess[0], 1e-12);
        assertEquals("Wrong mean", expected[1], initialGuess[1], 1e-12);
        assertEquals("Wrong sigma", expected[2], initialGuess[2], 1e-12);
    }

    /**
     * Test the fitting with multiple initial guesses.
     */
    public void testFitWithMultipleInitialGuesses() {
        double[][] data = {
            { 4.05, 1.0},
            { 4.1, 100.0},
            { 4.15, 10000.0},
            { 4.2, 1000000.0},
            { 4.25, 10000000.0},
            { 4.3, 100000000.0},
            { 4.35, 1000000000.0},
            { 4.4, 10000000000.0},
            { 4.45, 100000000000.0},
            { 4.5, 1000000000000.0}
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[data.length];
        for (int i = 0; i < data.length; i++) {
            observations[i] = new WeightedObservedPoint(data[i][0], data[i][1], 1.0);
        }

        double[][] initialGuesses = {
            { 1.0, 4.0, 0.5 },
            { 2.0, 4.1, 0.6 },
            { 3.0, 4.2, 0.7 },
            { 4.0, 4.3, 0.8 }
        };

        for (double[] initialGuess : initialGuesses) {
            GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
            fitter.addObservedPoints(observations);
            fitter.setInitialGuess(initialGuess);
            double[] result = fitter.fit();

            double expected[] = { 1.0e10, 4.275, 0.525 };
            assertEquals("Wrong norm", expected[0], result[0], 1e-12);
            assertEquals("Wrong mean", expected[1], result[1], 1e-12);
            assertEquals("Wrong sigma", expected[2], result[2], 1e-12);
        }
    }

    /**
     * Test the fitting with restarts.
     */
    public void testFitWithRestart() {
        double[][] data = {
            { 4.05, 1.0},
            { 4.1, 100.0},
            { 4.15, 10000.0},
            { 4.2, 1000000.0},
            { 4.25, 10000000.0},
            { 4.3, 100000000.0},
            { 4.35, 1000000000.0},
            { 4.4, 10000000000.0},
            { 4.45, 100000000000.0},
            { 4.5, 1000000000000.0}
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[data.length];
        for (int i = 0; i < data.length; i++) {
            observations[i] = new WeightedObservedPoint(data[i][0], data[i][1], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        RestartPolicy<ExtremeValuePoint> restartPolicy = new SimpleRestartPolicy<>(
                new MaxEval<>(3), new Threshold<>(1e-8, 30));
        Restart<PointValuePair> restart = new Restart<>(restartPolicy);

        double[] initialGuess = new double[] { 1.0, 4.0, 0.5 };
        double[] result = restart.optimize(new GaussianFitter(fitter), initialGuess,
                new LeastSquaresFitnessFunction(observations), new StoppingCondition(1e-10), new AveragedInitialGuess(2), new GaussianInitialGuess());

        double expected[] = { 1.0e10, 4.275, 0.525 };
        assertEquals("Wrong norm", expected[0], result[0], 1e-12);
        assertEquals("Wrong mean", expected[1], result[1], 1e-12);
        assertEquals("Wrong sigma", expected[2], result[2], 1e-12);
    }
}
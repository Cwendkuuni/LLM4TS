package org.apache.commons.math.optimization.fitting.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.initialization.Guess;
import org.apache.commons.math3.optim.initialization.OptimizationTestValues;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCriteria;
import org.apache.commons.math3.optim.nonlinear.gradient.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.WeightedObservedPoint;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.test.OpenMathAssert;
import org.apache.commons.math3.util.test.TestUtils;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPointList;
import org.apache.commons.math.optimization.fitting.ParameterGuesser;

/**
 * Test for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test for fitting a {@link Gaussian} function with known parameters using
     * {@link LevenbergMarquardtOptimizer}.
     *
     * @throws NotStrictlyPositiveException if initial guesses are not strictly
     * positive.
     */
    @Test
    public void testFitGaussianFunctionWithKnownParameters()
        throws NotStrictlyPositiveException {
        final double EPSILON = 1e-12;
        final double[] initialGuess = new double[] { 1.0, 0.5, 0.1 };
        final double[] expected = new double[] { 1.0, 0.5, 0.1 };

        final WeightedObservedPointList observations = new WeightedObservedPointList(
                new double[][] { { 0.2, 1.1 }, { 0.4, 1.5 }, { 0.6, 1.9 },
                                 { 0.8, 2.4 }, { 1.0, 3.1 } },
                new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });

        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer(EPSILON, 50));
        final double[] result = fitter.fit(initialGuess, observations);
        OpenMathAssert.compareArrays(expected, result, EPSILON);
        assertEquals(GoalType.MINIMIZE, fitter.getGoalType());
    }

    /**
     * Test for fitting a {@link Gaussian} function with known parameters using
     * {@link LevenbergMarquardtOptimizer} and initial guesses guessed by
     * {@link ParameterGuesser}.
     *
     * @throws NotStrictlyPositiveException if initial guesses are not strictly
     * positive.
     */
    @Test
    public void testFitGaussianFunctionWithKnownParametersAndAutomaticGuess()
        throws NotStrictlyPositiveException {
        final double EPSILON = 1e-12;
        final double[] initialGuess = new double[] { 0.0, 0.0, 0.0 };

        final WeightedObservedPointList observations = new WeightedObservedPointList(
                new double[][] { { 0.2, 1.1 }, { 0.4, 1.5 }, { 0.6, 1.9 },
                                 { 0.8, 2.4 }, { 1.0, 3.1 } },
                new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });

        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer(EPSILON, 50));
        final double[] expected = new double[] { 1.0, 0.5, 0.1 };
        final double[] result = fitter.fit(observations);
        OpenMathAssert.compareArrays(expected, result, EPSILON);
        assertEquals(GoalType.MINIMIZE, fitter.getGoalType());
    }

    /**
     * Test for fitting a {@link Gaussian} function with incorrect number of
     * observations.
     *
     * @throws NotStrictlyPositiveException if initial guesses are not strictly
     * positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianFunctionWithIncorrectNumberOfObservations()
        throws NotStrictlyPositiveException {
        final double[] initialGuess = new double[] { 1.0, 0.5, 0.1 };
        final WeightedObservedPoint observation = new WeightedObservedPoint(0.2, 1.1, 1.0);
        final WeightedObservedPointList observations = new WeightedObservedPointList(
                new double[][] { { observation.getX(), observation.getY() } },
                new double[] { observation.getWeight() });
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(initialGuess, observations);
    }

    /**
     * Test for fitting a {@link Gaussian} function with incorrect initial guesses.
     *
     * @throws NotStrictlyPositiveException if initial guesses are not strictly
     * positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianFunctionWithIncorrectInitialGuesses()
        throws NotStrictlyPositiveException {
        final double[] initialGuess = new double[] { 1.0, 0.0, -0.1 };
        final WeightedObservedPointList observations = new WeightedObservedPointList(
                new double[][] { { 0.2, 1.1 }, { 0.4, 1.5 }, { 0.6, 1.9 },
                                 { 0.8, 2.4 }, { 1.0, 3.1 } },
                new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(initialGuess, observations);
    }

    /**
     * Test for fitting a {@link Gaussian} function with incorrect stop criteria.
     *
     * @throws NotStrictlyPositiveException if initial guesses are not strictly
     * positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianFunctionWithIncorrectStopCriteria()
        throws NotStrictlyPositiveException {
        final double[] initialGuess = new double[] { 1.0, 0.5, 0.1 };
        final WeightedObservedPointList observations = new WeightedObservedPointList(
                new double[][] { { 0.2, 1.1 }, { 0.4, 1.5 }, { 0.6, 1.9 },
                                 { 0.8, 2.4 }, { 1.0, 3.1 } },
                new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.setStoppingCriteria(null);
        fitter.fit(initialGuess, observations);
    }

    /**
     * Test for fitting a {@link Gaussian} function with incorrect goal type.
     *
     * @throws NotStrictlyPositiveException if initial guesses are not strictly
     * positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitGaussianFunctionWithIncorrectGoalType()
        throws NotStrictlyPositiveException {
        final double[] initialGuess = new double[] { 1.0, 0.5, 0.1 };
        final WeightedObservedPointList observations = new WeightedObservedPointList(
                new double[][] { { 0.2, 1.1 }, { 0.4, 1.5 }, { 0.6, 1.9 },
                                 { 0.8, 2.4 }, { 1.0, 3.1 } },
                new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.setGoalType(null);
        fitter.fit(initialGuess, observations);
    }

    /**
     * Test for throwing exception when fitting a non-Gaussian function.
     *
     * @throws NotStrictlyPositiveException if initial guesses are not strictly
     * positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitNonGaussianFunction()
        throws NotStrictlyPositiveException {
        final double[] initialGuess = new double[] { 1.0, 0.5, 0.1 };
        final ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
                public double value(double[] parameters) {
                    return Math.pow(Math.PI * Math.pow(parameters[0], 2)
                            + Math.pow(parameters[1] - 2.5, 2), 0.5);
                }

                public double[] gradient(double[] parameters) {
                    throw new UnsupportedOperationException();
                }
            };
        final WeightedObservedPointList observations = new WeightedObservedPointList(
                new double[][] { { 0.2, 1.1 }, { 0.4, 1.5 }, { 0.6, 1.9 },
                                 { 0.8, 2.4 }, { 1.0, 3.1 } },
                new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.setObjectiveFunction(objectiveFunction);
        fitter.fit(initialGuess, observations);
    }

    /**
     * Test for throwing exception when fitting a non-Gaussian function with
     * incorrect initial guesses.
     *
     * @throws NotStrictlyPositiveException if initial guesses are not strictly
     * positive.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitNonGaussianFunctionWithIncorrectInitialGuesses()
        throws NotStrictlyPositiveException {
        final ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
                public double value(double[] parameters) {
                    return Math.pow(Math.PI * Math.pow(parameters[0], 2)
                            + Math.pow(parameters[1] - 2.5, 2), 0.5);
                }

                public double[] gradient(double[] parameters) {
                    throw new UnsupportedOperationException();
                }
            };
        final WeightedObservedPointList observations = new WeightedObservedPointList(
                new double[][] { { 0.2, 1.1 }, { 0.4, 1.5 }, { 0.6, 1.9 },
                                 { 0.8, 2.4 }, { 1.0, 3.1 } },
                new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });
        final GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.setObjectiveFunction(objectiveFunction);
        fitter.setInitialGuess(new double[] { 1.0, 0.0, 0.1 });
        fitter.fit(observations);
    }
}
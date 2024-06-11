package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    private static final double EPSILON = 1.0e-14;
    private static final double MINIMUM = 2.0;
    private static final double MAXIMUM = 4.0;
    private static final int MAX_EVAL = 100;
    private static final int NUM_STARTS = 3;

    private UnivariateRealFunction testFunction;
    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private PointValuePair bestSolution;
    private PointValuePairList allSolutions;
    private RandomGenerator rg;

    @Before
    public void setUp() {
        testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        optimizer = new FibonacciSearchOptimizer(testFunction, MAX_EVAL);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, NUM_STARTS, new MersenneTwister());
        rg = new MersenneTwister();
    }

    @Test
    public void testOptimizeMinimum() throws FunctionEvaluationException, ConvergenceException {
        SimpleBounds bounds = new SimpleBounds(MINIMUM, MAXIMUM);
        GoalType goal = GoalType.MINIMIZE;
        bestSolution = multiStartOptimizer.optimize(testFunction, goal, bounds);
        assertArrayEquals(new double[] {MINIMUM}, new double[] {bestSolution.getPoint()[0]}, EPSILON);
        allSolutions = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[] {bestSolution, null, null}, allSolutions.toArray(), EPSILON);
    }

    @Test
    public void testOptimizeMaximum() throws FunctionEvaluationException, ConvergenceException {
        SimpleBounds bounds = new SimpleBounds(MINIMUM, MAXIMUM);
        GoalType goal = GoalType.MAXIMIZE;
        bestSolution = multiStartOptimizer.optimize(testFunction, goal, bounds);
        assertArrayEquals(new double[] {MAXIMUM}, new double[] {bestSolution.getPoint()[0]}, EPSILON);
        allSolutions = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[] {null, null, bestSolution}, allSolutions.toArray(), EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws FunctionEvaluationException, ConvergenceException {
        SimpleBounds bounds = new SimpleBounds(MINIMUM, MAXIMUM);
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer.setMaxEvaluations(MAX_EVAL * 2);
        multiStartOptimizer.optimize(testFunction, goal, bounds);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws FunctionEvaluationException, ConvergenceException {
        SimpleBounds bounds = new SimpleBounds(MINIMUM, MAXIMUM);
        GoalType goal = GoalType.MINIMIZE;
        testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, NUM_STARTS, new MersenneTwister());
        multiStartOptimizer.optimize(testFunction, goal, bounds);
    }

    @Test(expected = IllegalStateException.class)
    public void testNoOptima() throws FunctionEvaluationException, ConvergenceException {
        testFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 0.5;
            }
        };
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, NUM_STARTS, new MersenneTwister());
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, new SimpleBounds(0.0, 1.0));
    }
}
package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergentIterationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.UnivariateRealOptimizerFactory;
import org.apache.commons.math3.optim.noderiv.BrentOptimizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealOptimizer wrappedOptimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        final BrentOptimizer baseOptimizer = new BrentOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer(baseOptimizer, 5, new BasicRandom());
        wrappedOptimizer = optimizer.getOptimizer();
        generator = optimizer.getGenerator();
    }

    @Test
    public void testMinimization() {
        final UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        GoalType goal = GoalType.MINIMIZE;
        double min = 3.0;
        double max = 5.0;
        try {
            double best = optimizer.optimize(f, goal, min, max);
            double expected = -1.0;
            assertEquals(expected, best, 1e-6);
        } catch (ConvergenceException ce) {
            fail("Minimization should converge.");
        } catch (FunctionEvaluationException fe) {
            fail("Minimization should not throw FunctionEvaluationException.");
        }
    }

    @Test
    public void testMaximization() {
        final UnivariateRealFunction f = new PolynomialFunction(2, new double[]{1.0, -1.0, 1.0});
        GoalType goal = GoalType.MAXIMIZE;
        double min = 3.0;
        double max = 5.0;
        try {
            double best = optimizer.optimize(f, goal, min, max);
            double expected = 1.0;
            assertEquals(expected, best, 1e-6);
        } catch (ConvergenceException ce) {
            fail("Maximization should converge.");
        } catch (FunctionEvaluationException fe) {
            fail("Maximization should not throw FunctionEvaluationException.");
        }
    }

    @Test
    public void testIterationLimit() {
        final UnivariateRealFunction f = new PolynomialFunction(2, new double[]{1.0, -1.0, 1.0});
        GoalType goal = GoalType.MAXIMIZE;
        double min = 3.0;
        double max = 5.0;
        optimizer.setMaximalIterationCount(10);
        try {
            optimizer.optimize(f, goal, min, max);
            fail("Iteration limit should be reached.");
        } catch (ConvergenceException ce) {
            // expected
        } catch (FunctionEvaluationException fe) {
            fail("Test should not throw FunctionEvaluationException.");
        }
    }

    @Test
    public void testEvaluationLimit() {
        final UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        GoalType goal = GoalType.MINIMIZE;
        double min = 3.0;
        double max = 5.0;
        optimizer.setMaxEvaluations(10);
        try {
            optimizer.optimize(f, goal, min, max);
            fail("Evaluation limit should be reached.");
        } catch (ConvergenceException ce) {
            // expected
        } catch (FunctionEvaluationException fe) {
            fail("Test should not throw FunctionEvaluationException.");
        }
    }

    @Test
    public void testNonConvergence() {
        final UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0});
        GoalType goal = GoalType.MINIMIZE;
        double min = 3.0;
        double max = 5.0;
        try {
            optimizer.optimize(f, goal, min, max);
            fail("Test should throw ConvergenceException.");
        } catch (ConvergenceException ce) {
            // expected
        } catch (FunctionEvaluationException fe) {
            fail("Test should not throw FunctionEvaluationException.");
        }
    }

    @Test
    public void testMultipleStartPoints() {
        final UnivariateRealFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, -1.0});
        GoalType goal = GoalType.MINIMIZE;
        double min = 3.0;
        double max = 5.0;
        try {
            PointValuePair[] optima = optimizer.getOptima();
            int numStarts = optimizer.getStarts();
            assertEquals(numStarts, optima.length);
            for (PointValuePair p : optima) {
                assertFalse(p.getValue() == Double.NaN);
            }
            optimizer.optimize(f, goal, min, max);
            optima = optimizer.getOptima();
            for (PointValuePair p : optima) {
                assertFalse(p.getValue() == Double.NaN);
            }
        } catch (ConvergenceException ce) {
            fail("Test should not throw ConvergenceException.");
        } catch (FunctionEvaluationException fe) {
            fail("Test should not throw FunctionEvaluationException.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfStarts() {
        optimizer = new MultiStartUnivariateRealOptimizer(wrappedOptimizer, -5, new BasicRandom());
    }

    @Test(expected = NullPointerException.class)
    public void testNullGenerator() {
        optimizer = new MultiStartUnivariateRealOptimizer(wrappedOptimizer, 5, null);
    }
}
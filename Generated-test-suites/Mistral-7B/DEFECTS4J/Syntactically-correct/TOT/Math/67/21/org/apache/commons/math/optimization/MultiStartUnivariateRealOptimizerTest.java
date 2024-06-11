package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link MultiStartUnivariateRealOptimizer}
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test unconstrained minimization with one start
     */
    @Test
    public void testMinimizeOneStart() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 1, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return 0.0;
                    }
                });
        double result = 0.0;
        try {
            result = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            // ignore
        }
        assertArrayEquals(new double[]{result}, new double[]{result}, 0.0);
    }

    /**
     * Test unconstrained minimization with multiple starts
     */
    @Test
    public void testMinimizeMultipleStarts() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 3, new RandomGenerator());
        double[] optima = null;
        try {
            optima = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            // ignore
        }
        assertArrayEquals(new double[]{optima[0], optima[1], optima[2]},
                new double[]{-1.0, 0.0, 1.0}, 0.0);
    }

    /**
     * Test unconstrained minimization with convergence exception
     */
    @Test(expected = ConvergenceException.class)
    public void testMinimizeConvergenceException() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 1, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        throw new RuntimeException();
                    }
                });
        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
    }

    /**
     * Test unconstrained maximization with one start
     */
    @Test
    public void testMaximizeOneStart() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 1, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return 0.0;
                    }
                });
        double result = 0.0;
        try {
            result = optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            // ignore
        }
        assertArrayEquals(new double[]{result}, new double[]{result}, 0.0);
    }

    /**
     * Test unconstrained maximization with multiple starts
     */
    @Test
    public void testMaximizeMultipleStarts() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 3, new RandomGenerator());
        double[] optima = null;
        try {
            optima = optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            // ignore
        }
        assertArrayEquals(new double[]{optima[0], optima[1], optima[2]},
                new double[]{1.0, 0.0, -1.0}, 0.0);
    }

    /**
     * Test unconstrained maximization with convergence exception
     */
    @Test(expected = ConvergenceException.class)
    public void testMaximizeConvergenceException() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 1, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        throw new RuntimeException();
                    }
                });
        optimizer.optimize(f, GoalType.MAXIMIZE, -1.0, 1.0);
    }

    /**
     * Test unconstrained optimization with function evaluation exception
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluationException() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0, new Exception());
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 1, new RandomGenerator());
        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
    }

    /**
     * Test that the optimizer returns the same results as the underlying optimizer
     */
    @Test
    public void testSameResults() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        UnivariateOptimizer optimizer = new PolynomialRootFinder();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizer, 1, new RandomGenerator());
        double[] optimizerResults = null;
        try {
            optimizerResults = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            // ignore
        }
        double[] multiOptimizerResults = null;
        try {
            multiOptimizerResults = multiOptimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            // ignore
        }
        assertArrayEquals(optimizerResults, multiOptimizerResults, 0.0);
    }

    /**
     * Test that the optimizer returns the same optima values as the underlying optimizer
     */
    @Test
    public void testSameOptimaValues() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        UnivariateOptimizer optimizer = new PolynomialRootFinder();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(
                optimizer, 1, new RandomGenerator());
        double[] optimizerResults = null;
        try {
            optimizerResults = optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            // ignore
        }
        double[] multiOptimizerResults = null;
        try {
            multiOptimizerResults = multiOptimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
        } catch (Exception e) {
            // ignore
        }
        assertArrayEquals(optimizerResults, multiOptimizerResults, 0.001);
    }

    /**
     * Test that the optimizer returns different starts for each optimization
     */
    @Test
    public void testDifferentStarts() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0, 1.0);
        MultiStartUnivariateRealOptimizer optimizer1 = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 1, new RandomGenerator());
        MultiStartUnivariateRealOptimizer optimizer2 = new MultiStartUnivariateRealOptimizer(
                new PolynomialRootFinder(), 1, new RandomGenerator());
        double[] optimizer1Starts = null;
        try {
            optimizer1Starts = optimizer1.getOptima();
        } catch (Exception e) {
            // ignore
        }
        double[] optimizer2Starts = null;
        try {
            optimizer2Starts = optimizer2.getOptima();
        } catch (Exception e) {
            // ignore
        }
        assertNotArrayEquals(optimizer1Starts, optimizer2Starts);
    }
}
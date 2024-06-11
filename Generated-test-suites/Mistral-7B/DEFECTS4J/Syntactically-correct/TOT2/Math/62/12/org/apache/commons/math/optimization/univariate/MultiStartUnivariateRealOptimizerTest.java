package org.apache.commons.math.optimization.univariate;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BrentMethod;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.random.RandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartMinimization() {
        RandomGenerator rg = new MersenneTwisterFast();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rg);
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0, 1.0, -1.0}, 5);

        // Test optimization with multiple starts
        double min = -5.0;
        double max = 5.0;
        GoalType goal = MINIMIZE;
        PointValuePair bestPoint = multiStartOptimizer.optimize(f, goal, min, max);
        double bestValue = bestPoint.getValue();
        double expectedBestValue = -12.0;
        assertEquals(expectedBestValue, bestValue, 1e-6);

        // Test all points found during optimization
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(5, optima.length);
        for (int i = 0; i < optima.length; i++) {
            assertNotNull(optima[i]);
            assertEquals(goal, optima[i].getGoalType());
        }
    }

    @Test
    public void testMultiStartMaximization() {
        RandomGenerator rg = new MersenneTwisterFast();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rg);
        PolynomialFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 5);

        // Test optimization with multiple starts
        double min = -5.0;
        double max = 5.0;
        GoalType goal = MAXIMIZE;
        PointValuePair bestPoint = multiStartOptimizer.optimize(f, goal, min, max);
        double bestValue = bestPoint.getValue();
        double expectedBestValue = 12.0;
        assertEquals(expectedBestValue, bestValue, 1e-6);

        // Test all points found during optimization
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(5, optima.length);
        for (int i = 0; i < optima.length; i++) {
            assertNotNull(optima[i]);
            assertEquals(goal, optima[i].getGoalType());
        }
    }

    @Test
    public void testMultiStartOptimizationWithConvergenceChecker() {
        RandomGenerator rg = new MersenneTwisterFast();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = new MyConvergenceChecker<>();
        MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rg);
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        PolynomialFunction f = new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 5);

        // Test optimization with multiple starts and convergence checker
        double min = -5.0;
        double max = 5.0;
        GoalType goal = MINIMIZE;
        PointValuePair bestPoint = multiStartOptimizer.optimize(f, goal, min, max);
        double bestValue = bestPoint.getValue();
        double expectedBestValue = -12.0;
        assertEquals(expectedBestValue, bestValue, 1e-6);

        // Test all points found during optimization
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(5, optima.length);
        for (int i = 0; i < optima.length; i++) {
            assertNotNull(optima[i]);
            assertEquals(goal, optima[i].getGoalType());
        }

        // Test if convergence checker was called during optimization
        assertTrue(convergenceChecker.wasCalled());
    }

    @Test
    public void testMultiStartOptimizationWithMaxEvaluations() {
        RandomGenerator rg = new MersenneTwisterFast();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rg);

        // Test optimization with multiple starts and maxEvaluations
        double min = -5.0;
        double max = 5.0;
        GoalType goal = MINIMIZE;
        int maxEvaluations = 10;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        PointValuePair bestPoint = null;
        try {
            bestPoint = multiStartOptimizer.optimize(new PolynomialFunction(new double[]{1.0, -1.0, 1.0, -1.0, 1.0}, 5), goal, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test all points found during optimization
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(5, optima.length);
        for (int i = 0; i < optima.length; i++) {
            assertNotNull(optima[i]);
            assertEquals(goal, optima[i].getGoalType());
        }

        // Test maxEvaluations was respected
        assertEquals(maxEvaluations, multiStartOptimizer.getEvaluations());
    }

    @Test(expected = IllegalStateException.class)
    public void testMultiStartOptimizationWithoutCallingOptimize() {
        RandomGenerator rg = new MersenneTwisterFast();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, rg);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        fail("Expected MathIllegalStateException");
    }

    class MyConvergenceChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {
        private boolean wasCalled = false;

        @Override
        public boolean check(UnivariateRealPointValuePair value) {
            wasCalled = true;
            return true;
        }

        public boolean wasCalled() {
            return wasCalled;
        }
    }
}
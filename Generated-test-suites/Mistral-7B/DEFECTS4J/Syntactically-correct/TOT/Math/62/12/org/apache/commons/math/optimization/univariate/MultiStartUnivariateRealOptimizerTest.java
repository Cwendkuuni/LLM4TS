package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.SimpleGoalTypeChecker;
import org.apache.commons.math.optimization.SimpleLineSearch;
import org.apache.commons.math.optimization.noderiv.BrentOptimizer;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {
    private BaseUnivariateRealOptimizer<UnivariateFunction> optimizer;
    private MultiStartUnivariateRealOptimizer<UnivariateFunction> multiStartOptimizer;
    private UnivariateRealFunction testFunction;
    private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;
    private RandomDataGenerator random;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        convergenceChecker = new SimpleGoalTypeChecker(GoalType.MINIMIZE);
        random = new RandomDataGenerator();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, random);
        testFunction = new TestFunction();
    }

    @Test
    public void testMinimization() throws Exception {
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.0, result.getPoint(), 1e-8);
        Assert.assertEquals(-1.0, result.getValue(), 1e-8);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testMinimizationWithNoSolution() throws Exception {
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        UnivariateFunction nonConvergentFunction = new NonConvergentFunction();
        multiStartOptimizer.optimize(nonConvergentFunction, GoalType.MINIMIZE, -5.0, 5.0);
    }

    @Test
    public void testMinimizationWithDifferentStartingPoints() throws Exception {
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0, 2.5);
        Assert.assertNotNull(optima);
        Assert.assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            Assert.assertNotNull(pair);
            Assert.assertNotEquals(pair.getPoint(), pair.getValue());
            Assert.assertEquals(-1.0, pair.getValue(), 1e-8);
        }
    }

    @Test
    public void testMaximization() throws Exception {
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MAXIMIZE, -5.0, 5.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(5.0, result.getPoint(), 1e-8);
        Assert.assertEquals(5.0, result.getValue(), 1e-8);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testMaximizationWithNegativeGoalType() {
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        multiStartOptimizer.optimize(testFunction, GoalType.MAXIMIZE, -5.0, 5.0);
    }

    @Test
    public void testMinimizationWithMaxEvaluations() throws Exception {
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        multiStartOptimizer.setMaxEvaluations(1);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.0, result.getPoint(), 1e-8);
        Assert.assertEquals(-1.0, result.getValue(), 1e-8);
    }

    @Test
    public void testMaximizationWithMaxEvaluations() throws Exception {
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);
        multiStartOptimizer.setMaxEvaluations(1);
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MAXIMIZE, -5.0, 5.0);
        Assert.assertNull(result);
    }

    @Test
    public void testMinimizationWithNullGoalTypeChecker() {
        multiStartOptimizer.setConvergenceChecker(null);
        try {
            multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -5.0, 5.0);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("goal type checker cannot be null"));
        }
    }

    @Test
    public void testMaximizationWithNullGoalTypeChecker() {
        multiStartOptimizer.setConvergenceChecker(null);
        try {
            multiStartOptimizer.optimize(testFunction, GoalType.MAXIMIZE, -5.0, 5.0);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("goal type checker cannot be null"));
        }
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return FunctionUtils.signum(Math.sin(x)) * Math.pow(Math.abs(Math.sin(x)), 2.0);
        }

        @Override
        public double derivative(double x) {
            return 2.0 * Math.sin(x) * Math.cos(x);
        }

        @Override
        public double derivative2(double x) {
            return 2.0 * (Math.cos(x) * Math.cos(x) - Math.sin(x) * Math.sin(x));
        }
    }

    private static class NonConvergentFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new UnsupportedOperationException("This function is non-convergent");
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException("This function is non-convergent");
        }

        @Override
        public double derivative2(double x) {
            throw new UnsupportedOperationException("This function is non-convergent");
        }
    }
}
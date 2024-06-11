package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergenceException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * Test file for MultiStartUnivariateRealOptimizer class
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}
     * @throws Exception
     */
    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizer() throws Exception {
        // Test initialization
        UnivariateRealOptimizer optimizer = new org.apache.commons.math3.optim.SimpleXOptimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertNotNull(multiOptimizer);
        assertEquals(optimizer.getClass(), multiOptimizer.getOptimizer().getClass());
        assertEquals(optimizer.getClass().getSimpleName() + "MultiStart", multiOptimizer.getClass().getSimpleName());

        // Test getters and setters
        assertEquals(Integer.MAX_VALUE, multiOptimizer.getMaxIterations());
        assertEquals(Integer.MAX_VALUE, multiOptimizer.getMaxEvaluations());
        assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
        assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        assertNull(multiOptimizer.getOptima());
        assertNull(multiOptimizer.getOptimaValues());
        assertEquals(0, multiOptimizer.getIterationCount());
        assertEquals(0, multiOptimizer.getEvaluations());
        assertEquals(Double.NaN, multiOptimizer.getFunctionValue(), 0.0);
        assertEquals(Double.NaN, multiOptimizer.getResult(), 0.0);
        assertEquals(Double.NaN, multiOptimizer.getAbsoluteAccuracy(), 0.0);
        assertEquals(Double.NaN, multiOptimizer.getRelativeAccuracy(), 0.0);

        // Test optimization
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, 3);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double result = multiOptimizer.optimize(f, goalType, min, max);
        PointValuePair pvp = optimizer.optimize(f, goalType, min, max);
        assertEquals(pvp.getValue(), result, 1e-8);
        assertArrayEquals(new double[]{pvp.getPoint()}, multiOptimizer.getOptima(), 1e-8);
        assertArrayEquals(new double[]{pvp.getValue()}, multiOptimizer.getOptimaValues(), 1e-8);
        assertEquals(multiOptimizer.getIterationCount(), 5 * optimizer.getMaxIterations(), 5);
        assertEquals(multiOptimizer.getEvaluations(), 5 * optimizer.getMaxEvaluations(), 5);

        // Test that multiple starts converge to the same minimum
        for (int i = 0; i < 5; ++i) {
            result = multiOptimizer.optimize(f, goalType, min, max);
            pvp = optimizer.optimize(f, goalType, min, max);
            assertEquals(pvp.getValue(), result, 1e-8);
        }

        // Test optimization with non-convergence
        UnivariateRealFunction fNC = new PolynomialFunction(new double[]{1.0, 0.0, 0.0}, 3);
        try {
            multiOptimizer.optimize(fNC, goalType, min, max);
            fail("An OptimizationException should have been thrown.");
        } catch (OptimizationException oe) {
            // optimization failed, test that no optima were found
            assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
            assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        }
        try {
            multiOptimizer.optimize(fNC, goalType, min, max, startValue);
            fail("An OptimizationException should have been thrown.");
        } catch (OptimizationException oe) {
            // optimization failed, test that no optima were found
            assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
            assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        }

        // Test optimization with no minimum
        UnivariateRealFunction fNM = new PolynomialFunction(new double[]{0.0, 0.0, 1.0}, 3);
        try {
            multiOptimizer.optimize(fNM, goalType, min, max);
            fail("An OptimizationException should have been thrown.");
        } catch (NonConvergenceException nc) {
            // optimization failed, test that no optima were found
            assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
            assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        }
        try {
            multiOptimizer.optimize(fNM, goalType, min, max, startValue);
            fail("An OptimizationException should have been thrown.");
        } catch (NonConvergenceException nc) {
            // optimization failed, test that no optima were found
            assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
            assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        }

        // Test optimization with maximum
        GoalType goalTypeMax = GoalType.MAXIMIZE;
        double maxResult = multiOptimizer.optimize(f, goalTypeMax, min, max);
        pvp = optimizer.optimize(f, goalTypeMax, min, max);
        assertEquals(pvp.getValue(), maxResult, 1e-8);
        assertArrayEquals(new double[]{pvp.getPoint()}, multiOptimizer.getOptima(), 1e-8);
        assertArrayEquals(new double[]{pvp.getValue()}, multiOptimizer.getOptimaValues(), 1e-8);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator, int, int)}
     */
    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizer2() throws Exception {
        // Test initialization
        UnivariateRealOptimizer optimizer = new org.apache.commons.math3.optim.SimpleXOptimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister(), 10, 100);
        assertNotNull(multiOptimizer);
        assertEquals(optimizer.getClass(), multiOptimizer.getOptimizer().getClass());
        assertEquals(optimizer.getClass().getSimpleName() + "MultiStart", multiOptimizer.getClass().getSimpleName());

        // Test optimization
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, 3);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double result = multiOptimizer.optimize(f, goalType, min, max);
        PointValuePair pvp = optimizer.optimize(f, goalType, min, max);
        assertEquals(pvp.getValue(), result, 1e-8);
        assertArrayEquals(new double[]{pvp.getPoint()}, multiOptimizer.getOptima(), 1e-8);
        assertArrayEquals(new double[]{pvp.getValue()}, multiOptimizer.getOptimaValues(), 1e-8);
        assertEquals(multiOptimizer.getIterationCount(), 10 * optimizer.getMaxIterations(), 10);
        assertEquals(multiOptimizer.getEvaluations(), 10 * optimizer.getMaxEvaluations(), 10);

        // Test that multiple starts converge to the same minimum
        for (int i = 0; i < 5; ++i) {
            result = multiOptimizer.optimize(f, goalType, min, max);
            pvp = optimizer.optimize(f, goalType, min, max);
            assertEquals(pvp.getValue(), result, 1e-8);
        }

        // Test optimization with non-convergence
        UnivariateRealFunction fNC = new PolynomialFunction(new double[]{1.0, 0.0, 0.0}, 3);
        try {
            multiOptimizer.optimize(fNC, goalType, min, max);
            fail("An OptimizationException should have been thrown.");
        } catch (OptimizationException oe) {
            // optimization failed, test that no optima were found
            assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
            assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        }
        try {
            multiOptimizer.optimize(fNC, goalType, min, max, startValue);
            fail("An OptimizationException should have been thrown.");
        } catch (OptimizationException oe) {
            // optimization failed, test that no optima were found
            assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
            assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        }

        // Test optimization with no minimum
        UnivariateRealFunction fNM = new PolynomialFunction(new double[]{0.0, 0.0, 1.0}, 3);
        try {
            multiOptimizer.optimize(fNM, goalType, min, max);
            fail("An OptimizationException should have been thrown.");
        } catch (NonConvergenceException nc) {
            // optimization failed, test that no optima were found
            assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
            assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        }
        try {
            multiOptimizer.optimize(fNM, goalType, min, max, startValue);
            fail("An OptimizationException should have been thrown.");
        } catch (NonConvergenceException nc) {
            // optimization failed, test that no optima were found
            assertArrayEquals(new double[0], multiOptimizer.getOptima(), 0.0);
            assertArrayEquals(new double[0], multiOptimizer.getOptimaValues(), 0.0);
        }

        // Test optimization with maximum
        GoalType goalTypeMax = GoalType.MAXIMIZE;
        double maxResult = multiOptimizer.optimize(f, goalTypeMax, min, max);
        pvp = optimizer.optimize(f, goalTypeMax, min, max);
        assertEquals(pvp.getValue(), maxResult, 1e-8);
        assertArrayEquals(new double[]{pvp.getPoint()}, multiOptimizer.getOptima(), 1e-8);
        assertArrayEquals(new double[]{pvp.getValue()}, multiOptimizer.getOptimaValues(), 1e-8);
    }

}
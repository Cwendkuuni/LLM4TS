package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.AbstractSimplex;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}
 */
public class SimplexOptimizerTest {

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer()}.
     */
    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(ConvergenceChecker)}.
     */
    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        assertNotNull(optimizer);
        when(checker.converged(1, null, null)).thenReturn(true);
        MultivariateFunction evalFunc = mock(MultivariateFunction.class);
        PointValuePair pointValuePair = new PointValuePair(new double[]{}, 0.0);
        when(evalFunc.value(pointValuePair.getPoint())).thenReturn(0.0);
        when(checker.check(Mockito.anyInt(), Mockito.any(PointValuePair[].class),
                Mockito.eq(pointValuePair))).thenReturn(true);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData optimizationData = mock(OptimizationData.class);
        when(optimizationData.getFunction()).thenReturn(evalFunc);
        when(optimizationData.getInitialGuess()).thenReturn(pointValuePair.getPoint());
        PointValuePair result = optimizer.optimize(1, evalFunc, goalType, optimizationData);
        assertNotNull(result);
        assertEquals(pointValuePair.getPoint(), result.getPoint());
        assertEquals(pointValuePair.getValue(), result.getValue(), 0.0);
    }

    /**
     * Test method for {@link SimplexOptimizer#SimplexOptimizer(double, double)}.
     */
    @Test
    public void testConstructorWithRelAndAbs() {
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.001, checker);
        assertNotNull(optimizer);
        when(checker.converged(1, null, null)).thenReturn(true);
        MultivariateFunction evalFunc = mock(MultivariateFunction.class);
        PointValuePair pointValuePair = new PointValuePair(new double[]{}, 0.0);
        when(evalFunc.value(pointValuePair.getPoint())).thenReturn(0.0);
        when(checker.check(Mockito.anyInt(), Mockito.any(PointValuePair[].class),
                Mockito.eq(pointValuePair))).thenReturn(true);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData optimizationData = mock(OptimizationData.class);
        when(optimizationData.getFunction()).thenReturn(evalFunc);
        when(optimizationData.getInitialGuess()).thenReturn(pointValuePair.getPoint());
        PointValuePair result = optimizer.optimize(1, evalFunc, goalType, optimizationData);
        assertNotNull(result);
        assertEquals(pointValuePair.getPoint(), result.getPoint());
        assertEquals(pointValuePair.getValue(), result.getValue(), 0.001);
    }

    /**
     * Test method for {@link SimplexOptimizer#setSimplex(AbstractSimplex)}.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = mock(AbstractSimplex.class);
        when(simplex.getSize()).thenReturn(3);
        optimizer.setSimplex(simplex);

        MultivariateFunction evalFunc = mock(MultivariateFunction.class);
        when(evalFunc.value(Mockito.anyDoubleArray())).thenAnswer(i -> i[0] * i[0]);
        PointValuePair bestPointValuePair = new PointValuePair(new double[]{1.0, 1.0}, 1.0);
        when(simplex.getPoint(Mockito.anyInt())).thenReturn(bestPointValuePair.getPoint());
        PointValuePair[] previous = new PointValuePair[]{new PointValuePair(new double[]{0.0, 0.0}, 0.0),
            new PointValuePair(new double[]{2.0, 2.0}, 4.0), new PointValuePair(new double[]{3.0, 3.0}, 9.0)};
        when(simplex.getPoints()).thenReturn(previous);

        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData optimizationData = mock(OptimizationData.class);
        when(optimizationData.getFunction()).thenReturn(evalFunc);
        when(optimizationData.getInitialGuess()).thenReturn(new double[] {2.0, 2.0});

        PointValuePair result = optimizer.optimize(10, evalFunc, goalType, optimizationData);
        assertArrayEquals(bestPointValuePair.getPoint(), result.getPoint(), 0.0);
        assertEquals(bestPointValuePair.getValue(), result.getValue(), 0.0);
    }
}
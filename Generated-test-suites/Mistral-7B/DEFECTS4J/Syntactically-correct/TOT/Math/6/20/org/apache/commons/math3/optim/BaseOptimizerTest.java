package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.executors.GoalType;
import org.apache.commons.math3.optim.initializers.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCount;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.NoConvergence;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciHalfGoldenSectionSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PowellDirectionSet;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BaseOptimizer} interface.
 *
 * @since 3.0
 */
public class BaseOptimizerTest {

    /**
     * Test for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        BaseOptimizer<PointValuePair> optimizer = new FibonacciHalfGoldenSectionSearch(new ObjectiveFunction() {},
                new SimpleBounds(-10.0, 10.0), new AveragedStartingPointInitializer(-1.0, 1.0), new PowellDirectionSet(),
                new SimpleConvergenceChecker(1.0e-6, new NoConvergence()), GoalType.MINIMIZE, GoalTypeCount.MINIMIZE,
                new MaxEval(maxEvaluations));
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    /**
     * Test for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new FibonacciHalfGoldenSectionSearch(new ObjectiveFunction() {},
                new SimpleBounds(-10.0, 10.0), new AveragedStartingPointInitializer(-1.0, 1.0), new PowellDirectionSet(),
                new SimpleConvergenceChecker(1.0e-6, new NoConvergence()), GoalType.MINIMIZE, GoalTypeCount.MINIMIZE, new MaxEval(5));
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize();
        assertEquals(5, optimizer.getEvaluations());
    }

    /**
     * Test for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new FibonacciHalfGoldenSectionSearch(new ObjectiveFunction() {},
                new SimpleBounds(-10.0, 10.0), new AveragedStartingPointInitializer(-1.0, 1.0), new PowellDirectionSet(),
                new SimpleConvergenceChecker(1.0e-6, new NoConvergence()), GoalType.MINIMIZE, GoalTypeCount.MINIMIZE, new MaxEval(5));
        SimpleConvergenceChecker convergenceChecker = (SimpleConvergenceChecker) optimizer.getConvergenceChecker();
        assertEquals(1.0e-6, convergenceChecker.getTolerance(), 1E-7);
        assertFalse(convergenceChecker.isConverged());

        optimizer.optimize();
        assertTrue(convergenceChecker.isConverged());
    }
}
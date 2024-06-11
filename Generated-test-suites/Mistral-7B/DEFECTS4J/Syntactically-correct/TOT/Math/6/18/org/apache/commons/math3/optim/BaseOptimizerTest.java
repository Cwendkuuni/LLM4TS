package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleConstraintsMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.ZeroFinder;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

/**
 * Test class for {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private BaseOptimizer<PAIR> optimizer;
    private ConvergenceChecker<PAIR> convergenceChecker;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        optimizer = new SimpleBoundMinimizer<>(new SimpleObjectiveFunction(0),
                new SimpleBounds(0, 10), GoalType.MINIMIZE);
        convergenceChecker = new SimpleConstraintsMinimizer.DefaultConvergenceChecker();
    }

    /**
     * Tests {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    /**
     * Tests {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        ZeroFinder zeroFinder = new FibonacciMinimizer(
                new SimpleObjectiveFunction(0), new SimpleBounds(0, 1),
                new MaxEval(10), new StoppingCondition(1.0e-12));
        optimizer = (BaseOptimizer<PAIR>) zeroFinder;
        optimizer.optimize();
        assertEquals(10, optimizer.getEvaluations());
    }

    /**
     * Tests {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        assertNull(optimizer.getConvergenceChecker());
        optimizer.setConvergenceChecker(convergenceChecker);
        assertSame(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Tests the setting of the convergence checker.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetConvergenceCheckerUnsupported() {
        optimizer.setConvergenceChecker(new Object());
    }

    /**
     * Tests {@link BaseOptimizer#optimize()} with no constraints.
     */
    @Test
    public void testOptimizeNoConstraints() {
        ObjectiveFunction objectiveFunction = new SimpleObjectiveFunction(1);
        optimizer = new SimpleBoundMinimizer<>(objectiveFunction,
                new SimpleBounds(0, 10), GoalType.MINIMIZE);
        optimizer.optimize();
        PAIR result = optimizer.getOptimum();
        assertTrue(result != null);
        assertEquals(0.0, result.getValue(), 1.0e-9);
        assertFalse(optimizer.isDiverging());
    }

    /**
     * Tests {@link BaseOptimizer#optimize()} with constraints.
     */
    @Test
    public void testOptimizeWithConstraints() {
        ObjectiveFunction objectiveFunction = new SimpleObjectiveFunction(1);
        SimpleConstraints constraints = new SimpleConstraints(SimpleConstraints.Type.EQ, new double[][]{
                {2.0, 0.0}
        });
        optimizer = new SimpleConstraintsMinimizer<>(objectiveFunction,
                new SimpleBounds(0, 10), GoalType.MINIMIZE, constraints);
        optimizer.optimize();
        PAIR result = optimizer.getOptimum();
        assertNull(result);
        assertFalse(optimizer.isDiverging());
    }

    /**
     * Tests {@link BaseOptimizer#optimize()} with an error in the objective function.
     */
    @Test(expected = RuntimeException.class)
    public void testOptimizeError() {
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException("Error in objective function");
            }

            @Override
            public PointValuePair gradient(double x) {
                return new SimplePointValuePair(Double.NaN, Double.NaN);
            }
        };
        optimizer = new SimpleBoundMinimizer<>(objectiveFunction,
                new SimpleBounds(0, 10), GoalType.MINIMIZE);
        optimizer.optimize();
    }
}
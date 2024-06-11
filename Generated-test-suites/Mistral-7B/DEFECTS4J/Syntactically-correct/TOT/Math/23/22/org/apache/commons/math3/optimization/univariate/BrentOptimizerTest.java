package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximizer;
import org.apache.commons.math3.optim.initializer.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.function.Add;
import org.apache.commons.math3.optim.function.Max;
import org.apache.commons.math3.optim.function.SimpleFunction;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class BrentOptimizerTest {

    /**
     * Test of doOptimize method, with minimum goal.
     */
    @Test
    public void testMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        SimpleFunction function = new Add(new SimpleFunction("f") {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(x);
            }
        }, new SimpleFunction("g") {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        });
        GoalTypeMinimizer goal = new GoalTypeMinimizer(GoalType.RELATIVE, 1e-6);

        PointValuePair result = optimizer.optimize(function, goal, new ArrayRealVectorInitializer(-10, 10));

        assertNotNull(result);
        assertEquals(-1.2141615465218839, result.getPoint(), 1e-6);
        assertEquals(1.2222435645382899E-6, result.getValue(), 1e-6);
    }

    /**
     * Test of doOptimize method, with maximum goal.
     */
    @Test
    public void testMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        SimpleFunction function = new Add(new SimpleFunction("f") {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(x);
            }
        }, new SimpleFunction("g") {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        });
        GoalTypeMaximizer goal = new GoalTypeMaximizer(GoalType.RELATIVE, 1e-6);

        PointValuePair result = optimizer.optimize(function, goal, new ArrayRealVectorInitializer(-10, 10));

        assertNotNull(result);
        assertEquals(1.2141615465218839, result.getPoint(), 1e-6);
        assertEquals(1.2222435645382899E-6, result.getValue(), 1e-6);
    }

    /**
     * Test of doOptimize method, with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, -1e-5);
    }

    /**
     * Test of doOptimize method, with convergence checker.
     */
    @Test
    public void testConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new MyConvergenceChecker());
        SimpleFunction function = new Max(new SimpleFunction("f") {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(x);
            }
        }, new SimpleFunction("g") {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        });
        GoalTypeMinimizer goal = new GoalTypeMinimizer(GoalType.RELATIVE, 1e-6);

        PointValuePair result = optimizer.optimize(function, goal, new ArrayRealVectorInitializer(-10, 10));

        assertNotNull(result);
        assertEquals(-1.2141615465218839, result.getPoint(), 1e-6);
        assertEquals(1.2222435645382899E-6, result.getValue(), 1e-6);

        // Convergence checker should not have been called.
        assertFalse(myConvergenceCheckerCalled);
    }

    private boolean myConvergenceCheckerCalled = false;

    /**
     * Convergence checker for testing.
     */
    private class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            myConvergenceCheckerCalled = true;
            return false;
        }
    }
}
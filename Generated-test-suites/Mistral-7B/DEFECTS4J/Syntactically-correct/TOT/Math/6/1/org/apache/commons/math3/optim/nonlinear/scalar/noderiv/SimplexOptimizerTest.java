package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Test simplex optimization with a function that has a minimum.
     */
    @Test
    public void testMinimizeSimpleFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1] + 2 * x[0] + 2 * x[1] - 4;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        double[] startPoint = new double[] {1.0, 1.0};
        OptimizationData data = new OptimizationData() {
            public AbstractSimplex getSimplex() {
                Simplex4D simplex = new Simplex4D();
                simplex.setComparisonPoint(startPoint);
                simplex.setVerbose(false);
                return simplex;
            }
        };
        PointValuePair solution = optimizer.optimize(1000, f, goalType, data);

        assertEquals(new PointValuePair(new double[] {-1.0, -1.0}, -3.0), solution);
    }

    /**
     * Test simplex optimization with a function that has a maximum.
     */
    @Test
    public void testMaximizeSimpleFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return -(x[0] * x[0] + x[1] * x[1] + 2 * x[0] + 2 * x[1] - 4);
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        double[] startPoint = new double[] {1.0, 1.0};
        OptimizationData data = new OptimizationData() {
            public AbstractSimplex getSimplex() {
                Simplex4D simplex = new Simplex4D();
                simplex.setComparisonPoint(startPoint);
                simplex.setVerbose(false);
                return simplex;
            }
        };
        PointValuePair solution = optimizer.optimize(1000, f, goalType, data);

        assertEquals(new PointValuePair(new double[] {1.0, 1.0}, 3.0), solution);
    }

    /**
     * Test simplex optimization with a function that has no minimum or maximum.
     */
    @Test
    public void testFindSaddlePoint() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] - x[1] * x[1];
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        double[] startPoint = new double[] {1.0, 0.0};
        OptimizationData data = new OptimizationData() {
            public AbstractSimplex getSimplex() {
                Simplex4D simplex = new Simplex4D();
                simplex.setComparisonPoint(startPoint);
                simplex.setVerbose(false);
                return simplex;
            }
        };
        PointValuePair solution = optimizer.optimize(1000, f, goalType, data);

        // The saddle point (0, 0) is not found because the simplex method
        // is not designed to find minima and maxima in the same point.
        assertNotNull(solution);
        assertNotEquals(new PointValuePair(new double[] {0.0, 0.0}, 0.0), solution);
    }

    /**
     * Test simplex optimization with a null function.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        MultivariateFunction f = null;
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        double[] startPoint = new double[] {1.0, 1.0};
        OptimizationData data = new OptimizationData() {
            public AbstractSimplex getSimplex() {
                Simplex4D simplex = new Simplex4D();
                simplex.setComparisonPoint(startPoint);
                simplex.setVerbose(false);
                return simplex;
            }
        };
        optimizer.optimize(1000, f, goalType, data);
    }

    /**
     * Test simplex optimization with a null optimization data.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullOptimizationData() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        optimizer.optimize(1000, f, goalType, null);
    }
}
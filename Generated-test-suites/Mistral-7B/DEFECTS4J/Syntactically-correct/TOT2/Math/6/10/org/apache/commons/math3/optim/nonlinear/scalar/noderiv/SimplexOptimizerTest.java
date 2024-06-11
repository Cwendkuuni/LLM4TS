package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /** Simplex optimizer. */
    private SimplexOptimizer optimizer;

    /** Simplex. */
    private AbstractSimplex simplex;

    /** Objective function. */
    private MultivariateFunction objectiveFunction;

    /** Convergence checker. */
    private SimpleValueChecker convergenceChecker;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        simplex = new SimpleSimplex();
        objectiveFunction = new PolynomialFunction(new double[][] { {-1, -1}, {-1, 1}, {1, -1}, {1, 1} },
                                                 new double[] { 4.0, -3.0 });
    }

    /**
     * Tests the basic functionality of the optimizer.
     */
    @Test
    public void testOptimize() {
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(100, objectiveFunction, GoalType.MINIMIZE, new double[] { 1.0, 1.0 });
        assertEquals(new PointValuePair(new double[] { -0.66666667, -0.66666667 }, 0.4), result);
    }

    /**
     * Tests the optimization process with a noisy objective function.
     */
    @Test
    public void testOptimizeWithNoisyFunction() {
        MultivariateFunction noisyObjectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                double value = objectiveFunction.value(point);
                return value + FunctionUtils.randomValue(Math.abs(value) / 10.0);
            }
        };
        optimizer.setSimplex(simplexCubic);
        PointValuePair result = optimizer.optimize(100, noisyObjectiveFunction, GoalType.MINIMIZE, new double[] { 1.0, 1.0 });
        double expected = Math.min(Math.min(new double[] { -1.0, -1.0, 1.0, 1.0 }), 0.0) - 1e-5;
        assertEquals(new PointValuePair(new double[] { expected, expected }, Math.abs(expected) * 1.1), result);
    }

    /**
     * Tests the optimization process with a function that is not differentiable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeWithUndifferentiablesFunction() {
        optimizer.setSimplex(simplexCubic);
        optimizer.optimize(100, FunctionUtils.sqrt, GoalType.MINIMIZE, new double[] { 1.0, 1.0 });
    }

    /**
     * Tests the optimization process with a function that has no optimum.
     */
    @Test
    public void testOptimizeWithFunctionNoOptimum() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.sin(Math.PI) + Math.sqrt(Math.pow(point[0], 2) + Math.pow(point[1], 2));
            }
        };
        optimizer.setSimplex(simplexCubic);
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, new double[] { 1.0, 1.0 });
        assertEquals(Double.NaN, result.getValue(), 1e-5);
    }
}

class SimpleSimplex extends Simplex {
    @Override
    public void build(double[] startPoint) {
        super.build(new double[][] { startPoint }, 1);
    }
}

class SimpleSimplexCubic extends SimpleSimplex {
    @Override
    public void build(double[] startPoint) {
        super.build(new double[][] { startPoint, new double[] { startPoint[0] + 1, startPoint[1] + 1 },
                                     new double[] { startPoint[0] - 1, startPoint[1] - 1 },
                                     new double[] { startPoint[0] + 1, startPoint[1] - 1 },
                                     new double[] { startPoint[0] - 1, startPoint[1] + 1 } }, 3);
    }
}
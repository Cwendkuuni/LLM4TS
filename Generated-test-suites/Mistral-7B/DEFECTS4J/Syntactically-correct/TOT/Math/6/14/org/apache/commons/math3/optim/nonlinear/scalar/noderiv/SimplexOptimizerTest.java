package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Test optimization of a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + 2 * x[1] * x[1] + 2 * x[0] * x[1];
            }
        };
        AbstractSimplex simplex = new SimpleXSimplex(new SimpleValueChecker(1e-9, 1e-9));
        SimplexOptimizer optimizer = new SimplexOptimizer(simplex);
        PointValuePair result = optimizer.optimize(1000, f, GoalType.MINIMIZE, null);
        assertEquals(-1.5, result.getPoint()[0], 1e-9);
        assertEquals(-2.25, result.getValue(), 1e-9);
    }

    /**
     * Test optimization of a discontinuous function.
     */
    @Test
    public void testDiscontinuousFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] >= 1) {
                    return Double.POSITIVE_INFINITY;
                }
                return x[0] * x[0];
            }
        };
        AbstractSimplex simplex = new SimpleXSimplex(new SimpleValueChecker(1e-9, 1e-9));
        SimplexOptimizer optimizer = new SimplexOptimizer(simplex);
        PointValuePair result = optimizer.optimize(1000, f, GoalType.MINIMIZE, null);
        assertNotNull(result);
        assertEquals(-1, result.getPoint()[0], 1e-9);
    }

    /**
     * Test optimization with a null objective function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        AbstractSimplex simplex = new SimpleXSimplex(new SimpleValueChecker(1e-9, 1e-9));
        SimplexOptimizer optimizer = new SimplexOptimizer(simplex);
        optimizer.optimize(1000, null, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization with a null optimization data.
     */
    @Test(expected = NullPointerException.class)
    public void testNullOptimizationData() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + 2 * x[1] * x[1];
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1000, f, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization with a null simplex.
     */
    @Test(expected = NullPointerException.class)
    public void testNullSimplex() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + 2 * x[1] * x[1];
            }
        };
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(null);
        optimizer.optimize(1000, f, GoalType.MINIMIZE, null);
    }
}
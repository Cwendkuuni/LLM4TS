package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Simplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleXScaledShermanNewtonSimplex;
import org.junit.Test;

/**
 * Tests for {@link SimplexOptimizer}.
 *
 */
public class SimplexOptimizerTest {
    /**
     * Function for testing optimization.
     */
    private static final ObjectiveFunction TEST_FUNCTION = new ObjectiveFunction() {
        public double value(double[] x) {
            return Math.pow(Math.sin(Math.PI * x[0] + 1.0), 2.0)
                + Math.pow(Math.sin(Math.PI * x[1] + 1.0), 2.0);
        }

        public double[] gradient(double[] x) {
            double[] result = new double[2];
            result[0] = -2 * Math.cos(Math.PI * x[0] + 1.0) * Math.sin(Math.PI * x[0] + 1.0)
                - 2 * Math.sin(Math.PI * x[1] + 1.0) * Math.cos(Math.PI * x[1] + 1.0);
            result[1] = -2 * Math.cos(Math.PI * x[1] + 1.0) * Math.sin(Math.PI * x[1] + 1.0)
                - 2 * Math.sin(Math.PI * x[0] + 1.0) * Math.cos(Math.PI * x[0] + 1.0);
            return result;
        }
    };

    /**
     * Test optimization with a simplex that does not implement {@link AbstractSimplex}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonAbstractSimplex() {
        new SimplexOptimizer(new SimpleXScaledShermanNewtonSimplex(2) {});
    }

    /**
     * Test optimization with no provided {@link AbstractSimplex}.
     */
    @Test(expected = NullArgumentException.class)
    public void testNoSimplex() {
        new SimplexOptimizer(new SimpleValueChecker());
    }

    /**
     * Test optimization with a provided {@link AbstractSimplex} of incorrect size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectSimplexSize() {
        AbstractSimplex simplex = new Simplex(2) {
            @Override
            public int getSize() {
                return 3;
            }
        };
        new SimplexOptimizer(simplex);
    }

    /**
     * Test optimization with a provided initial point.
     */
    @Test
    public void testOptimizationWithInitialPoint() {
        AbstractSimplex simplex = new Simplex(2);
        double[] startPoint = new double[] { 0.5, 0.5 };
        simplex.build(startPoint);

        SimplexOptimizer optimizer = new SimplexOptimizer(simplex, new SimpleValueChecker());
        PointValuePair result = optimizer.optimize(10, TEST_FUNCTION, GoalType.MINIMIZE, startPoint);
        assertArrayEquals(new double[] { -1.0, -1.0 }, result.getPoint(), 0.001);
        assertEquals(-2 * Math.PI, result.getValue(), 0.001);

        optimizer.optimize(10, TEST_FUNCTION, GoalType.MAXIMIZE, startPoint);
        result = optimizer.getLastIterationResult();
        assertArrayEquals(startPoint, result.getPoint(), 0.001);
        assertEquals(2 * Math.PI * Math.PI, result.getValue(), 0.001);
    }

    /**
     * Test optimization with a provided function.
     */
    @Test
    public void testOptimizationWithFunction() {
        AbstractSimplex simplex = new Simplex(2);
        double[] startPoint = new double[] { 0.5, 0.5 };
        simplex.build(startPoint);

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(10, TEST_FUNCTION, GoalType.MINIMIZE, startPoint);
        assertArrayEquals(new double[] { -1.0, -1.0 }, result.getPoint(), 0.001);
        assertEquals(-2 * Math.PI, result.getValue(), 0.001);

        optimizer.optimize(10, TEST_FUNCTION, GoalType.MAXIMIZE, startPoint);
        result = optimizer.getLastIterationResult();
        assertArrayEquals(startPoint, result.getPoint(), 0.001);
        assertEquals(2 * Math.PI * Math.PI, result.getValue(), 0.001);
    }

    /**
     * Test optimization with a provided goal type.
     */
    @Test
    public void testOptimizationWithGoalType() {
        AbstractSimplex simplex = new Simplex(2);
        double[] startPoint = new double[] { 0.5, 0.5 };
        simplex.build(startPoint);

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(10, TEST_FUNCTION, GoalType.MINIMIZE, startPoint);
        assertArrayEquals(new double[] { -1.0, -1.0 }, result.getPoint(), 0.001);
        assertEquals(-2 * Math.PI, result.getValue(), 0.001);

        optimizer.optimize(10, TEST_FUNCTION, GoalType.MAXIMIZE, startPoint);
        result = optimizer.getLastIterationResult();
        assertArrayEquals(startPoint, result.getPoint(), 0.001);
        assertEquals(2 * Math.PI * Math.PI, result.getValue(), 0.001);
    }

    /**
     * Test optimization with a function having unbounded domain.
     */
    @Test(expected = UnboundedDomainException.class)
    public void testUnboundedFunction() {
        AbstractSimplex simplex = new Simplex(2);
        double[] startPoint = new double[] { 0.5, 0.5 };
        simplex.build(startPoint);

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        MultivariateFunction unboundedFunction = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] > 10.0) {
                    throw new UnboundedDomainException();
                }
                return Math.pow(x[0], 2.0);
            }
        };

        optimizer.optimize(10, unboundedFunction, GoalType.MINIMIZE, startPoint);
    }
}
package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.NullVectorException;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.point.PointValuePairComparator;
import org.apache.commons.math3.optimization.simpledirect.GoalTypeThreshold;
import org.apache.commons.math3.optimization.simpledirect.SimpleSimplex;
import org.apache.commons.math3.optimization.simpledirect.SimplexIterationException;
import org.apache.commons.math3.optimization.simpledirect.SimplexOptimizationData;
import org.apache.commons.math3.optimization.simpledirect.SimplexOptimizer;
import org.junit.Test;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Tests the optimization of a simple polynomial function.
     */
    @Test
    public void testSimplePolynomial() {
        final MultivariateFunction f = new PolynomialFunction(new double[][]{{1.0, 1.0}, {-1.0, 1.0}, {1.0, -1.0}}, 2);

        // Initial simplex.
        AbstractSimplex simplex = new SimpleSimplex(3, 1.0E-5, 1.0E-5);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        double[] startPoint = new double[] {0.1, 0.1};
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, startPoint);

        // Check result.
        assertEquals(1.0, result.getValue(), 1.0E-6);
        assertArrayEquals(new double[] {-1.0, -1.0}, result.getPoint(), 1.0E-6);

        // Test exceptions when parameters are missing.
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(null, f, GoalType.MINIMIZE, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), null, GoalType.MINIMIZE, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), f, null, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, null));
    }

    /**
     * Tests the optimization of a simple polynomial function with a custom
     * convergence checker.
     */
    @Test
    public void testSimplePolynomialWithCustomChecker() {
        final MultivariateFunction f = new PolynomialFunction(new double[][]{{1.0, 1.0}, {-1.0, 1.0}, {1.0, -1.0}}, 2);

        // Initial simplex.
        AbstractSimplex simplex = new SimpleSimplex(3, 1.0E-5, 1.0E-5);
        SimplexOptimizer optimizer = new SimplexOptimizer(new GoalTypeThreshold(1.0E-5));
        double[] startPoint = new double[] {0.1, 0.1};
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, startPoint);

        // Check result.
        assertEquals(1.0, result.getValue(), 1.0E-6);
        assertArrayEquals(new double[] {-1.0, -1.0}, result.getPoint(), 1.0E-6);

        // Test exceptions when parameters are missing.
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(null, f, GoalType.MINIMIZE, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), null, GoalType.MINIMIZE, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), f, null, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, null));

        // Test optimization with null initial simplex.
        optimizer = new SimplexOptimizer(new GoalTypeThreshold(1.0E-5));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, startPoint));
    }

    /**
     * Tests the optimization of a simple polynomial function with simple bounds.
     */
    @Test
    public void testSimplePolynomialWithSimpleBounds() {
        final MultivariateFunction f = new PolynomialFunction(new double[][]{{1.0, 1.0}, {-1.0, 1.0}, {1.0, -1.0}}, 2);

        // Initial simplex.
        double[][] bounds = new double[][] {{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}, {0.0, 1.0}, {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}};
        AbstractSimplex simplex = new SimpleSimplex(3, bounds, 1.0E-5, 1.0E-5, new PointValuePairComparator());
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        double[] startPoint = new double[] {0.1, 0.1};
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, startPoint);

        // Check result.
        assertEquals(1.0, result.getValue(), 1.0E-6);
        assertArrayEquals(new double[] {-1.0, -1.0}, result.getPoint(), 1.0E-6);

        // Test exceptions when parameters are missing.
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(null, f, GoalType.MINIMIZE, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), null, GoalType.MINIMIZE, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), f, null, startPoint));
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, null));

        // Test optimization with null initial simplex.
        optimizer = new SimplexOptimizer(new SimpleValueChecker());
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, startPoint));
    }

    /**
     * Tests the optimization of a function that never converges.
     */
    @Test
    public void testNeverConverges() {
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.random();
            }
        };

        // Initial simplex.
        AbstractSimplex simplex = new SimpleSimplex(3, 1.0E-5, 1.0E-5);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        double[] startPoint = new double[] {0.1, 0.1};
        optimizer.setSimplex(simplex);
        assertThrows(SimplexIterationException.class, () -> optimizer.optimize(new MaxEval(1), f, GoalType.MINIMIZE, startPoint));
    }

    /**
     * Tests the optimization of a function that fails to evaluate.
     */
    @Test
    public void testFailedToEvaluate() {
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                throw new NullVectorException();
            }
        };

        // Initial simplex.
        AbstractSimplex simplex = new SimpleSimplex(3, 1.0E-5, 1.0E-5);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        double[] startPoint = new double[] {0.1, 0.1};
        optimizer.setSimplex(simplex);
        assertThrows(RuntimeException.class, () -> optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, startPoint));
    }

    /**
     * Tests the optimization of a function that returns a NaN value.
     */
    @Test
    public void testNaN() {
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return Double.NaN;
            }
        };

        // Initial simplex.
        AbstractSimplex simplex = new SimpleSimplex(3, 1.0E-5, 1.0E-5);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        double[] startPoint = new double[] {0.1, 0.1};
        optimizer.setSimplex(simplex);
        assertThrows(ArithmeticException.class, () -> optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE, startPoint));
    }
}
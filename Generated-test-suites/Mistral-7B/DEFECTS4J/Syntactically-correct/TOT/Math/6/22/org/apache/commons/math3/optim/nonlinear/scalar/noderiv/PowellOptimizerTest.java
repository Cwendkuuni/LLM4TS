package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.apache.commons.math3.test.AbstractTestCase;
import org.junit.Test;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest extends AbstractTestCase {

    /**
     * Test the optimization of a simple function with a single local minimum.
     *
     * @throws NotStrictlyPositiveException if the initial step size is not
     * positive.
     */
    @Test
    public void testSimpleFunction() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(f, MINIMIZE, 0.0, 3.0);
        assertEquals(2.0, result.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a simple function with a single local maximum.
     *
     * @throws NotStrictlyPositiveException if the initial step size is not
     * positive.
     */
    @Test
    public void testSimpleFunctionMaximum() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return -Math.pow(x - 2.0, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(f, MAXIMIZE, 0.0, 3.0);
        assertEquals(2.0, result.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a simple function with multiple local minimums.
     *
     * @throws NotStrictlyPositiveException if the initial step size is not
     * positive.
     */
    @Test
    public void testSimpleFunctionMultipleMinima() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return (Math.pow(x - 1.0, 2.0) - Math.pow(x - 2.0, 2.0));
            }
        };
        PointValuePair result = optimizer.optimize(f, MINIMIZE, 0.0, 4.0);
        assertEquals(1.0, result.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a function with a local minimum at an edge
     * of the search interval.
     *
     * @throws NotStrictlyPositiveException if the initial step size is not
     * positive.
     */
    @Test
    public void testSimpleFunctionEdge() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 0.5, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(f, MINIMIZE, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a function with a local minimum at the start
     * of the search interval.
     *
     * @throws NotStrictlyPositiveException if the initial step size is not
     * positive.
     */
    @Test
    public void testSimpleFunctionStart() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(f, MINIMIZE, 0.0, 1.0);
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a function with no local minimum in the
     * search interval.
     *
     * @throws NotStrictlyPositiveException if the initial step size is not
     * positive.
     */
    @Test(expected = org.apache.commons.math3.optim.MaxEvalExceededException.class)
    public void testSimpleFunctionNoMinima() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        optimizer.optimize(f, MINIMIZE, 0.0, 10.0);
    }

    /**
     * Test the optimization of a function with a local maximum at the start
     * of the search interval.
     *
     * @throws NotStrictlyPositiveException if the initial step size is not
     * positive.
     */
    @Test
    public void testSimpleFunctionMaximumStart() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return -Math.pow(x, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(f, MAXIMIZE, 0.0, 1.0);
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

    /**
     * Test the optimization of a function with a local maximum at an edge
     * of the search interval.
     *
     * @throws NotStrictlyPositiveException if the initial step size is not
     * positive.
     */
    @Test
    public void testSimpleFunctionMaximumEdge() throws NotStrictlyPositiveException {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return -Math.pow(x - 2.0, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(f, MAXIMIZE, 0.0, 3.0);
        assertEquals(2.0, result.getPoint(), 1e-6);
    }
}
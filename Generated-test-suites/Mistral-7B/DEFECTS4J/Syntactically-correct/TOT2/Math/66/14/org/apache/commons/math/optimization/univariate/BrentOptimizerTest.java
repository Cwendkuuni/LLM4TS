package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealVectorOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;

/**
 * Tests for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Tests the optimization of a simple function.
     */
    @Test
    public void testSimpleOptimization() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { -1, 2, 1 });
        RealVectorOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);
        assertEquals(-1.61525, result.getPoint(), 1e-8);
        assertEquals(3.60247E-11, result.getValue(), 1e-13);
    }

    /**
     * Tests the optimization of a function with multiple local minima.
     */
    @Test
    public void testMultipleLocalMinima() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { -1, 2, -1, 1 });
        RealVectorOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);
        assertEquals(-2.17041, result.getPoint(), 1e-8);
        assertEquals(1.31173E-9, result.getValue(), 1e-13);
    }

    /**
     * Tests the optimization of a function with a singularity.
     */
    @Test
    public void testSingularity() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { 0, 1, 0 });
        RealVectorOptimizer optimizer = new BrentOptimizer();

        assertThrows(ZeroException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, -1, 1, 0));
    }

    /**
     * Tests the optimization of a function with upper bound.
     */
    @Test
    public void testUpperBound() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { -1, 1, 1 });
        RealVectorOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 0, 0);
        assertEquals(-0.5, result.getPoint(), 1e-8);
        assertEquals(0, result.getValue(), 1e-13);
    }

    /**
     * Tests the optimization of a function with lower bound.
     */
    @Test
    public void testLowerBound() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { 1, 1, 1 });
        RealVectorOptimizer optimizer = new BrentOptimizer();

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 0);
        assertEquals(1, result.getPoint(), 1e-8);
        assertEquals(1, result.getValue(), 1e-13);
    }

    /**
     * Tests the optimization of a function with initial value outside the bounds.
     */
    @Test
    public void testInitialValueOutsideBounds() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { -1, 1, 1 });
        RealVectorOptimizer optimizer = new BrentOptimizer();

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 1, 0, 0));
    }

    /**
     * Tests the optimization with non-strictly positive relative accuracy.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonPositiveRelativeAccuracy() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { -1, 1, 1 });
        RealVectorOptimizer optimizer = new BrentOptimizer();

        optimizer.setRelativeAccuracy(-1e-8);
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 1, 0);
    }

    /**
     * Tests the optimization with non-positive absolute accuracy.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonPositiveAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { -1, 1, 1 });
        RealVectorOptimizer optimizer = new BrentOptimizer();

        optimizer.setAbsoluteAccuracy(-1e-6);
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 1, 0);
    }

    /**
     * Tests the optimization with a function that throws an exception on evaluation.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        RealVectorOptimizer optimizer = new BrentOptimizer();

        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 0);
    }

    /**
     * Tests the optimization with a function that throws a maximum iterations exceeded exception.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        UnivariateFunction f = new PolynomialFunction(1, new double[] { -1, 1, 1 }) {
            @Override
            public double value(double x) {
                throw new MaxIterationsExceededException("Test exception");
            }
        };
        RealVectorOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);

        optimizer.optimize(f, GoalType.MINIMIZE, 0, 1, 0);
    }
}
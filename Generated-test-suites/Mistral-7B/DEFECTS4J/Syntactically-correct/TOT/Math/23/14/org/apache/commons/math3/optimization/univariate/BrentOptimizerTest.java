package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-5, 1e-5);
    }

    /**
     * Tests minimization of a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        UnivariateFunction function = new QuadraticFunction(-1.0, 4.0);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(-2.0, result.getPoint(), 1e-6);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    /**
     * Tests minimization of a function with a global minimum at the boundary.
     */
    @Test
    public void testFunctionWithMinimumAtBoundary() {
        UnivariateFunction function = new FunctionWithMinimumAtBoundary();
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Tests minimization of a function with a local minimum at the boundary.
     */
    @Test
    public void testFunctionWithLocalMinimumAtBoundary() {
        UnivariateFunction function = new FunctionWithLocalMinimumAtBoundary();
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Tests minimization of a function with a global minimum at the middle.
     */
    @Test
    public void testFunctionWithMinimumAtMiddle() {
        UnivariateFunction function = new FunctionWithMinimumAtMiddle();
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(0.1, result.getValue(), 1e-6);
    }

    /**
     * Tests minimization of a function with a local minimum at the middle.
     */
    @Test
    public void testFunctionWithLocalMinimumAtMiddle() {
        UnivariateFunction function = new FunctionWithLocalMinimumAtMiddle();
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(0.2, result.getValue(), 1e-6);
    }

    /**
     * Tests minimization of a function with relative tolerance greater than machine epsilon.
     */
    @Test
    public void testFunctionWithLargeRelativeTolerance() {
        try {
            optimizer = new BrentOptimizer(0.1, 0.0);
            UnivariateFunction function = new QuadraticFunction(-1.0, 4.0);
            optimizer.optimize(function, GoalType.MINIMIZE);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Tests minimization of a function with absolute tolerance less than zero.
     */
    @Test
    public void testFunctionWithNegativeAbsoluteTolerance() {
        try {
            optimizer = new BrentOptimizer(1e-5, -1e-5);
            UnivariateFunction function = new QuadraticFunction(-1.0, 4.0);
            optimizer.optimize(function, GoalType.MINIMIZE);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}

/**
 * A quadratic function for testing.
 */
class QuadraticFunction implements UnivariateFunction {
    private final double a;
    private final double b;

    public QuadraticFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double value(double x) {
        return a * x * x + b * x;
    }
}

/**
 * A function for testing minimum at the boundary.
 */
class FunctionWithMinimumAtBoundary implements UnivariateFunction {
    @Override
    public double value(double x) {
        if (x > 0) {
            return x * (FastMath.log(x) - 1);
        } else {
            return -x * (FastMath.log(-x) + 1);
        }
    }
}

/**
 * A function for testing local minimum at the boundary.
 */
class FunctionWithLocalMinimumAtBoundary implements UnivariateFunction {
    @Override
    public double value(double x) {
        return x * x * (x < 0 ? -1 : 1) + 0.1 * Math.sin(Math.PI * x);
    }
}

/**
 * A function for testing minimum at the middle.
 */
class FunctionWithMinimumAtMiddle implements UnivariateFunction {
    @Override
    public double value(double x) {
        return 100 * x * (x - 1) * (x - 2);
    }
}

/**
 * A function for testing local minimum at the middle.
 */
class FunctionWithLocalMinimumAtMiddle implements UnivariateFunction {
    @Override
    public double value(double x) {
        return 100 * x * (x - 2) * (x - 3) + 0.1 * Math.sin(Math.PI * x);
    }
}
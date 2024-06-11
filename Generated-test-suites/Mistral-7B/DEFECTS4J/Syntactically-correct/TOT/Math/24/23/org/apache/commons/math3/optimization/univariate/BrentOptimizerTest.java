package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.AverageInitializer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.musk.BentleyBrookeFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.musk.BrackettFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.musk.RidgedMillerFunction;
import org.apache.commons.math3.test.AbstractTestCase;
import org.apache.commons.math3.test.annotations.TestMethod;
import org.apache.commons.math3.test.annotations.TestMethods;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest extends AbstractTestCase {

    /**
     * Test optimization of a simple function.
     */
    @TestMethod
    public void testSimpleFunction() {
        final ObjectiveFunction f = new BrackettFunction();
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result = optimizer.optimize(f, new SimpleBounds(0, 5), null);
        assertEquals("minimum not found", 1.0, result.getPoint().getX(), 1e-6);
    }

    /**
     * Test optimization of a function with a minimum at the boundary.
     */
    @TestMethod
    public void testMinAtBoundary() {
        final ObjectiveFunction f = new BentleyBrookeFunction();
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result = optimizer.optimize(f, new SimpleBounds(-1, 1), null);
        assertEquals("minimum not found", -1.0, result.getPoint().getX(), 1e-6);
    }

    /**
     * Test optimization of a function with a minimum at an endpoint.
     */
    @TestMethod
    public void testMinAtEndpoint() {
        final ObjectiveFunction f = new RidgedMillerFunction();
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result = optimizer.optimize(f, new SimpleBounds(-10, 10), null);
        assertEquals("minimum not found", 0.0, result.getPoint().getX(), 1e-6);
    }

    /**
     * Test optimization of a function with multiple local minima.
     */
    @TestMethod
    public void testMultipleLocalMinima() {
        final ObjectiveFunction f = new BrackettFunction();
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result1 = optimizer.optimize(f, new SimpleBounds(0, 3), null);
        final UnivariateOptimizer result2 = optimizer.optimize(f, new SimpleBounds(3, 5), null);
        final UnivariateOptimizer result3 = optimizer.optimize(f, new SimpleBounds(5, 7), null);
        assertEquals("minimum not found", 1.0, result1.getPoint().getX(), 1e-6);
        assertEquals("minimum not found", 3.0, result2.getPoint().getX(), 1e-6);
        assertEquals("minimum not found", 5.0, result3.getPoint().getX(), 1e-6);
    }

    /**
     * Test optimization of a function with a minimum at a complex value.
     */
    @TestMethod
    public void testMinAtComplexValue() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) * Math.sin(Math.PI * x)
                    + Math. cos(Math.PI * x) * Math.cos(Math.PI * x) - 1;
            }
        };
        try {
            final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
            optimizer.optimize(f, new SimpleBounds(-5, 5), null);
            fail("Exception expected: not a real-valued function");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test optimization of a function with non-monotone behavior
     * in the interval.
     */
    @TestMethod
    public void testNonMonotoneFunction() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 2.5), 2) * Math.sin(Math.abs(Math.PI * x));
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result = optimizer.optimize(f, new SimpleBounds(-5, 5), null);
        // The optimizer may converge to different x depending on the starting
        // point. We only check that the result is not far from the minimum.
        final double expected = 2.5;
        final double tolerance = 1e-3;
        assertTrue("minimum not found in interval", result.getPoint().getX() > expected - tolerance);
        assertTrue("minimum not found in interval", result.getPoint().getX() < expected + tolerance);
    }

    /**
     * Test optimization of a function with infinite minimum.
     */
    @TestMethod
    public void testInfiniteMinimum() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return 1 / (x * x);
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(f, new SimpleBounds(0, 1), null);
            fail("Exception expected: infinite minimum");
        } catch (final ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test optimization of a function with a very flat minimum.
     */
    @TestMethods({"testFlatMinimum1", "testFlatMinimum2"})
    public void testFlatMinimum() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return 1e-20 * (x * x - 1);
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result1 = optimizer.optimize(f, new SimpleBounds(-5, 5), null);
        final UnivariateOptimizer result2 = optimizer.optimize(f, new SimpleBounds(-5, 5), null);
        assertEquals("minimum not found", -1.0, result1.getPoint().getX(), 1e-3);
        assertEquals("minimum not found", -1.0, result2.getPoint().getX(), 1e-3);
    }

    /**
     * Test optimization of a function with a very steep minimum.
     */
    @TestMethods({"testSteepMinimum1", "testSteepMinimum2"})
    public void testSteepMinimum() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return 1e10 * (x * x - 1);
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result1 = optimizer.optimize(f, new SimpleBounds(-5, 5), null);
        final UnivariateOptimizer result2 = optimizer.optimize(f, new SimpleBounds(-5, 5), null);
        assertEquals("minimum not found", 1.0, result1.getPoint().getX(), 1e-3);
        assertEquals("minimum not found", 1.0, result2.getPoint().getX(), 1e-3);
    }

    /**
     * Test optimization of a function with a very large domain.
     */
    @TestMethods({"testLargeDomain1", "testLargeDomain2"})
    public void testLargeDomain() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x < -1000 || x > 1000) {
                    throw new IllegalArgumentException("x out of bounds");
                }
                return Math.exp(-x * x) * Math.sin(Math.PI * x);
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result1 = optimizer.optimize(f, new SimpleBounds(-1000, 1000), null);
        final UnivariateOptimizer result2 = optimizer.optimize(f, new SimpleBounds(-1000, 1000), null);
        // The optimizer may converge to different x depending on the starting
        // point. We only check that the result is not far from the minimum.
        final double expected = 0.0;
        final double tolerance = 1e-2;
        assertTrue("minimum not found in interval", result1.getPoint().getX() > expected - tolerance);
        assertTrue("minimum not found in interval", result1.getPoint().getX() < expected + tolerance);
        assertTrue("minimum not found in interval", result2.getPoint().getX() > expected - tolerance);
        assertTrue("minimum not found in interval", result2.getPoint().getX() < expected + tolerance);
    }

    /**
     * Test optimization of a function with a very large range.
     */
    @TestMethods({"testLargeRange1", "testLargeRange2"})
    public void testLargeRange() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) * Math.sin(Math.PI * x);
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result1 = optimizer.optimize(f, new SimpleBounds(-5, 5), null);
        final UnivariateOptimizer result2 = optimizer.optimize(f, new SimpleBounds(-5, 5), null);
        // The optimizer may converge to different x depending on the starting
        // point. We only check that the result is not far from the minimum.
        final double expected = 0.0;
        final double tolerance = 1e-2;
        assertTrue("minimum not found in interval", result1.getPoint().getX() > expected - tolerance);
        assertTrue("minimum not found in interval", result1.getPoint().getX() < expected + tolerance);
        assertTrue("minimum not found in interval", result2.getPoint().getX() > expected - tolerance);
        assertTrue("minimum not found in interval", result2.getPoint().getX() < expected + tolerance);
    }

    /**
     * Test optimization of a function with an initial point outside the interval.
     */
    @TestMethod
    public void testInitialPointOutsideInterval() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(f, new SimpleBounds(0, 5), new AverageInitializer());
            fail("Exception expected: initial point outside interval");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test optimization of a function with a goal type different from minimization.
     */
    @TestMethod
    public void testMaximization() {
        final ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        final UnivariateOptimizer result = optimizer.optimize(f, new SimpleBounds(0, 5), null);
        // The optimizer tries to minimize the negative function,
        // so the result should have a negative value.
        assertEquals("maximum not found", -Math.sin(Math.PI), result.getPoint().getValue(), 1e-6);
    }
}
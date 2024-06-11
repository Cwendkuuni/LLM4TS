package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.math.AckleyFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.math.RosenbrockFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.math.SineFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.math.SquaredErrorFunction;
import org.apache.commons.math3.test.EqualsTester;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} and {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)} with default {@link ConvergenceChecker}.
     */
    @Test
    public void testDefaultConstructors() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;

        BrentOptimizer optimizer1 = new BrentOptimizer(relTol, absTol);
        BrentOptimizer optimizer2 = new BrentOptimizer(relTol, absTol, null);

        assertEquals(optimizer1.getAbsoluteThreshold(), optimizer2.getAbsoluteThreshold(), 0.0);
        assertEquals(optimizer1.getRelativeThreshold(), optimizer2.getRelativeThreshold(), 0.0);
        assertNotNull(optimizer1.getConvergenceChecker());
        assertNull(optimizer2.getConvergenceChecker());
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)} with custom {@link ConvergenceChecker}.
     */
    @Test
    public void testCustomConstructor() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double customTol = 1e-7;

        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return FastMath.abs(current.getValue() - previous.getValue()) < customTol;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, customChecker);

        assertEquals(optimizer.getAbsoluteThreshold(), absTol, 0.0);
        assertEquals(optimizer.getRelativeThreshold(), relTol, 0.0);
        assertNotNull(optimizer.getConvergenceChecker());
        assertEquals(optimizer.getConvergenceChecker(), customChecker);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, ConvergenceChecker)} with default {@link ConvergenceChecker}.
     */
    @Test
    public void testDefaultOptimizeFunction() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double xMin = -1.5;
        final double xMax = 1.5;

        UnivariateFunction function = new SquaredErrorFunction(new SineFunction());
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);

        PointValuePair result = optimizer.optimize(function, xMin, xMax, null);

        assertNotNull(result);
        assertEquals(result.getValue(), 0, 1e-8);
        assertTrue(FastMath.isCloseTo(result.getPoint(), -1, 1e-8));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, ConvergenceChecker)} with custom {@link ConvergenceChecker}.
     */
    @Test
    public void testCustomOptimizeFunction() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double customTol = 1e-7;
        final double xMin = 0;
        final double xMax = 10;

        UnivariateFunction function = new RosenbrockFunction(1);
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return FastMath.abs(current.getValue() - previous.getValue()) < customTol;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, customChecker);

        PointValuePair result = optimizer.optimize(function, xMin, xMax, null);

        assertNotNull(result);
        assertEquals(result.getValue(), 0, 1e-8);
        assertTrue(FastMath.isCloseTo(result.getPoint(), 0, 1e-8));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double, ConvergenceChecker)} with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptimizeFunction() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double xMin = -1;
        final double xMax = 1;

        UnivariateFunction function = new AckleyFunction(-1, 1);
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);

        optimizer.optimize(function, xMin, xMax, null);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double)} with default {@link ConvergenceChecker}.
     */
    @Test
    public void testDefaultGoalType() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double xMin = -1.5;
        final double xMax = 1.5;

        UnivariateFunction function = new SquaredErrorFunction(new SineFunction());
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);

        PointValuePair result = optimizer.optimize(function, xMin, xMax);

        assertNotNull(result);
        assertEquals(result.getValue(), 0, 1e-8);
        assertTrue(FastMath.isCloseTo(result.getPoint(), -1, 1e-8));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double)} with {@link GoalType#MAXIMIZE}.
     */
    @Test
    public void testMaximizeGoalType() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double xMin = 0;
        final double xMax = 10;

        UnivariateFunction function = new SquaredErrorFunction(new SineFunction());
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, GoalType.MAXIMIZE);

        PointValuePair result = optimizer.optimize(function, xMin, xMax);

        assertNotNull(result);
        assertEquals(result.getValue(), 0, 1e-8);
        assertTrue(FastMath.isCloseTo(result.getPoint(), 1, 1e-8));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double, double)} with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double xMin = -1;
        final double xMax = 1;

        UnivariateFunction function = new SquaredErrorFunction(new SineFunction());
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);

        optimizer.optimize(function, xMin, xMax, GoalType.MIN_MAX);
    }

    /**
     * Test {@link BrentOptimizer#getMin()} and {@link BrentOptimizer#getMax()}.
     */
    @Test
    public void testMinMax() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double xMin = -1.5;
        final double xMax = 1.5;

        UnivariateFunction function = new SquaredErrorFunction(new SineFunction());
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);

        PointValuePair result = optimizer.optimize(function, xMin, xMax);

        assertEquals(optimizer.getMin(), xMin, 1e-8);
        assertEquals(optimizer.getMax(), xMax, 1e-8);
    }

    /**
     * Test equality.
     */
    @Test
    public void testEquals() {
        final double relTol = 1e-5;
        final double absTol = 1e-6;
        final double xMin = -1.5;
        final double xMax = 1.5;

        UnivariateFunction function = new SquaredErrorFunction(new SineFunction());
        BrentOptimizer optimizer1 = new BrentOptimizer(relTol, absTol);
        BrentOptimizer optimizer2 = new BrentOptimizer(relTol, absTol);

        new EqualsTester()
            .addEqualityGroup(optimizer1, optimizer2)
            .testEquals();

        optimizer1.setRelativeThreshold(1e-4);
        assertFalse(optimizer1.equals(optimizer2));

        optimizer1.setAbsoluteThreshold(1e-7);
        assertFalse(optimizer1.equals(optimizer2));

        optimizer1.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return FastMath.abs(current.getValue() - previous.getValue()) < 1e-9;
            }
        });
        assertFalse(optimizer1.equals(optimizer2));
    }
}
package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MinEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonUnivariateConstraint;
import org.apache.commons.math3.optim.PointValuePairChecker;
import org.apache.commons.math3.optim.PointValuePairSorter;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MinEval;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

/**
 * Test for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {
    /**
     * Test with a simple function.
     */
    @Test
    public void testSimpleFunction() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return a[0] * a[0] + a[1] * a[1];
            }
        };

        final PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, x);
        assertEquals(0.0, result.getPoint()[0], 1e-8);
        assertEquals(0.0, result.getPoint()[1], 1e-8);
        assertEquals(1.0, result.getValue(), 1e-8);
    }

    /**
     * Test with a function without a minimum.
     */
    @Test
    public void testFunctionWithNoMinimum() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.sin(a[0]) * Math.sin(a[1]);
            }
        };

        final PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, x);
        assertNull(result);
    }

    /**
     * Test with a function with multiple minima.
     */
    @Test
    public void testFunctionWithMultipleMinima() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0] - 2.0, 2.0) + Math.pow(a[1] - 2.0, 2.0);
            }
        };

        // Test the first minimum.
        final PointValuePair result1 = optimizer.optimize(f, GoalType.MINIMIZE, x);
        assertEquals(2.0, result1.getPoint()[0], 1e-8);
        assertEquals(2.0, result1.getPoint()[1], 1e-8);
        assertEquals(0.0, result1.getValue(), 1e-8);

        // Test the second minimum.
        final double[] x2 = new double[] { -2.0, -2.0 };
        final PointValuePair result2 = optimizer.optimize(f, GoalType.MINIMIZE, x2);
        assertEquals(-2.0, result2.getPoint()[0], 1e-8);
        assertEquals(-2.0, result2.getPoint()[1], 1e-8);
        assertEquals(0.0, result2.getValue(), 1e-8);
    }

    /**
     * Test with a function with bounds.
     */
    @Test
    public void testFunctionWithBounds() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0], 2.0) + Math.pow(a[1], 2.0);
            }
        };
        final NonUnivariateConstraint constraint = new NonUnivariateConstraint() {
            public boolean isSatisfied(double[] point) {
                // The function is not defined for negative values.
                for (double value : point) {
                    if (value < 0) {
                        return false;
                    }
                }
                return true;
            }
        };

        final PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, x,
                new SimpleBounds(new double[] { 0.0, 0.0 }, new double[] { 10.0, 10.0 }), constraint);
        assertEquals(Arrays.asList(new double[] { 2.0, 2.0 }), Arrays.asList(result.getPoint()));
        assertEquals(4.0, result.getValue(), 1e-8);
    }

    /**
     * Test with a non-univariate function.
     */
    @Test
    public void testNonUnivariateFunction() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0, 1.0 };
        final MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0], 2.0) + Math.pow(a[1], 2.0) + Math.pow(a[2], 2.0);
            }
        };

        final PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, x);
        assertEquals(Arrays.asList(new double[] { 0.0, 0.0, 0.0 }), Arrays.asList(result.getPoint()));
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test with a function with a large number of variables.
     */
    @Test
    public void testFunctionWithManyVariables() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[100];
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                double sum = 0.0;
                for (int i = 0; i < a.length; i++) {
                    sum += Math.pow(a[i], 2.0);
                }
                return sum;
            }
        };

        final PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, x);
        final double[] expected = new double[x.length];
        Arrays.fill(expected, 0.0);
        assertEquals(Arrays.asList(expected), Arrays.asList(result.getPoint()));
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test with an incorrect initial guess.
     */
    @Test
    public void testIncorrectInitialGuess() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { -1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0], 2.0) + Math.pow(a[1], 2.0);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, x));
    }

    /**
     * Test with a non-numeric initial guess.
     */
    @Test
    public void testNonNumericInitialGuess() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 'a' };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0], 2.0);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, x));
    }

    /**
     * Test with an incorrect goal type.
     */
    @Test
    public void testIncorrectGoalType() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0], 2.0) + Math.pow(a[1], 2.0);
            }
        };

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, null, x));
    }

    /**
     * Test with a null convergence checker.
     */
    @Test
    public void testNullConvergenceChecker() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0], 2.0) + Math.pow(a[1], 2.0);
            }
        };

        assertThrows(NullPointerException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, x, null));
    }

    /**
     * Test with a non-decreasing convergence checker.
     */
    @Test
    public void testNonDecreasingConvergenceChecker() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0], 2.0) + Math.pow(a[1], 2.0);
            }
        };
        final PointValuePairChecker checker = new PointValuePairChecker() {
            public boolean isSatisfied(PointValuePair p1, PointValuePair p2) {
                final double[] p1P = p1.getPoint();
                final double[] p2P = p2.getPoint();
                return Arrays.stream(p1P).allMatch(value -> value >= Arrays.stream(p2P).mapToDouble(Double::valueOf).min().getAsInt());
            }
        };

        final PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, x, null, checker);
        assertNull(result);
    }

    /**
     * Test with a non-increasing convergence checker.
     */
    @Test
    public void testNonIncreasingConvergenceChecker() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        final double[] x = new double[] { 1.0, 1.0 };
        final UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] a) {
                return Math.pow(a[0], 2.0) + Math.pow(a[1], 2.0);
            }
        };
        final PointValuePairChecker checker = new PointValuePairChecker() {
            public boolean isSatisfied(PointValuePair p1, PointValuePair p2) {
                final double[] p1P = p1.getPoint();
                final double[] p2P = p2.getPoint();
                return Arrays.stream(p1P).allMatch(value -> value <= Arrays.stream(p2P).mapToDouble(Double::valueOf).min().getAsInt());
            }
        };

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, x, null, checker));
    }
}
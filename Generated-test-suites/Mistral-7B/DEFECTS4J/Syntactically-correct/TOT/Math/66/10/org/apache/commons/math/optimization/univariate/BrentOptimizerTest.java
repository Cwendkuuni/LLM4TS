package org.apache.commons.math.optimization.univariate.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.RealVectorValueFunction;
import org.apache.commons.math3.special.E;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.BrentOptimizer;

/**
 * Test cases for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test optimization on a trivial function.
     */
    @Test
    public void testOptimizationTrivial() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -2.0, 2.0, 0.0);
        assertEquals(-1.0, result.getPoint(), 1e-14);
        assertEquals(1.0, result.getValue(), 1e-14);
    }

    /**
     * Test optimization on a function with a known minimum.
     */
    @Test
    public void testOptimizationFunctionWithKnownMinimum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.exp(Math.sin(x)), 2.0) - 2.0 * Math.exp(Math.sin(x)) + Math.exp(1.0) - 1.0;
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(-1.41372, result.getPoint(), 1e-14);
        assertEquals(0.0, result.getValue(), 1e-14);
    }

    /**
     * Test optimization on a function with a known maximum.
     */
    @Test
    public void testOptimizationFunctionWithKnownMaximum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.exp(Math.sin(x)), 2.0) + 2.0 * Math.exp(Math.sin(x)) + Math.exp(1.0) + 1.0;
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(1.41372, result.getPoint(), 1e-14);
        assertEquals(3.0, result.getValue(), 1e-14);
    }

    /**
     * Test optimization on a function with a minimum outside the bounds.
     */
    @Test
    public void testOptimizationFunctionMinimumOutsideBounds() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) - 4.0;
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 0.0, 0.0));
    }

    /**
     * Test optimization on a function with a maximum outside the bounds.
     */
    @Test
    public void testOptimizationFunctionMaximumOutsideBounds() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0) + 4.0;
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 10.0, 0.0));
    }

    /**
     * Test optimization on a function with an infinite minimum.
     */
    @Test
    public void testOptimizationFunctionInfimum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 0.0, 0.0));
    }

    /**
     * Test optimization on a function with an infinite maximum.
     */
    @Test
    public void testOptimizationFunctionSupremum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x)) * -1.0;
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 10.0, 0.0));
    }

    /**
     * Test optimization on a function with an infinite minimum and maximum.
     */
    @Test
    public void testOptimizationFunctionInfimumAndSupremum() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 0.0, 0.0));
        assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(new RealVectorValueFunction(f), GoalType.MAXIMIZE, new SimpleBounds(-10.0, 0.0), null));
    }

    /**
     * Test optimization on a function with an invalid starting point.
     */
    @Test
    public void testOptimizationInvalidStartingPoint() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 1.0, 0.0, 0.0));
    }

    /**
     * Test optimization on a function with an invalid step size.
     */
    @Test
    public void testOptimizationInvalidStepSize() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.abs(x)) - Math.exp(-Math.abs(x - 1.0));
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 0.0, 0.0));
    }

    /**
     * Test optimization on a function with an invalid accuracy.
     */
    @Test
    public void testOptimizationInvalidAccuracy() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.abs(x)) - Math.exp(-Math.abs(x - 1.0));
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 0.0, -1.0));
    }

    /**
     * Test optimization on a function with an invalid maximum iterations.
     */
    @Test
    public void testOptimizationInvalidMaxIterations() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.abs(x)) - Math.exp(-Math.abs(x - 1.0));
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 0.0, 0.0, -1));
    }

    /**
     * Test optimization on a function with a very large maximum iterations.
     */
    @Test
    public void testOptimizationLargeMaxIterations() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.abs(x)) - Math.exp(-Math.abs(x - 1.0));
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(Long.MAX_VALUE);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 0.0, 0.0);
        assertEquals(0.0, result.getPoint(), 1e-14);
        assertEquals(-E.E, result.getValue(), 1e-14);
    }

    /**
     * Test optimization on a cyclic function.
     */
    @Test
    public void testOptimizationCyclicFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x) + 1.0;
            }
        };
        UnivariateRealFunction optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -2.0 * Math.PI, 2.0 * Math.PI, 0.0);
        assertEquals(0.0, result.getPoint(), 1e-14);
        assertEquals(1.0, result.getValue(), 1e-14);
    }

}
package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MAXIMIZE;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Test;

/**
 * Test class for BrentOptimizer.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer()}.
     */
    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeMinimum() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 3);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double minValue = optimizer.optimize(f, MINIMIZE, -1, 1, 0).getValue();
        assertEquals(0.25, minValue, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimizeMinimumBounded() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 3);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double minValue = optimizer.optimize(f, MINIMIZE, -3, 3).getValue();
        assertEquals(0.25, minValue, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimizeMaximum() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1, -1, 1}, 3);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double maxValue = optimizer.optimize(f, MAXIMIZE, -1, 1, 0).getValue();
        assertEquals(0.75, maxValue, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimizeMaximumBounded() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{1, -1, 1}, 3);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double maxValue = optimizer.optimize(f, MAXIMIZE, -3, 3).getValue();
        assertEquals(0.75, maxValue, 1e-6);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Checking for a NotStrictlyPositiveException.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeAccuracy() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 3);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1e-6);
        optimizer.optimize(f, MINIMIZE, -1, 1, 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Checking for a NotStrictlyPositiveException.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithNegativeIterationCount() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 3);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(-10);
        optimizer.optimize(f, MINIMIZE, -1, 1, 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Checking for a MaxIterationsExceededException.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationsExceeded() throws Exception {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, 3);
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(0);
        optimizer.optimize(f, MINIMIZE, -1, 1, 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Checking for a FunctionEvaluationException.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithFunctionEvaluationException() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException();
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, MINIMIZE, 0, 1, 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Checking for an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeWithUnsupportedOperationException() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, MINIMIZE, 0, 1, 0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Checking for a MaxIterationsExceededException.
     */
    @Test
    public void testOptimizeWithMaxIterationsExceededExceptionMessage() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        try {
            optimizer.optimize(f, MINIMIZE, -1, 1, 0);
        } catch (MaxIterationsExceededException e) {
            assertEquals("Maximal iteration count exceeded.", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Checking for a MaxIterationsExceededException.
     */
    @Test
    public void testOptimizeWithMaxIterationsExceededExceptionMessageMaxEval() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        optimizer.setMaxEvaluations(1);
        try {
            optimizer.optimize(f, MINIMIZE, -1, 1, 0);
        } catch (MaxIterationsExceededException e) {
            assertEquals("Maximal iteration count exceeded.", e.getMessage());
            return;
        }
        fail();
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     * Checking for a MaxIterationsExceededException.
     */
    @Test
    public void testOptimizeWithMaxIterationsExceededExceptionMessageMaxAccuracy() throws Exception {
        UnivariateFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        optimizer.setAbsoluteAccuracy(Double.POSITIVE_INFINITY);
        try {
            optimizer.optimize(f, MINIMIZE, -1, 1, 0);
        } catch (MaxIterationsExceededException e) {
            assertEquals("Maximal iteration count exceeded.", e.getMessage());
            return;
        }
        fail();
    }
}
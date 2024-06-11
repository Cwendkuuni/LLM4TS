package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Logistic;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.analysis.function.Sin;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathematicalConstraintViolationException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.math.OpenIntInterval;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Tests {@link BrentOptimizer#BrentOptimizer()}.
     */
    @Test
    public void testConstructor() {
        final BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() {
        final UnivariateRealFunction f = new Pow(2, new Logistic(1));
        final BrentOptimizer optimizer = new BrentOptimizer();
        final double[] expected = {0.525572224021998, 2.718281828459045, 35};
        final double min = 0.1;
        final double max = 1.0;
        final double startValue = 0.5;

        final double result[] = optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        assertArrayEquals(expected, new double[]{result[0], result[1], result[2]});
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceeded() {
        final UnivariateRealFunction f = new Logistic(1);
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(f, GoalType.MINIMIZE, 0.1, 1.0, 0.5);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmall() {
        final UnivariateRealFunction f = new Logistic(1);
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(10000);
        optimizer.setMaximalIterationCount(10000);
        optimizer.setRelativeAccuracy(Precision.EPSILON);
        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 0.5);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLarge() {
        final UnivariateRealFunction f = new Logistic(1);
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(10000);
        optimizer.setMaximalIterationCount(10000);
        optimizer.setRelativeAccuracy(Precision.EPSILON);
        optimizer.optimize(f, GoalType.MINIMIZE, -100000.0, 1.0, 0.5);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArgument() {
        final UnivariateRealFunction f = null;
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, 0.1, 1.0, 0.5);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = MathematicalConstraintViolationException.class)
    public void testConstraintViolation() {
        final UnivariateRealFunction f = new Logistic(0);
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, 0.1, 1.0, 0.5);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluation() {
        final UnivariateRealFunction f = new Sin();
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, 0.1, 1.0, 0.5);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double)}.
     */
    @Test
    public void testOptimizeWithoutStartValue() {
        final UnivariateRealFunction f = new Pow(2, new Logistic(1));
        final BrentOptimizer optimizer = new BrentOptimizer();
        final double[] expected = {0.525572224021998, 2.718281828459045, 35};
        final double min = 0.1;
        final double max = 1.0;

        final double result[] = optimizer.optimize(f, GoalType.MINIMIZE, min, max);
        assertArrayEquals(expected, new double[]{result[0], result[1], result[2]});
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithStartValue() {
        final UnivariateRealFunction f = new Pow(2, new Logistic(1));
        final BrentOptimizer optimizer = new BrentOptimizer();
        final double[] expected = {0.525572224021998, 2.718281828459045, 35};
        final double min = 0.1;
        final double max = 1.0;
        final double startValue = 0.5;

        final double result[] = optimizer.optimize(f, GoalType.MINIMIZE, min, max, startValue);
        assertArrayEquals(expected, new double[]{result[0], result[1], result[2]});
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument() {
        final UnivariateRealFunction f = new Pow(2, new Logistic(1));
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, null, 0.1, 1.0);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument2() {
        final UnivariateRealFunction f = new Pow(2, new Logistic(1));
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testNoRoots() {
        final UnivariateRealFunction f = new Logistic(1);
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(10000);
        optimizer.setMaximalIterationCount(10000);
        optimizer.setRelativeAccuracy(1.0e-15);
        optimizer.optimize(f, GoalType.MINIMIZE, -10000.0, 10000.0);
        assertFalse(optimizer.isFound());
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        final UnivariateRealFunction f = new Logistic(1);
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);
        optimizer.setMaximalIterationCount(1);
        optimizer.optimize(f, GoalType.MINIMIZE, 0.1, 1.0, 0.5);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithInterval() {
        final UnivariateRealFunction f = new Pow(2, new Logistic(1));
        final BrentOptimizer optimizer = new BrentOptimizer();
        final double[] expected = {0.525572224021998, 2.718281828459045, 35};
        final GoalType goalType = GoalType.MINIMIZE;
        final OpenIntInterval interval = new OpenIntInterval(1, 100);

        final double result[] = optimizer.optimize(f, goalType, interval);
        assertArrayEquals(expected, new double[]{result[0], result[1], result[2]});
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument3() {
        final UnivariateRealFunction f = new Pow(2, new Logistic(1));
        final BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, null, 1.0, 0.5);
    }

    /**
     * Tests {@link BrentOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithInterval2() {
        final UnivariateRealFunction f = new Pow(2, new Logistic(1));
        final BrentOptimizer optimizer = new BrentOptimizer();
        final double[] expected = {0.525572224021998, 2.718281828459045, 35};
        final GoalType goalType = GoalType.MINIMIZE;
        final OpenIntInterval interval = new OpenIntInterval(0, 1);

        final double result[] = optimizer.optimize(f, goalType, interval);
        assertArrayEquals(expected, new double[]{result[0], result[1], result[2]});
    }
}
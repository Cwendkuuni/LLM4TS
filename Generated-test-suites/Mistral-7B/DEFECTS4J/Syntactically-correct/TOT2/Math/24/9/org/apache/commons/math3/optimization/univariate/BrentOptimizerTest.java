package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.exception.MaxEvalException;

import org.junit.Test;

/**
 * Test cases for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     */
    @Test
    public void testBrentOptimizerConstructor() {
        UnivariateOptimizer opt = new BrentOptimizer(0.1, 0.01, null);
        assertEquals(0.1, opt.getRelativeThreshold(), 0.0);
        assertEquals(0.01, opt.getAbsoluteThreshold(), 0.0);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     */
    @Test
    public void testBrentOptimizerConstructor2() {
        UnivariateOptimizer opt = new BrentOptimizer(0.1, 0.01);
        assertEquals(0.1, opt.getRelativeThreshold(), 0.0);
        assertEquals(0.01, opt.getAbsoluteThreshold(), 0.0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     */
    @Test
    public void testOptimizeSimpleFunction() throws MaxEvalException {
        UnivariateFunction func = new SimpleFunction(0);
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        UnivariatePointValuePair result = opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with custom convergence checker
     */
    @Test
    public void testOptimizeSimpleFunctionWithChecker() throws MaxEvalException {
        UnivariateFunction func = new SimpleFunction(0);
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0, new MyConvergenceChecker());
        UnivariatePointValuePair result = opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
        assertEquals(1.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a minimum outside the bounds provided
     */
    @Test(expected = MaxEvalException.class)
    public void testOptimizeFunctionOutsideBounds() throws MaxEvalException {
        UnivariateFunction func = new SimpleFunction(Math.E);
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a minimum inside the bounds provided
     * but with a large step size
     */
    @Test
    public void testOptimizeFunctionLargeStepSize() throws MaxEvalException {
        UnivariateFunction func = new SimpleFunction(Math.E);
        UnivariateOptimizer opt = new BrentOptimizer(10.0, 0.0);
        UnivariatePointValuePair result = opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
        assertEquals(1.718281828459045, result.getPoint(), 0.001);
        assertEquals(1.718281828459045, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a minimum inside the bounds provided
     * but with a small step size
     */
    @Test
    public void testOptimizeFunctionSmallStepSize() throws MaxEvalException {
        UnivariateFunction func = new SimpleFunction(Math.E);
        UnivariateOptimizer opt = new BrentOptimizer(0.001, 0.0);
        UnivariatePointValuePair result = opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
        assertEquals(1.718281828459045, result.getPoint(), 0.0001);
        assertEquals(1.718281828459045, result.getValue(), 0.0001);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that is not monotonic
     */
    @Test
    public void testOptimizeNonMonotonicFunction() throws MaxEvalException {
        UnivariateFunction func = new NonMonotonicFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        UnivariatePointValuePair result = opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
        assertEquals(1.5, result.getPoint(), 0.01);
        assertEquals(1.5 * Math.sqrt(2), result.getValue(), 0.01);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has multiple local minima
     */
    @Test
    public void testOptimizeFunctionMultipleLocalMinima() throws MaxEvalException {
        UnivariateFunction func = new MultipleLocalMinimaFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        UnivariatePointValuePair result = opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 10));
        assertEquals(2.5, result.getPoint(), 0.01);
        assertEquals(0.5, result.getValue(), 0.01);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a vertical asymptote
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionVerticalAsymptote() {
        UnivariateFunction func = new VerticalAsymptoteFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a horizontal asymptote
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionHorizontalAsymptote() {
        UnivariateFunction func = new HorizontalAsymptoteFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has an infinite minimum
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionInfiniteMinimum() {
        UnivariateFunction func = new InfiniteMinimumFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a singular point
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionSingularPoint() {
        UnivariateFunction func = new SingularPointFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a vertical inflection point
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionVerticalInflectionPoint() {
        UnivariateFunction func = new VerticalInflectionPointFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a horizontal inflection point
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeFunctionHorizontalInflectionPoint() {
        UnivariateFunction func = new HorizontalInflectionPointFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 2));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a local maximum inside the bounds provided
     */
    @Test
    public void testOptimizeFunctionLocalMaximum() throws MaxEvalException {
        UnivariateFunction func = new SimpleFunction(Math.E);
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        UnivariatePointValuePair result = opt.optimize(func, GoalType.MAXIMIZE, new SimpleBounds(1, 2));
        assertEquals(1.718281828459045, result.getPoint(), 0.001);
        assertEquals(1.718281828459045, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that has a global maximum outside the bounds provided
     */
    @Test(expected = MaxEvalException.class)
    public void testOptimizeFunctionGlobalMaximumOutsideBounds() throws MaxEvalException {
        UnivariateFunction func = new SimpleFunction(Math.pow);
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        opt.optimize(func, GoalType.MAXIMIZE, new SimpleBounds(1, 2));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType, SimpleBounds)}
     * with a function that oscillates around the minimum
     */
    @Test
    public void testOptimizeFunctionOscillatingAroundMinimum() throws MaxEvalException {
        UnivariateFunction func = new OscillatingFunction();
        UnivariateOptimizer opt = new BrentOptimizer(0.01, 0.0);
        UnivariatePointValuePair result = opt.optimize(func, GoalType.MINIMIZE, new SimpleBounds(1, 10));
        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    private static class SimpleFunction implements UnivariateFunction {
        private final double value;

        public SimpleFunction(double value) {
            this.value = value;
        }

        @Override
        public double value(double x) {
            return value;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return (iteration > 100) || (Math.abs(previous.getValue() - current.getValue()) < 1e-5);
        }
    }

    private static class NonMonotonicFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x <= 1.5) {
                return Math.pow(x, 2);
            } else {
                return 1.5 * Math.sqrt(x) - 0.5;
            }
        }
    }

    private static class MultipleLocalMinimaFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) * Math.sin(Math.PI * x);
        }
    }

    private static class VerticalAsymptoteFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return 1.0 / x;
        }
    }

    private static class HorizontalAsymptoteFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(Math.PI / 2.0) * Math.log(Math.abs(x));
        }
    }

    private static class InfiniteMinimumFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return 1.0 / (x * x);
        }
    }

    private static class SingularPointFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x == 0) {
                throw new IllegalArgumentException("Cannot evaluate function at x = 0");
            }
            return Math.sin(Math.PI / x);
        }
    }

    private static class VerticalInflectionPointFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) * Math.log(Math.abs(x));
        }
    }

    private static class HorizontalInflectionPointFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.abs(x - 2), 2) * Math.sin(Math.PI / 2.0);
        }
    }

    private static class OscillatingFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(Math.PI * x) / (Math.PI * x + 1.0);
        }
    }
}
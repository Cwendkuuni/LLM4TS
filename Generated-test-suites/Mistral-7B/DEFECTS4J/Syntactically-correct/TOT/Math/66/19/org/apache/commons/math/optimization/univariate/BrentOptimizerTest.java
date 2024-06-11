package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.BracketedBrent;
import org.apache.commons.math3.optim.nonlinear.Brent;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    @Test
    public void testOptimizeSimpleFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 5.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(goalType, result.getGoalType());
        assertEquals(min, result.getPoint(), 1e-9);
        assertEquals(-result.getValue(), f.value(min), 1e-9);
    }

    @Test
    public void testOptimizeFunctionWithInflectionPoint() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 3) - 4 * Math.pow(x - 1, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.4;
        double max = 1.4;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(goalType, result.getGoalType());
        assertEquals(min, result.getPoint(), 1e-9);
        assertEquals(-result.getValue(), f.value(min), 1e-9);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeFunctionWithMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 0.001;
        double startValue = 0.001;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionWithException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new Exception("Exception");
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail();
        } catch (RuntimeException e) {
            //Expected
        }
    }

    @Test
    public void testOptimizeFunctionWithInvalidStartValue() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(f, goalType, min, max, startValue);
            fail();
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeFunctionWithInvalidBounds() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.optimize(f, goalType, min, max);
            fail();
        } catch (NotStrictlyPositiveException e) {
            //Expected
        }
    }

    @Test
    public void testOptimizeFunctionWithGoalTypeMaximize() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 5.0;
        double startValue = 2.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(goalType, result.getGoalType());
        assertEquals(max, result.getPoint(), 1e-9);
        assertEquals(result.getValue(), f.value(max), 1e-9);
    }

    @Test
    public void testOptimizeFunctionWithBracketedBrent() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * Math.pow(x, 0.5) + 2;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        double startValue = 1.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(goalType, result.getGoalType());
        assertEquals(min, result.getPoint(), 1e-9);
        assertEquals(-result.getValue(), f.value(min), 1e-9);

        // Test with BracketedBrent
        UnivariateOptimizer bracketedBrent = new BracketedBrent(f, min, max);
        PointValuePair bracketedResult = bracketedBrent.optimize();
        assertEquals(goalType, bracketedResult.getGoalType());
        assertEquals(min, bracketedResult.getPoint(), 1e-9);
        assertEquals(-bracketedResult.getValue(), f.value(min), 1e-9);
    }

    @Test
    public void testOptimizeFunctionWithBrent() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * Math.pow(x, 0.5) + 2;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        double startValue = 1.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(goalType, result.getGoalType());
        assertEquals(min, result.getPoint(), 1e-9);
        assertEquals(-result.getValue(), f.value(min), 1e-9);

        // Test with Brent
        UnivariateOptimizer brent = new Brent(f, new SimpleBounds(min, max));
        PointValuePair brentResult = brent.optimize();
        assertEquals(goalType, brentResult.getGoalType());
        assertEquals(min, brentResult.getPoint(), 1e-9);
        assertEquals(-brentResult.getValue(), f.value(min), 1e-9);
    }
}
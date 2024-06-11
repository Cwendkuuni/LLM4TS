import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.NonlinearConstraint;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructorWithRelativeAndAbsoluteToleranceAndConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return iteration > 10;
            }
        });
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorWithRelativeToleranceLessThanMinimum() {
        new BrentOptimizer(0.001d, 0.0d, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorWithNegativeAbsoluteTolerance() {
        new BrentOptimizer(0.01d, -0.001d, null);
    }

    @Test
    public void testDoOptimize_InitialGuessBelowMin() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(0.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        assertEquals(0.0, result.getPoint().getValue(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_InitialGuessAboveMax() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(2.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x;
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        assertEquals(1.0, result.getPoint().getValue(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_MaxIterationsReached() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(0.5);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.1, 1.0);
        optimizer.setMaxIterations(0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        fail();
    }

    @Test
    public void testDoOptimize_NoRootFound() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(1.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        assertNull(result);
    }

    @Test
    public void testDoOptimize_RootFound() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(2.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        SimpleBounds bounds = new SimpleBounds(1.0, 5.0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        assertEquals(2.0, result.getPoint().getValue(), 0.001);
        assertEquals(-4.0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_RootCloseToMinBound() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(0.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.01;
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        assertEquals(0.0, result.getPoint().getValue(), 0.001);
        assertEquals(-0.01, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_RootCloseToMaxBound() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(10.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.01;
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        assertEquals(10.0, result.getPoint().getValue(), 0.001);
        assertEquals(-0.01, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_RootBetweenMinAndMaxBound() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(3.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        assertEquals(2.0, result.getPoint().getValue(), 0.001);
        assertEquals(-1.0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_RootCloseToMinAndMaxBound() throws MaxEval {
        BrentOptimizer optimizer = new BrentOptimizer(0.01d, 0.001d, null);
        InitialGuess initialGuess = new InitialGuess(1.0);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 0.005;
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 2.0);
        UnivariateOptimizerResult result = optimizer.optimize(function, initialGuess, bounds);
        assertEquals(1.0, result.getPoint().getValue(), 0.001);
        assertEquals(-0.005, result.getValue(), 0.001);
    }

    @Test
    public void testBest_NullFirst() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair result = BrentOptimizer.this.best(a, b, false);
        assertEquals(b, result);
    }

    @Test
    public void testBest_NullSecond() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair result = BrentOptimizer.this.best(a, b, false);
        assertEquals(a, result);
    }

    @Test
    public void testBest_BothSame() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair result = BrentOptimizer.this.best(a, b, false);
        assertEquals(a, result);
    }

    @Test
    public void testBest_FirstMin() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair result = BrentOptimizer.this.best(a, b, false);
        assertEquals(a, result);
    }

    @Test
    public void testBest_SecondMin() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair result = BrentOptimizer.this.best(a, b, false);
        assertEquals(b, result);
    }

    @Test
    public void testBest_FirstMax() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair result = BrentOptimizer.this.best(a, b, true);
        assertEquals(b, result);
    }

    @Test
    public void testBest_SecondMax() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair result = BrentOptimizer.this.best(a, b, true);
        assertEquals(a, result);
    }
}
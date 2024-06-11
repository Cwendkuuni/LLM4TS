import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private FunctionFunction function;

    @Before
    public void setup() {
        function = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * x + 4;
            }
        };
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        new BrentOptimizer(0.0, 5.0, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.01, -1.0, null);
    }

    @Test
    public void testDoOptimize_Minimization() {
        optimizer = new BrentOptimizer(0.01, 1.0, null);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 10.0);
        Assert.assertEquals(0.33969465, result.getPoint(), 0.001);
        Assert.assertEquals(-3.31579373, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_Maximization() {
        optimizer = new BrentOptimizer(0.01, 1.0, null, GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function.negate(), 0.0, 10.0);
        Assert.assertEquals(4.0, result.getPoint(), 0.001);
        Assert.assertEquals(33.1579373, result.getValue(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimize_NullFunction() {
        optimizer = new BrentOptimizer(0.01, 1.0, null);
        optimizer.optimize(null, 0.0, 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimize_InvalidMin() {
        optimizer = new BrentOptimizer(0.01, 1.0, null);
        optimizer.optimize(function, -1.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimize_InvalidMax() {
        optimizer = new BrentOptimizer(0.01, 1.0, null);
        optimizer.optimize(function, 10.0, -1.0);
    }

    @Test
    public void testDoOptimize_ConvergenceChecker() {
        class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair newPoint) {
                return (current.getValue() - newPoint.getValue()) < 0.001;
            }
        }

        optimizer = new BrentOptimizer(0.01, 1.0, new MyConvergenceChecker());
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 10.0);
        Assert.assertEquals(0.33969465, result.getPoint(), 0.001);
        Assert.assertEquals(-3.31579373, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_EmptyConvergenceChecker() {
        class MyEmptyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair newPoint) {
                return false;
            }
        }

        optimizer = new BrentOptimizer(0.01, 1.0, new MyEmptyConvergenceChecker());
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 10.0);
        Assert.assertEquals(Optional.of(0.33969465), optimizer.getBestPoint(), OptimizationResult.SUCCESS);
        Assert.assertEquals(Optional.of(-3.31579373), optimizer.getBestValue(), OptimizationResult.SUCCESS);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 4.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 9.0);

        Assert.assertEquals(a, optimizer.best(null, a, false));
        Assert.assertEquals(b, optimizer.best(a, b, false));
        Assert.assertEquals(c, optimizer.best(b, c, false));

        Assert.assertEquals(a, optimizer.best(null, a, true));
        Assert.assertEquals(b, optimizer.best(a, b, true));
        Assert.assertEquals(c, optimizer.best(b, c, true));
    }
}
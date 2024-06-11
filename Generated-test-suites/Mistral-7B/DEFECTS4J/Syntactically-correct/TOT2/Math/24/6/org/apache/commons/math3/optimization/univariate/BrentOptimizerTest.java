import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(f, -10, 10, GoalType.MINIMIZE);
        Assert.assertEquals(0.0, solution.getPoint(), 1e-7);
        Assert.assertEquals(0.0, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizationWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(f, 2.5, GoalType.MINIMIZE);
        Assert.assertEquals(0.0, solution.getPoint(), 1e-7);
        Assert.assertEquals(0.0, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMinimizationWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2));
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(f, -10, 10, GoalType.MINIMIZE);
        Assert.assertEquals(-1.275492030331328, solution.getPoint(), 1e-7);
        Assert.assertEquals(0.284704398194255, solution.getValue(), 1e-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidArguments() {
        new BrentOptimizer(-1e-6, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        new BrentOptimizer(0, 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(f, -10, 10, GoalType.MAXIMIZE);
        Assert.assertEquals(0.0, solution.getPoint(), 1e-7);
        Assert.assertEquals(0.0, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMaximizationWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(f, 2.5, GoalType.MAXIMIZE);
        Assert.assertEquals(0.0, solution.getPoint(), 1e-7);
        Assert.assertEquals(0.0, solution.getValue(), 1e-7);
    }

    @Test
    public void testBrentOptimizerMaximizationWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2));
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(f, -10, 10, GoalType.MAXIMIZE);
        Assert.assertEquals(1.275492030331328, solution.getPoint(), 1e-7);
        Assert.assertEquals(0.284704398194255, solution.getValue(), 1e-7);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return current.getValue() < 0.0;
        }
    }
}
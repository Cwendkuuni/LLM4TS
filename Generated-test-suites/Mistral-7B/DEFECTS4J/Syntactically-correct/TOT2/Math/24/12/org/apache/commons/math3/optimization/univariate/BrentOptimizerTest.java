import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltypes.GoalType;
import org.junit.Test;
import org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_SimpleMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new SquareFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(-1.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_SimpleMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbsFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE);
        assertEquals(1.0, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_NegativeRelativeThreshold() {
        new BrentOptimizer(-1.0, 1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_NegativeAbsoluteThreshold() {
        new BrentOptimizer(1.0, -1.0, null);
    }

    @Test
    public void testBrentOptimizer_ConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker
            = new CustomConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
        UnivariateFunction function = new SinFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertTrue(checker.wasConverged());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_NullFunction() {
        new BrentOptimizer(1e-6, 1e-6, null);
    }

    @Test
    public void testBrentOptimizer_FunctionWithMultipleMinima() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new QuarticFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(-1.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_MismatchedGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbsFunction();
        try {
            optimizer.optimize(function, GoalType.MAXIMIZE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        private boolean wasConverged = false;

        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            wasConverged = true;
            return true;
        }

        public boolean wasConverged() {
            return wasConverged;
        }
    }

    private static class SquareFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }
    }

    private static class AbsFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.abs(x);
        }
    }

    private static class SinFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    }

    private static class QuarticFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x * x * x - 4.0 * x * x + 1.0;
        }
    }
}
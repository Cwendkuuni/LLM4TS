import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.SimpleUnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointsValuePair;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    /**
     * Test function: f(x) = x^3 - 2x^2 + x + 1.
     */
    @Test
    public void testBrentOptimizer() {
        UnivariateFunction function = new SimpleUnivariateFunction("testFunction", x -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x + 1);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        PointsValuePair result = optimizer.optimize(function, new InitialGuess(1.0), new MaxEval(100));

        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType(), 0);
        Assert.assertTrue(Math.abs(result.getValue()) < 1e-6);
        Assert.assertEquals(0.0, result.getPoint(), 1e-6);
    }

    /**
     * Test function: f(x) = Math.sin(x) - x.
     */
    @Test
    public void testBrentOptimizerSin() {
        UnivariateFunction function = new SimpleUnivariateFunction("testFunction", x -> Math.sin(x) - x);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        PointsValuePair result = optimizer.optimize(function, new InitialGuess(1.0), new MaxEval(100));

        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType(), 0);
        Assert.assertTrue(Math.abs(result.getValue()) < 1e-6);
        Assert.assertEquals(-1.1975521860292887, result.getPoint(), 1e-6);
    }

    /**
     * Test for negative absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new BrentOptimizer(1e-6, -1e-6, null);
    }

    /**
     * Test for relative threshold smaller than machine precision.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThreshold() {
        new BrentOptimizer(1e-10, 1e-6, null);
    }

    /**
     * Test for null convergence checker.
     */
    @Test
    public void testNullConvergenceChecker() {
        UnivariateFunction function = new SimpleUnivariateFunction("testFunction", x -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x + 1);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        PointsValuePair result = optimizer.optimize(function, new InitialGuess(1.0), new MaxEval(100));

        Assert.assertEquals(GoalType.MINIMIZE, optimizer.getGoalType(), 0);
        Assert.assertTrue(Math.abs(result.getValue()) < 1e-6);
        Assert.assertEquals(0.0, result.getPoint(), 1e-6);
    }

    /**
     * Test for null function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        new BrentOptimizer(1e-6, 1e-6, null);
    }
}
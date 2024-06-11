import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws Exception {
        UnivariateRealFunction function = new Function("function", x -> Math.pow(x, 3) - 4 * x * Math.pow(x, 2) + 4 * x - 4);
        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        double initialGuess = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, initialGuess);

        Assert.assertEquals(goalType, result.getGoalType());
        Assert.assertEquals(min, result.getPoint(), 0.001);
        Assert.assertEquals(-12.0, result.getValue(), 0.001);

        // Test with different initial guess
        initialGuess = 2.0;
        result = optimizer.optimize(function, goalType, min, max, initialGuess);

        Assert.assertEquals(goalType, result.getGoalType());
        Assert.assertEquals(min, result.getPoint(), 0.001);
        Assert.assertEquals(-12.0, result.getValue(), 0.001);

        // Test with MAXIMIZE goal type
        goalType = GoalType.MAXIMIZE;
        double minimum = -result.getPoint();
        double expectedMaximum = -result.getValue();

        result = optimizer.optimize(function, goalType, min, max, initialGuess);

        Assert.assertEquals(goalType, result.getGoalType());
        Assert.assertEquals(maximum, result.getPoint(), 0.001);
        Assert.assertEquals(expectedMaximum, result.getValue(), 0.001);
    }

    @FunctionalInterface
    interface Function extends UnivariateRealFunction {
        Function INSTANCE = function -> function;
    }
}
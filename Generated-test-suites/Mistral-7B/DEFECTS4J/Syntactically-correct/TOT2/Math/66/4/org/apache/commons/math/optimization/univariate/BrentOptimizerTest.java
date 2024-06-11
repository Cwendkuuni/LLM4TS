import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_Minimize() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new Function("f", x -> Math.pow(x, 3) - 4 * x + 2);
        UnivariateRealOptimizer optimizer = new BrentOptimizer();

        // Basic test case: Minimize a simple function.
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        double expected = -1.0;
        Assert.assertEquals(expected, result.getPoint(), 0.001);

        // Test with given start value.
        result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 1.0);
        Assert.assertEquals(expected, result.getPoint(), 0.001);

        // Test with given minimum value.
        result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, -1.0);
        Assert.assertEquals(expected, result.getPoint(), 0.001);

        // Test with given maximum value.
        result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 3.0);
        Assert.assertEquals(expected, result.getPoint(), 0.001);

        // Edge cases: Minimize a function with non-real roots.
        UnivariateRealFunction functionSqrt = x -> Math.sqrt(x) - 1;
        result = optimizer.optimize(functionSqrt, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(-2.0, result.getPoint(), 0.01);

        // Error scenarios: Minimize a function with max iterations exceeded.
        UnivariateRealFunction functionSin = x -> Math.sin(x);
        optimizer.setMaxIterations(1);
        try {
            optimizer.optimize(functionSin, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
            Assert.fail("Max iterations exceeded exception not thrown.");
        } catch (MaxIterationsExceededException e) {
            // Expected.
        }
    }

    @Test
    public void testBrentOptimizer_Maximize() throws MaxIterationsExceededException {
        UnivariateRealFunction function = x -> Math.pow(x, 3) + 4 * x - 2;
        UnivariateRealOptimizer optimizer = new BrentOptimizer();

        // Basic test case: Maximize a simple function.
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        double expected = 1.0;
        Assert.assertEquals(expected, result.getPoint(), 0.001);

        // Test with given start value.
        result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 1.0);
        Assert.assertEquals(expected, result.getPoint(), 0.001);

        // Test with given minimum value.
        result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, -1.0);
        Assert.assertEquals(expected, result.getPoint(), 0.001);

        // Test with given maximum value.
        result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 3.0);
        Assert.assertEquals(expected, result.getPoint(), 0.001);

        // Edge cases: Maximize a function with non-real roots.
        UnivariateRealFunction functionSqrt = x -> Math.sqrt(x) + 1;
        result = optimizer.optimize(functionSqrt, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(2.0, result.getPoint(), 0.01);

        // Error scenarios: Maximize a function with max iterations exceeded.
        UnivariateRealFunction functionCos = x -> Math.cos(x);
        optimizer.setMaxIterations(1);
        try {
            optimizer.optimize(functionCos, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
            Assert.fail("Max iterations exceeded exception not thrown.");
        } catch (MaxIterationsExceededException e) {
            // Expected.
        }
    }
}
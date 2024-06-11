import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the BrentOptimizer class.
 */
public class BrentOptimizerTest {

    private UnivariateRealFunction testFunction;
    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        testFunction = new MyTestFunction();
        optimizer = new BrentOptimizer();
    }

    /**
     * Test optimization with minimum goal.
     */
    @Test
    public void testMinimization() {
        double min = -5.0;
        double max = 5.0;
        double startValue = 0.0;
        double expectedResult = -3.0;

        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, result.getPoint(), 0.001);
    }

    /**
     * Test optimization with maximum goal and reversed function.
     */
    @Test
    public void testMaximization() {
        double min = -5.0;
        double max = 5.0;
        double startValue = 0.0;
        double expectedResult = 3.0;

        testFunction = new MyTestFunctionReversed();
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, min, max, startValue);

        Assert.assertEquals(expectedResult, result.getPoint(), 0.001);
    }

    /**
     * Test optimization with boundary limits.
     */
    @Test
    public void testBoundaryLimits() {
        double min = 0.0;
        double max = 0.0;
        double startValue = 0.0;

        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);

        Assert.assertEquals(startValue, result.getPoint(), 0.001);
    }

    /**
     * Test optimization with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 0.0, 0.0);
    }

    /**
     * Test optimization with maximum iterations reached.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.0;
        optimizer.setMaxIterations(0);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);
    }

    /**
     * Test function for optimization.
     */
    private class MyTestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 3.0 * x * Math.pow(Math.sin(Math.PI * x), 2);
        }

        @Override
        public double derivative(double x) {
            return 3.0 * x * (1.0 - Math.pow(Math.sin(Math.PI * x), 2)) - 3.0 * Math.sin(Math.PI * x) * Math.cos(Math.PI * x);
        }
    }

    /**
     * Test function for optimization with maximum goal and reversed.
     */
    private class MyTestFunctionReversed implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return -(MyTestFunction.this.value(x));
        }

        @Override
        public double derivative(double x) {
            return -MyTestFunction.this.derivative(x);
        }
    }
}
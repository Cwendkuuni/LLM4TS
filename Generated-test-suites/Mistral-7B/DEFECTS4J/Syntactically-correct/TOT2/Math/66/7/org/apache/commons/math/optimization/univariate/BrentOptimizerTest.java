import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test cases for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    /**
     * Test optimization of simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        Assert.assertEquals("Optimum value is not as expected.", -2.5, result.getValue(), 0.001);
        Assert.assertEquals("Optimum point is not as expected.", -2.5, result.getPoint(), 0.001);
    }

    /**
     * Test optimization of function with minimum at an endpoint.
     */
    @Test
    public void testOptimizeFunctionMinimumAtEndpoint() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 0.0;
        double startValue = 0.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        Assert.assertEquals("Optimum value is not as expected.", 0.0, result.getValue(), 0.001);
        Assert.assertEquals("Optimum point is not as expected.", 0.0, result.getPoint(), 0.001);
    }

    /**
     * Test optimization of function with minimum between endpoints.
     */
    @Test
    public void testOptimizeFunctionMinimumBetweenEndpoints() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        Assert.assertEquals("Optimum value is not as expected.", 0.0, result.getValue(), 0.001);
        Assert.assertEquals("Optimum point is not as expected.", -0.5, result.getPoint(), 0.001);
    }

    /**
     * Test optimization of function with maximum at an endpoint.
     */
    @Test
    public void testOptimizeFunctionMaximumAtEndpoint() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        Assert.assertEquals("Optimum value is not as expected.", 25.0, result.getValue(), 0.001);
        Assert.assertEquals("Optimum point is not as expected.", 5.0, result.getPoint(), 0.001);
    }

    /**
     * Test optimization of function with maximum between endpoints.
     */
    @Test
    public void testOptimizeFunctionMaximumBetweenEndpoints() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize
                (f, goalType, new SimpleBounds(min, max), new GoalTypeConstraint(goalType));

        Assert.assertEquals("Optimum value is not as expected.", 0.25, result.getValue(), 0.001);
        Assert.assertEquals("Optimum point is not as expected.", 0.0, result.getPoint(), 0.001);
    }

    /**
     * Test optimization of function with initial point outside the bounds.
     */
    @Test
    public void testOptimizeFunctionOutsideBounds() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 20.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        expectedEx.expect(MaxIterationsExceededException.class);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test optimization of function with initial point as minimum.
     */
    @Test
    public void testOptimizeFunctionInitialPointIsMinimum() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 0.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        Assert.assertEquals("Optimum value is not as expected.", 0.0, result.getValue(), 0.001);
        Assert.assertEquals("Optimum point is not as expected.", 0.0, result.getPoint(), 0.001);
    }

    /**
     * Test optimization of function with initial point as maximum.
     */
    @Test
    public void testOptimizeFunctionInitialPointIsMaximum() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 100.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        Assert.assertEquals("Optimum value is not as expected.", 10000.0, result.getValue(), 0.001);
        Assert.assertEquals("Optimum point is not as expected.", 100.0, result.getPoint(), 0.001);
    }

    /**
     * Test optimization of function with maximum iteration count.
     */
    @Test
    public void testOptimizeFunctionMaxIterations() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(3);
        expectedEx.expect(MaxIterationsExceededException.class);
        optimizer.optimize(f, goalType, min, max, startValue);
    }

    /**
     * Test optimization of function with absolute accuracy.
     */
    @Test
    public void testOptimizeFunctionAbsoluteAccuracy() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1.0E-3);
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        Assert.assertEquals("Optimum value is not as expected.", 0.001, result.getValue(), 0.0005);
        Assert.assertEquals("Optimum point is not as expected.", 0.0, result.getPoint(), 0.001);
    }

    /**
     * Test optimization of function with relative accuracy.
     */
    @Test
    public void testOptimizeFunctionRelativeAccuracy() {
        UnivariateRealFunction f = new Function("x^2");
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(1.0E-3);
        PointValuePair result = optimizer.optimize(f, goalType, min, max, startValue);

        Assert.assertEquals("Optimum value is not as expected.", 0.0001, result.getValue(), 0.00005);
        Assert.assertEquals("Optimum point is not as expected.", 0.0, result.getPoint(), 0.001);
    }

    private class Function implements UnivariateRealFunction {
        private final String name;

        public Function(String name) {
            this.name = name;
        }

        @Override
        public double value(double x) {
            System.out.println("Evaluated " + name + "(" + x + ")");
            return Math.pow(x, 2);
        }
    }
}
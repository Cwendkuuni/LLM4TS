import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_Minimum() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2);
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMUM, bounds);
        double expected = 0.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected, result.getValue(), 1e-8);
    }

    @Test
    public void testBrentOptimizer_Maximum() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2);
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        PointValuePair result = optimizer.optimize(function.negate(), GoalType.MAXIMUM, bounds);
        double expected = 1.0;
        Assert.assertEquals(expected, result.getPoint(), 1e-8);
        Assert.assertEquals(expected, result.getValue(), 1e-8);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizer_MaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2);
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        optimizer.setMaxIterations(1); // Set an unrealistic number of iterations
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMUM, bounds);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizer_FunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = f -> {
            throw new FunctionEvaluationException("Exception while evaluating function");
        };
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMUM, bounds);
    }

    @Test
    public void testBrentOptimizer_OutOfBounds() throws Exception {
        UnivariateRealFunction function = f -> Math.log(f);
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        SimpleBounds bounds = new SimpleBounds(-1.0, 0.0);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMUM, bounds);
        Assert.assertFalse(result != null);
    }

    @Test
    public void testBrentOptimizer_NotStrictlyPositiveAccuracy() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2);
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-0.01);
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMUM, bounds);
        Assert.assertEquals(Double.NaN, result.getPoint(), 0);
        Assert.assertEquals(Double.NaN, result.getValue(), 0);
    }
}
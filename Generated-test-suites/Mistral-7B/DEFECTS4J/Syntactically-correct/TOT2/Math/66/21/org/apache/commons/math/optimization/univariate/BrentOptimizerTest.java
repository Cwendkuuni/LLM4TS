import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_Minimize() throws Exception {
        UnivariateRealFunction function = new MyTestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();

        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{-10.0, 10.0};
        PointValuePair result = optimizer.optimize(function, goalType, bounds[0], bounds[1], 0.0);

        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_Maximize() throws Exception {
        UnivariateRealFunction function = new MyTestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();

        GoalType goalType = GoalType.MAXIMIZE;
        double[] bounds = new double[]{-10.0, 10.0};
        PointValuePair result = optimizer.optimize(function, goalType, bounds[0], bounds[1], 0.0);

        Assert.assertEquals(12.0, result.getValue(), 1e-6);
        Assert.assertEquals(-2.0, result.getPoint(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizer_MaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new MyTestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();

        optimizer.setMaxIterations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizer_FunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = new MyThrowsFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();

        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_NotStrictlyPositiveException() throws Exception {
        UnivariateRealFunction function = new MyNegativeBoundsFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();

        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, -5.0, 0.0);
    }

    @Test
    public void testBrentOptimizer_goalTypeValueConstraint() throws Exception {
        UnivariateRealFunction function = new MyTestFunction();
        UnivariateOptimizer optimizer = new BrentOptimizer();

        double[] bounds = new double[]{0.0, 10.0};
        GoalTypeValue goalTypeValue = new GoalTypeValue(GoalType.MINIMIZE, 5.0);
        GoalTypeConstraint goalTypeConstraint = new GoalTypeConstraint(goalTypeValue);

        PointValuePair result = optimizer.optimize(function, goalTypeConstraint, bounds[0], bounds[1], 0.0);

        Assert.assertEquals(5.0, result.getValue(), 1e-6);
        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
    }

    private static class MyTestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 4.0 * x + 4.0;
        }
    }

    private static class MyThrowsFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws Exception {
            if (x == 2.0) {
                throw new FunctionEvaluationException("Exception in function evaluation at x = 2");
            }
            return Math.sin(x) / (x - 2.0);
        }
    }

    private static class MyNegativeBoundsFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }
    }
}
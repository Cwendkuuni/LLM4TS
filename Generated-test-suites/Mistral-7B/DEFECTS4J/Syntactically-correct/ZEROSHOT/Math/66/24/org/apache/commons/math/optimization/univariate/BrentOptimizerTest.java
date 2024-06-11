import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private UnivariateOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer();
        function = new MyFunction();
    }

    @Test
    public void testMinimization() throws MaxIterationsExceededException, NotStrictlyPositiveException {
        double min = Double.NEGATIVE_INFINITY;
        double max = 0.5;
        double initialGuess = 1.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, initialGuess);
        double expectedResult = -1.0;
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testMaximization() throws MaxIterationsExceededException, NotStrictlyPositiveException {
        double min = 0.5;
        double max = Double.POSITIVE_INFINITY;
        double initialGuess = 1.0;
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max, initialGuess);
        double expectedResult = 1.0;
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws MaxIterationsExceededException {
        double min = 0.0;
        double max = 1.0;
        double initialGuess = 0.5;
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, initialGuess);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() throws MaxIterationsExceededException, NotStrictlyPositiveException {
        double min = 0.0;
        double max = 1.0;
        double initialGuess = 0.0;
        optimizer.setMaxIterations(Integer.MAX_VALUE);
        optimizer.setMaxEvaluations(-1);
        optimizer.setAbsoluteAccuracy(0.0);
        optimizer.setRelativeAccuracy(-1.0);
        optimizer.optimize(function, GoalType.MINIMIZE, min, max, initialGuess);
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 2;
        }
    }
}
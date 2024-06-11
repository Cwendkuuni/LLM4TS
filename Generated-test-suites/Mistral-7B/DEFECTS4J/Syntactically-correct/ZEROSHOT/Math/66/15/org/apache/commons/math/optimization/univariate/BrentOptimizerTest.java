import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);

        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizerWithException() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Test exception");
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 4.0;
        double startValue = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeParameters() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -2.0;
        double max = 0.0;
        double startValue = 1.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, goalType, min, max, startValue);
    }
}
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test with a simple function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 1.0);
        Assert.assertEquals(result, -2.0, 0.001);

        // Test with a function with multiple local minima
        UnivariateRealFunction function2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        result = optimizer.optimize(function2, GoalType.MINIMIZE, -5.0, 5.0, 0.5);
        Assert.assertEquals(result, 0.5235987755982988, 0.001);

        // Test with a function with no real root
        UnivariateRealFunction function3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        try {
            result = optimizer.optimize(function3, GoalType.MINIMIZE, -5.0, 5.0, 1.0);
            Assert.fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test with a function with a vertical asymptote
        UnivariateRealFunction function4 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new FunctionEvaluationException("Vertical asymptote");
                }
                return 1 / x;
            }
        };
        try {
            result = optimizer.optimize(function4, GoalType.MINIMIZE, -5.0, 5.0, 1.0);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // Test with a function with a horizontal asymptote
        UnivariateRealFunction function5 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x));
            }
        };
        result = optimizer.optimize(function5, GoalType.MINIMIZE, -5.0, 5.0, 1.0);
        Assert.assertEquals(result, 0.0, 0.001);
    }
}
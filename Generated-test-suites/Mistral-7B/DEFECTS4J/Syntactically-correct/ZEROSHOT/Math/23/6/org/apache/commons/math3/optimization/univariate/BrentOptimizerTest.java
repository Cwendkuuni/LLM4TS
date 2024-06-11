import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        FunctionTest functionTest = new FunctionTest();

        // Test with default values
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariateOptimizer result = optimizer.optimize(functionTest, new InitialGuess(0.5), new MaxEval(100));
        PointValuePair expected = new PointValuePair(0.23606797749979, 0.0053730177635686);
        assertEquals(expected.getValue(), result.getValue(), 0.0001);
        assertEquals(expected.getPoint(), result.getPoint(), 0.0001);

        // Test with user-defined goal type
        optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeChecker(GoalType.MAXIMIZE));
        result = optimizer.optimize(functionTest.getMaxFunction(), new InitialGuess(0.5), new MaxEval(100));
        expected = new PointValuePair(-0.23606797749979, -0.0053730177635686);
        assertEquals(expected.getValue(), result.getValue(), 0.0001);
        assertEquals(expected.getPoint(), result.getPoint(), 0.0001);

        // Test with user-defined convergence checker
        optimizer = new BrentOptimizer(0.01, 0.001, new GoalTypeConvergenceChecker(GoalType.MINIMIZE, 1e-5, 1e-5));
        result = optimizer.optimize(functionTest, new InitialGuess(0.5), new MaxEval(100));
        assertTrue(result.isConverged());
        assertNotNull(result.getValue());
        assertNotNull(result.getPoint());
    }

    private static class FunctionTest implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x - 0.23606797749979, 2) + 0.0053730177635686 * x;
        }

        public UnivariateFunction getMaxFunction() {
            return new UnivariateFunction() {
                @Override
                public double value(double x) {
                    return -value(x);
                }
            };
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }
}
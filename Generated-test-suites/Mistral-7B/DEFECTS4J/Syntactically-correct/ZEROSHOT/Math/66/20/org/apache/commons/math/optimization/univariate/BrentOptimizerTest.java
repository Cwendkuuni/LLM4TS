import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new Function("f", x -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 4);
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test minimization
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -3.0, 3.0, 1.0);
        assertEquals(result.getValue(), -1.0, 1e-6);
        assertEquals(result.getPoint(), -1.0, 1e-6);

        // Test maximization
        result = optimizer.optimize(function, GoalType.MAXIMIZE, -3.0, 3.0, 1.0);
        assertEquals(result.getValue(), 1.0, 1e-6);
        assertEquals(result.getPoint(), 1.0, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new Function("f", x -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 4);
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, -3.0, 3.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction function = new Function("f", x -> { throw new ArithmeticException("Divide by zero"); });
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(function, GoalType.MINIMIZE, -3.0, 3.0, 1.0);
    }

    private static class Function implements UnivariateRealFunction {
        private final String name;
        private final Function1D operation;

        public Function(String name, Function1D operation) {
            this.name = name;
            this.operation = operation;
        }

        @Override
        public double value(double x) {
            return operation.applyAsDouble(x);
        }

        @Override
        public String getFunctionName() {
            return name;
        }
    }

    @FunctionalInterface
    private interface Function1D {
        double applyAsDouble(double x);
    }
}
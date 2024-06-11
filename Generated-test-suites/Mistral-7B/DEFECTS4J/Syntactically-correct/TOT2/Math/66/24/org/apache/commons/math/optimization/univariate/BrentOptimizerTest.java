import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    private UnivariateOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer();
        function = new MyFunction();
    }

    @Test
    public void testMinimizeSimpleFunction() {
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        assertEquals(-2.0, result.getPoint(), 0.001);
        assertEquals(1.5e-15, result.getValue(), 0.0);
    }

    @Test
    public void testMinimizeFunctionWithInitialGuess() {
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 1.0);
        assertEquals(-2.0, result.getPoint(), 0.001);
        assertEquals(1.5e-15, result.getValue(), 0.0);
    }

    @Test
    public void testMinimizeFunctionWithBounds() {
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new SimpleBounds(-5.0, 5.0), null);
        assertEquals(-2.0, result.getPoint(), 0.001);
        assertEquals(1.5e-15, result.getValue(), 0.0);
    }

    @Test
    public void testMaximizeSimpleFunction() {
        PointValuePair result = optimizer.optimize(new MyMaxFunction(), GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(1.5e-15, result.getValue(), 0.0);
    }

    @Test
    public void testMinimizeFunctionWithNegativeStepSize() {
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, -1.0));
    }

    @Test
    public void testMinimizeFunctionWithNegativeAccuracy() {
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0, -0.1));
    }

    @Test
    public void testMinimizeFunctionWithMaxIterationsExceeded() {
        optimizer.setMaxIterations(1);
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0));
    }

    @Test
    public void testFunctionEvaluationException() {
        function = new FunctionWithException();
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 0.0));
    }

    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) - 2;
        }
    }

    private static class MyMaxFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.cos(x), 2) + 2;
        }
    }

    private static class FunctionWithException implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            if (x > 0) {
                throw new FunctionEvaluationException("Failed to evaluate function");
            }
            return Math.pow(Math.sin(x), 2) - 2;
        }
    }
}
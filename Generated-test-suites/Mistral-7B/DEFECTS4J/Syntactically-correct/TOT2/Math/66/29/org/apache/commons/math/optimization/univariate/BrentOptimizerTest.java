import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_Minimization() throws Exception {
        UnivariateRealFunction f = new Function("f", x -> Math.pow(x, 2) + Math.sin(Math.PI * x));
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        PointValuePair answer = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(answer.getPoint().getValue(), -1.5708, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_NegativeBounds() throws Exception {
        UnivariateRealFunction f = new Function("f", x -> Math.pow(x, 2));
        new BrentOptimizer().optimize(f, GoalType.MINIMIZE, -1.0, -2.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_NegativeRelativeAccuracy() throws Exception {
        UnivariateRealFunction f = new Function("f", x -> Math.pow(x, 2));
        new BrentOptimizer().optimize(f, GoalType.MINIMIZE, 0.0, 1.0, -1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizer_MaxIterationsExceeded() throws Exception {
        UnivariateRealFunction f = new Function("f", x -> Math.pow(x, 2));
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }

    @Test
    public void testBrentOptimizer_Maximization() throws Exception {
        UnivariateRealFunction f = new Function("f", x -> Math.pow(x, 2) - Math.sin(Math.PI * x));
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        PointValuePair answer = optimizer.optimize(f, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        Assert.assertEquals(answer.getPoint().getValue(), 1.5708, 1e-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentOptimizer_FunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new Function("f", x -> {
            throw new FunctionEvaluationException("Error in function evaluation.");
        });
        new BrentOptimizer().optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizer_MaxIterationsExceeded_Maximization() throws Exception {
        UnivariateRealFunction f = new Function("f", x -> Math.pow(x, 2));
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 1.0, 0.0);
    }

    private static class Function implements UnivariateRealFunction {
        private final String name;
        private final UnivariateRealFunction function;

        public Function(String name, UnivariateRealFunction function) {
            this.name = name;
            this.function = function;
        }

        @Override
        public double value(double x) {
            return function.value(x);
        }

        @Override
        public double derivative(double x) {
            return function.derivative(x);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
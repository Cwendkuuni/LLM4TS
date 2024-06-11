import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.Minimizer;
import org.apache.commons.math3.optim.NonUnaryFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test optimization of a simple function with known minimum.
        Function f = new Function("f(x) = x^2");
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, 5), new GoalType(GoalType.MINIMIZE, 1e-6, 1e-6));
        assertEquals(2.0, result.getPoint(), 1e-6);

        // Test optimization of a function with non-unique minimum.
        Function g = new Function("g(x) = x^3 - 2x^2 + x");
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        result = optimizer.optimize(g, new SimpleBounds(0, 5), new GoalType(GoalType.MINIMIZE, 1e-6, 1e-6));
        assertEquals(1.0, result.getPoint(), 1e-6);

        // Test optimization of a function with multiple local minimum.
        Function h = new Function("h(x) = x^3 - 2x^2 + x - 0.01");
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        result = optimizer.optimize(h, new SimpleBounds(-5, 5), new GoalType(GoalType.MINIMIZE, 1e-6, 1e-6));
        assertEquals(-0.499, result.getPoint(), 1e-6);

        // Test optimization of a function with a single maximum.
        Function i = new Function("i(x) = -x^2");
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        result = optimizer.optimize(i, new SimpleBounds(0, 5), new GoalType(GoalType.MAXIMIZE, 1e-6, 1e-6));
        assertEquals(0.0, result.getPoint(), 1e-6);

        // Test optimization of a function with a single maximum outside the bounds.
        Function j = new Function("j(x) = x^2");
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            result = optimizer.optimize(j, new SimpleBounds(-1, 0), new GoalType(GoalType.MAXIMIZE, 1e-6, 1e-6));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test optimization of a function with lower bound equal to the upper bound.
        Function k = new Function("k(x) = x^2");
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            result = optimizer.optimize(k, new SimpleBounds(5, 5), new GoalType(GoalType.MINIMIZE, 1e-6, 1e-6));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test optimization of a function with an initial guess outside the bounds.
        Function l = new Function("l(x) = x^2");
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            result = optimizer.optimize(l, new SimpleBounds(0, 5), new InitialGuess(10));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    private static class Function implements NonUnaryFunction {
        private String name;

        public Function(String name) {
            this.name = name;
        }

        @Override
        public double value(double x) {
            System.out.println("Computing " + name + "(" + x + ")");
            // Add your implementation here
            return x * x;
        }
    }
}
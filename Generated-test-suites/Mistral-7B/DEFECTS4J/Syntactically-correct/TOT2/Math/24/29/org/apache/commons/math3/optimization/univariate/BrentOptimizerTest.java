import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        UnivariatePointValuePair result = optimizer.optimize(f, 1.5, null);
        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(-0.25, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        UnivariateFunction f = new QuadraticFunction(-1, 1, -1);
        UnivariatePointValuePair result = optimizer.optimize(f, -1.5, null);
        assertEquals(-0.5, result.getPoint(), 0.001);
        assertEquals(0.25, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithinBounds() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        UnivariatePointValuePair result = optimizer.optimize(f, 0.5, null);
        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(-0.25, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerOutOfBounds() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        try {
            optimizer.optimize(f, -1, null);
            assert false; // Should throw an exception
        } catch (GoalTypeMismatchException e) {
            // Expected exception
        }

        try {
            optimizer.optimize(f, 2, null);
            assert false; // Should throw an exception
        } catch (GoalTypeMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001, new MyConvergenceChecker());
        UnivariateFunction f = new QuadraticFunction(-1, 1, 1);
        UnivariatePointValuePair result = optimizer.optimize(f, 1.5, null);
        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(-0.25, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithCustomFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        UnivariateFunction f = new CustomFunction();
        UnivariatePointValuePair result = optimizer.optimize(f, 0, null);
        assertEquals(1.0, result.getPoint(), 0.01);
        assertEquals(-2.0, result.getValue(), 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, -0.001, null);
        // Should throw an exception
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0, 0.001, null);
        // Should throw an exception
    }
}

class QuadraticFunction implements UnivariateFunction {
    private final double a;
    private final double b;
    private final double c;

    public QuadraticFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double value(double x) {
        return a * x * x + b * x + c;
    }
}

class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
    @Override
    public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
        return previous != null && previous.getValue() - current.getValue() < 1e-6;
    }
}

class CustomFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        if (x > 0.5) {
            return Math.exp(-Math.pow(x - 1, 2));
        } else {
            throw new UnsupportedOperationException("Function is not defined for x < 0.5");
        }
    }
}
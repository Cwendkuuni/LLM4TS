import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        try {
            new BrentOptimizer(-0.1, 0.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        try {
            new BrentOptimizer(0.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        new BrentOptimizer(1.2, 2.3);
    }

    @Test
    public void testDoOptimizeBasic() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01);
        UnivariateFunction function = new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(Math.PI * x);
            }
        };
        OptimizationProblem problem = new ScalarOptimizationProblem(function, 0.0);
        UnivariatePointValuePair result = optimizer.optimize(problem);
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeConvergenceWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01);
        OptimizationProblem problem = new ScalarOptimizationProblem(new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(Math.PI * x) + Math.pow(1e-6, 2);
            }
        }, 0.0);
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, current, best) -> iter > 100;
        UnivariatePointValuePair result = optimizer.optimize(problem, checker);
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = IllegalStateException.class)
    public void testDoOptimizeNoConvergenceWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01);
        OptimizationProblem problem = new ScalarOptimizationProblem(new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(Math.PI * x);
            }
        }, 0.0);
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, current, best) -> false;
        optimizer.optimize(problem, checker);
    }

    @Test
    public void testDoOptimizeWithLimit() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01);
        OptimizationProblem problem = new ScalarOptimizationProblem(new ScalarFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(Math.PI * x);
            }
        }, 1.0);
        UnivariatePointValuePair result = optimizer.optimize(problem);
        assertTrue(result.getPoint() >= 0.0);
        assertTrue(result.getPoint() <= 1.0);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3.0, 4.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(5.0, 6.0);
        UnivariatePointValuePair result = BrentOptimizer.this.best(a, b, true);
        assertEquals(a.getValue(), result.getValue(), 1e-6);
        UnivariatePointValuePair result2 = BrentOptimizer.this.best(b, a, false);
        assertEquals(b.getValue(), result2.getValue(), 1e-6);
        UnivariatePointValuePair result3 = BrentOptimizer.this.best(c, null, true);
        assertEquals(c.getValue(), result3.getValue(), 1e-6);
        UnivariatePointValuePair result4 = BrentOptimizer.this.best(null, b, false);
        assertEquals(b.getValue(), result4.getValue(), 1e-6);
    }
}
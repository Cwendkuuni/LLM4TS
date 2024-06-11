import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_Minimization_SimpleFunction() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
        try {
            UnivariatePointValuePair solution = optimizer.optimize(problem);
            assertEquals(0.0, solution.getPoint(), 1e-5);
            assertEquals(0.0, solution.getValue(), 1e-5);
            assertNull(optimizer.getLastPoint());
        } catch (MaxEvalException | NumberIsTooSmallException | NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_Minimization_ComplexFunction() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(Math.sin(x) * Math.pow(Math.cos(x), 2) + Math.exp(-x / 2)));
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -10.0, 10.0);
        try {
            UnivariatePointValuePair solution = optimizer.optimize(problem);
            double expectedX = -3.141592653589793;
            double expectedValue = -1.1752011936438012;
            assertEquals(expectedX, solution.getPoint(), 1e-5);
            assertEquals(expectedValue, solution.getValue(), 1e-5);
            assertNotNull(optimizer.getLastPoint());
        } catch (MaxEvalException | NumberIsTooSmallException | NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_Minimization_Error() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                throw new IllegalArgumentException("Invalid x: " + x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
        try {
            optimizer.optimize(problem);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizer_Minimization_EmptyRange() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, -1.0);
        try {
            optimizer.optimize(problem);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizer_Minimization_NullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(null, -1.0, 1.0);
        try {
            optimizer.optimize(problem);
            fail("Expected exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizer_Maximization_SimpleFunction() {
        Function f = new UnaryFunction() {
            @Override
            public double apply(double x) {
                return -Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
        try {
            UnivariatePointValuePair solution = optimizer.optimize(problem);
            assertEquals(0.0, solution.getPoint(), 1e-5);
            assertEquals(0.0, solution.getValue(), 1e-5);
            assertNull(optimizer.getLastPoint());
        } catch (MaxEvalException | NumberIsTooSmallException | NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_Maximization_ComplexFunction() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return -Math.log(Math.abs(Math.sin(x) * Math.pow(Math.cos(x), 2) + Math.exp(-x / 2)));
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -10.0, 10.0);
        try {
            UnivariatePointValuePair solution = optimizer.optimize(problem);
            double expectedX = 3.141592653589793;
            double expectedValue = 1.1752011936438012;
            assertEquals(expectedX, solution.getPoint(), 1e-5);
            assertEquals(expectedValue, solution.getValue(), 1e-5);
            assertNotNull(optimizer.getLastPoint());
        } catch (MaxEvalException | NumberIsTooSmallException | NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizer_Maximization_Error() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                throw new IllegalArgumentException("Invalid x: " + x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, 1.0);
        try {
            optimizer.optimize(problem);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizer_Maximization_EmptyRange() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.cos(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -1.0, -1.0);
        try {
            optimizer.optimize(problem);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizer_Maximization_NullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(null, -1.0, 1.0);
        try {
            optimizer.optimize(problem);
            fail("Expected exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}
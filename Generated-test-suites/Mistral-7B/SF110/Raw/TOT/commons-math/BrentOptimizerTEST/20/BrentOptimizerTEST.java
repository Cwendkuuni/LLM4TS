import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -10.0, 10.0);
        UnivariatePointValuePair solution = optimizer.optimize(problem);

        assertEquals(-2.0, solution.getPoint(), 1e-6);
        assertEquals(-12.0, solution.getValue(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativetolTooSmall() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 1e-6, null);
        // This optimization problem has no solution, but it's used to test the exception
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(new PolynomialFunction(1, 0), -10.0, 10.0);
        optimizer.optimize(problem);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsolutetolNegative() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1.0, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(new IdentityFunction(), 0.0, 1.0);
        optimizer.optimize(problem);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2) - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return Math.abs(current.getValue() - previous.getValue()) < 1e-8;
            }
        });

        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -10.0, 10.0);
        UnivariatePointValuePair solution = optimizer.optimize(problem);

        assertEquals(-2.0, solution.getPoint(), 1e-6);
        assertEquals(-12.0, solution.getValue(), 1e-6);
    }
}
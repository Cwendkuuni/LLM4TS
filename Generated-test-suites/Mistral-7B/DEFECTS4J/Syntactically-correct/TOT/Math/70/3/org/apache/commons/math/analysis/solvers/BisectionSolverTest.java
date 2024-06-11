import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver_SimpleFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };

        UnivariateRealSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(function, -2.0, 2.0);
            Assert.assertEquals(2.0, root, 1e-6);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Bisection algorithm should find a root within the given interval.");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should be possible within the given interval.");
        }
    }

    @Test
    public void testBisectionSolver_FunctionWithNoRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1.0;
            }
        };

        UnivariateRealSolver solver = new BisectionSolver();

        try {
            solver.solve(function, -1.0, 1.0);
            Assert.fail("Function with no root in the given interval should throw NoRootFoundException.");
        } catch (NoRootFoundException e) {
            // Expected exception
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Function with no root in the given interval should not throw MaxIterationsExceededException.");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should be possible within the given interval.");
        }
    }

    @Test
    public void testBisectionSolver_FunctionWithNegativeRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1.0;
            }
        };

        UnivariateRealSolver solver = new BisectionSolver();

        try {
            solver.solve(function, 1.0, -1.0);
            Assert.fail("Function with negative root in the given interval should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Function with negative root in the given interval should not throw MaxIterationsExceededException.");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should be possible within the given interval.");
        }
    }

    @Test
    public void testBisectionSolver_FunctionWithLargeRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1000.0;
            }
        };

        UnivariateRealSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(function, -1001.0, 1001.0);
            Assert.assertEquals(10.0, root, 1e-6);
        } catch (NumberIsTooLargeException e) {
            Assert.fail("Function with large root in the given interval should not throw NumberIsTooLargeException.");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should be possible within the given interval.");
        }
    }
}
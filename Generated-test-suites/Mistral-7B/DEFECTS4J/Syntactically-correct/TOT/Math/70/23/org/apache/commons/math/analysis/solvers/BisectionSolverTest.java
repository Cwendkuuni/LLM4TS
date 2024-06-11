import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionIterationException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver_SimpleCase() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -2.0, 2.0);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testBisectionSolver_FunctionThrowsException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x == 0) {
                    throw new FunctionEvaluationException("Function evaluation is not possible at x = 0");
                }
                return Math.sin(x) / x;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -1.0, 1.0);
            Assert.fail();
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testBisectionSolver_ZeroFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -1.0, 1.0);
            Assert.fail();
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testBisectionSolver_FunctionWithLimitCycles() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x) - 0.5;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, -3.0, 3.0);
        Assert.assertEquals(-1.2732395447071539d, root, 1e-6);
    }

    @Test
    public void testBisectionSolver_EdgeCases() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.abs(x));
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(function, -1.0, Double.NEGATIVE_INFINITY);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(function, Double.POSITIVE_INFINITY, 1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(function, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(function, -1.0, 0.0);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 1.0);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, -1.0, Double.NaN);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(function, Double.NaN, 1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(function, -1.0, new Double("NaN"));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(function, new Double("NaN"), 1.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
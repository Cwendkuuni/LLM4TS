import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolverConstructor() {
        BisectionSolver solver = new BisectionSolver();
        Assert.assertEquals(100, solver.getMaxIterations());
        Assert.assertEquals(1E-6, solver.getAbsoluteAccuracy(), 1E-8);
    }

    @Test
    public void testSolverFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(1.0, 2.0);
        double expected = 1.4142135625; // Approximate solution
        Assert.assertEquals(expected, root, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolverMaxIterations() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / x);
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.solve(0.1, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolverFunctionEvaluation() throws FunctionEvaluationException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                if (x < 0) {
                    throw new FunctionEvaluationException("Function not defined for x < 0");
                }
                return Math.sqrt(x);
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.solve(-1.0, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverSmallInterval() throws NumberIsTooSmallException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.solve(0.0, 1E-12);
    }

    @Test(expected = TooManyIterationsException.class)
    public void testSolverTooManyIterations() throws TooManyIterationsException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        solver.setMaxIterations(1);
        solver.solve(1.0, 2.0);
    }

    @Test
    public void testSolverScaledFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double root = solver.solve(1.5, 2.5);
        double expected = 2.0; // Approximate solution
        Assert.assertEquals(Precision.round(expected, 3), root, 1E-6);
    }

    @Test
    public void testSolverInitialGuessOutsideInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        try {
            solver.solve(-0.5, 0.5);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
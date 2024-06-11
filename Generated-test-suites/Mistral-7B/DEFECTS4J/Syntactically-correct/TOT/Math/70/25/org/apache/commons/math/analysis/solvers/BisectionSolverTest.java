import org.apache.commons.math3.analysis.BisectionSolver;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(function, -2.0, 2.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            throw new RuntimeException("Failed to find root", e);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverFunctionEvalException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, -1.0, 1.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverMaxIterationsExceededException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1 / (x - 1));
            }
        };

        BisectionSolver solver = new BisectionSolver(10);
        solver.solve(function, -2.0, 2.0);
    }

    @Test
    public void testBisectionSolverEdgeCase() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(1 / (x - 1)) - Math.exp(1 / (x + 1));
            }
        };

        BisectionSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(function, -2.0, 2.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            throw new RuntimeException("Failed to find root", e);
        }
    }
}
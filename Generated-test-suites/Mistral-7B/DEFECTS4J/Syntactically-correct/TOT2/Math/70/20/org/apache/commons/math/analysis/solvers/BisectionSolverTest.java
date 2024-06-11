import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    @Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        assertEquals(-2.0, solver.solve(f, -3.0, 3.0), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BisectionSolver solver = new BisectionSolver(10);

        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvalError() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };
        BisectionSolver solver = new BisectionSolver();

        solver.solve(f, -1.0, 1.0);
    }
}
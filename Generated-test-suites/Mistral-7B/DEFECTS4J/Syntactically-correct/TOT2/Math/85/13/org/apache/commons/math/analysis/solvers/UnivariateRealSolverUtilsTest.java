import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        double solution = UnivariateRealSolverUtils.solve(f, -1.0, 1.0);
        assertEquals(0.0, solution, 1e-6);
    }

    @Test
    public void testSolveWithException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> {
            UnivariateRealSolverUtils.solve(f, 0.0, 1.0);
        });
    }

    @Test
    public void testSolveWithAbsAccuracy() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        double solution = UnivariateRealSolverUtils.solve(f, -1.0, 1.0, 1e-10);
        assertEquals(0.0, solution, 1e-12);
    }

    @Test
    public void testSolveWithConvergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                double result = Math.sin(x) / x;
                if (Math.abs(result) < 1e-6) {
                    throw new ConvergenceException("Convergence exception");
                }
                return result;
            }
        };
        assertThrows(ConvergenceException.class, () -> {
            UnivariateRealSolverUtils.solve(f, -1.0, 1.0);
        });
    }
}
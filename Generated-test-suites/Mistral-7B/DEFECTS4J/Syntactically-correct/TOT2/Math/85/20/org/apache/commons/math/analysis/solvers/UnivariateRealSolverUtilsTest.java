import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_FunctionWithRoot_ReturnsRoot() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 1.0);
        assertEquals("Solver did not find root", 0.0, root, 1e-10);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_FunctionWithNoRoot_ThrowsConvergenceException() throws Exception {
        UnivariateRealFunction function = f -> Math.sin(f);
        UnivariateRealSolverUtils.solve(function, 0.0, 10.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionWithException_ThrowsFunctionEvaluationException() throws Exception {
        UnivariateRealFunction function = f -> { throw new FunctionEvaluationException(); };
        try {
            UnivariateRealSolverUtils.solve(function, 0.0, 1.0);
        } catch (ConvergenceException e) {
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolve_FunctionWithAbsoluteAccuracy_ReturnsAccurateRoot() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 0.01;
        double root = UnivariateRealSolverUtils.solve(function, -2.0, 1.0, 1e-5);
        assertEquals("Solver did not find accurate root", 0.0, root, 1e-8);
    }

    @Test
    public void testBracket_FunctionWithRoots_ReturnsBracketingValues() throws Exception {
        UnivariateRealFunction function = f -> Math.pow(f, 2) - 1;
        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        double a = bracketingValues[0];
        double b = bracketingValues[1];
        assertEquals("Bracketing values are not as expected", -1.0, a, 1e-10);
        assertEquals("Bracketing values are not as expected", 0.0, b, 1e-10);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_FunctionWithoutRoots_ThrowsConvergenceException() throws Exception {
        UnivariateRealFunction function = f -> Math.sin(f);
        UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_FunctionWithException_ThrowsFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction function = f -> { throw new FunctionEvaluationException(); };
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 1.0);
        } catch (ConvergenceException e) {
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expectedMidpoint = (a + b) / 2.0;
        double actualMidpoint = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals("Midpoint calculation is incorrect", expectedMidpoint, actualMidpoint, 1e-12);
    }
}
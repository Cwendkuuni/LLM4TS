import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_FunctionPositiveZero() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new FunctionThatReturnsZero();
        double result = UnivariateRealSolverUtils.solve(f, -1.0, 1.0);
        assertEquals(0.0, result, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_FunctionReturnsInfiniteValues() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new FunctionThatReturnsInfiniteValues();
        try {
            UnivariateRealSolverUtils.solve(f, -1.0, 1.0);
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionThrowsException() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new FunctionThatThrowsException();
        try {
            UnivariateRealSolverUtils.solve(f, -1.0, 1.0);
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolve_FunctionDoesNotHaveRoots() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new FunctionThatDoesNotHaveRoots();
        try {
            UnivariateRealSolverUtils.solve(f, -1.0, 1.0);
            fail("Should have thrown ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testBracket_FunctionWithRoots() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new FunctionThatReturnsZero();
        double[] bracketedValues = UnivariateRealSolverUtils.bracket(f, 0.0, -1.0, 1.0);
        double expectedLower = -1.0;
        double expectedUpper = 0.0;
        assertArrayEquals(new double[]{expectedLower, expectedUpper}, bracketedValues);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_FunctionReturnsInfiniteValues() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new FunctionThatReturnsInfiniteValues();
        try {
            UnivariateRealSolverUtils.bracket(f, -1.0, -1.0, 1.0);
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_FunctionThrowsException() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new FunctionThatThrowsException();
        try {
            UnivariateRealSolverUtils.bracket(f, -1.0, -1.0, 1.0);
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testBracket_FunctionDoesNotHaveRoots() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new FunctionThatDoesNotHaveRoots();
        try {
            UnivariateRealSolverUtils.bracket(f, -1.0, -1.0, 1.0);
            fail("Should have thrown ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    interface Function {
        double value(double x) throws FunctionEvaluationException;
    }

    static class FunctionThatReturnsZero implements Function {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            return 0.0;
        }
    }

    static class FunctionThatReturnsInfiniteValues implements Function {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("FunctionEvaluationException");
        }
    }

    static class FunctionThatThrowsException implements Function {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new RuntimeException("Unable to evaluate function");
        }
    }

    static class FunctionThatDoesNotHaveRoots implements Function {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            return Math.sin(x) * Math.cos(x);
        }
    }
}
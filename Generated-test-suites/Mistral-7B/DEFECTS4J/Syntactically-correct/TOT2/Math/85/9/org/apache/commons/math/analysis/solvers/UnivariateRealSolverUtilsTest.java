import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.LBFGSBRealSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_FunctionZero() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithRoot(1.0);
        double x0 = 0.0;
        double x1 = 2.0;
        double expected = 1.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_FunctionNotZero() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithRootNegative(1.0);
        double x0 = 0.0;
        double x1 = 2.0;
        double expected = -1.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_NoRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithNoRoot();
        double x0 = 0.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationError() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithEvaluationError();
        double x0 = 0.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_NullFunction() {
        UnivariateRealFunction f = null;
        double x0 = 0.0;
        double x1 = 2.0;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolve_AbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithRoot(1.0);
        double x0 = 0.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;
        double expected = 1.0;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(expected, result, absoluteAccuracy);
    }

    @Test
    public void testBracket_Zero() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithRoot(1.0);
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        double[] expected = {-1.2, 1.2};
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBracket_Root() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithRoot(1.0);
        double initial = 0.5;
        double lowerBound = 0.5;
        double upperBound = 1.5;
        double[] expected = {0.5, 0.5};
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBracket_NoRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithNoRoot();
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_FunctionEvaluationError() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new FunctionWithEvaluationError();
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracket_NullFunction() {
        UnivariateRealFunction f = null;
        double initial = 0.5;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetup_NullFunction() {
        UnivariateRealFunction f = null;
        UnivariateRealSolverUtils.setup(f);
    }
}

class FunctionWithRoot implements UnivariateRealFunction {
    private double root;

    public FunctionWithRoot(double root) {
        this.root = root;
    }

    @Override
    public double value(double x) {
        return x - root;
    }
}

class FunctionWithRootNegative implements UnivariateRealFunction {
    private double root;

    public FunctionWithRootNegative(double root) {
        this.root = root;
    }

    @Override
    public double value(double x) {
        return x + root;
    }
}

class FunctionWithNoRoot implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class FunctionWithEvaluationError implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new ArithmeticException("Evaluation error");
    }
}
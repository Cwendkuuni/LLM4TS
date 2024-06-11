import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefaultSolver() {
        UnivariateRealFunction function = new FunctionWithRoot(0);
        double root = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
        assertEquals("Solve failed to find root", 0.0, root, 1e-12);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction function = new FunctionWithRoot(0);
        double root = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, 1e-15);
        assertEquals("Solve failed to find root with absolute accuracy", 0.0, root, 1e-16);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveNoRootBracketing() {
        UnivariateRealFunction function = new FunctionWithNoRoots();
        UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() {
        UnivariateRealFunction function = new ThrowingFunction();
        UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealFunction function = null;
        try {
            UnivariateRealSolverUtils.solve(function, 0.0, 0.0);
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException");
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException");
        }
        UnivariateRealSolverUtils.setup(function);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction function = new FunctionWithRoots();
        double[] roots = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        assertEquals("Bracket failed to return bracketing interval", 0.0, roots[0], 1e-12);
        assertEquals("Bracket failed to return bracketing interval", 1.0, roots[1], 1e-12);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketNoRoots() {
        UnivariateRealFunction function = new FunctionWithNoRoots();
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketFunctionEvaluationException() {
        UnivariateRealFunction function = new ThrowingFunction();
        UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketNullFunction() {
        UnivariateRealFunction function = null;
        try {
            UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException");
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException");
        }
        UnivariateRealSolverUtils.setup(function);
    }

    @Test
    public void testMidpoint() {
        double a = 1.5;
        double b = 2.5;
        double expected = 2.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals("Midpoint calculation error", expected, result, 1e-12);
    }
}

class FunctionWithRoot extends UnivariateRealFunction {
    private double root;

    FunctionWithRoot(double root) {
        this.root = root;
    }

    @Override
    public double value(double x) {
        return x - root;
    }
}

class FunctionWithNoRoots extends UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.sin(Math.PI * x);
    }
}

class FunctionWithRoots extends UnivariateRealFunction {
    @Override
    public double value(double x) {
        return (x - 0.5) * (x - 1.5);
    }
}

class ThrowingFunction extends UnivariateRealFunction {
    @Override
    public double value(double x) {
        throw new FunctionEvaluationException("Function evaluation error");
    }
}
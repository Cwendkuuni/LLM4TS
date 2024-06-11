import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedBrentSolver;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.PowellSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_simpleFunction() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        double root = UnivariateRealSolverUtils.solve(f, -2.5, 2.5);
        assertEquals("Wrong root", 0.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_functionWithNoRoot() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) + 1;
        UnivariateRealSolverUtils.solve(f, -10.0, 10.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_functionWithException() throws Exception {
        UnivariateRealFunction f = x -> { throw new FunctionEvaluationException("Exception message"); };
        UnivariateRealSolverUtils.solve(f, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_functionWithInvalidEndpoints() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        UnivariateRealSolverUtils.solve(f, 2.5, 2.5);
    }

    @Test
    public void testBracket_simpleFunction() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        double[] bounds = UnivariateRealSolverUtils.bracket(f, 0.0, -3.0, 3.0);
        assertTrue("Bracketing failed", bounds[0] < bounds[1]);
        double root = UnivariateRealSolverUtils.solve(f, bounds[0], bounds[1]);
        assertEquals("Wrong root", 0.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_functionWithNoBracketing() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) + 1;
        UnivariateRealSolverUtils.bracket(f, 0.0, 0.0, 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_functionWithException() throws Exception {
        UnivariateRealFunction f = x -> { throw new FunctionEvaluationException("Exception message"); };
        UnivariateRealSolverUtils.bracket(f, 0.0, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracket_invalidEndpoints() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        UnivariateRealSolverUtils.bracket(f, 2.5, 2.5, 2.5);
    }

    @Test
    public void testBisectionSolver() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x) - x;
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -3.0, 1.0);
        assertEquals("Wrong root", 0.7498344961413936, root, 1e-6);
    }

    @Test
    public void testBrentSolver() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BrentSolver solver = new BrentSolver();
        double root = solver.solve(f, -2.5, 2.5);
        assertEquals("Wrong root", 0.0, root, 1e-6);
    }

    @Test
    public void testBracketedBrentSolver() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 1;
        BracketedBrentSolver solver = new BracketedBrentSolver();
        double[] bounds = UnivariateRealSolverUtils.bracket(f, 0.0, -3.0, 3.0);
        double root = solver.solve(f, bounds[0], bounds[1]);
        assertEquals("Wrong root", 0.0, root, 1e-6);
    }

    @Test
    public void testPowellSolver() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) + 2 * x + 1;
        PowellSolver solver = new PowellSolver();
        double root = solver.solve(f, 1.0);
        assertEquals("Wrong root", -0.5, root, 1e-6);
    }
}
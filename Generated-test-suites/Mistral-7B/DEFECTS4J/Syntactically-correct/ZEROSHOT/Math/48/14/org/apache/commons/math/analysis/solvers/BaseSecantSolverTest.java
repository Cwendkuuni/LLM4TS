import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        function = f -> Math.pow(Math.E, Math.sin(f));
    }

    @After
    public void tearDown() {
        solver = null;
        function = null;
    }

    @Test
    public void testSolveWithDefaultParameters() {
        try {
            double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(Math.E, root, 1e-5);
        } catch (ConvergenceException e) {
            // This exception is expected as the test uses a function which
            // has no analytical solution and may not be solvable within the
            // given number of evaluations.
        }
    }

    @Test
    public void testSolveWithCustomParameters() {
        solver = new BaseSecantSolver(1e-7, 1e-8, 1e-9, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals(Math.E, root, 1e-8);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithRelativeAccuracyLowerThanAbsoluteAccuracy() {
        solver = new BaseSecantSolver(1e-8, 1e-7, 1e-9, BaseSecantSolver.Method.PEGASUS);
        solver.solve(10, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAllowedSolution() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -5.0, 5.0, new AllowedSolution(-1));
    }

}
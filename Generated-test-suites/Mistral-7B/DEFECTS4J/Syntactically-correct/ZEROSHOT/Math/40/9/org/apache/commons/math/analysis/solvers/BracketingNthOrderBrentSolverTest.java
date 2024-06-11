import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBrentSolver() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);

        UnivariateFunction f = f -> Math.pow(Math.sin(f), 2);

        assertEquals(0.0, solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(0.0, solver.solve(100, f, -3.5, 3.5, AllowedSolution.ANY_SIDE), 1e-6);
        assertEquals(1.273239544735162, solver.solve(100, f, -2.0, 2.0, AllowedSolution.RIGHT_SIDE), 1e-6);
        assertEquals(-1.273239544735162, solver.solve(100, f, -2.0, 2.0, AllowedSolution.LEFT_SIDE), 1e-6);
        assertEquals(-0.7493922916051418, solver.solve(100, f, -2.5, 2.5, AllowedSolution.BELOW_SIDE), 1e-6);
        assertEquals(0.7493922916051418, solver.solve(100, f, -2.5, 2.5, AllowedSolution.ABOVE_SIDE), 1e-6);

        // Test NoBracketingException
        try {
            solver.solve(100, f, 1.0, 1.0);
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentSolverWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }
}
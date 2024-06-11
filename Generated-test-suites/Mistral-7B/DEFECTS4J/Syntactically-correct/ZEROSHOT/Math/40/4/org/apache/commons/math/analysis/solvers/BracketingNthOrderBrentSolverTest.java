import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolver() throws NumberIsTooSmallException {
        // Test constructor with default arguments
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = (x) -> Math.pow(x, 3) - 2;
        double result = solver.solve(10, function, -2.0, 1.0);
        assertEquals("Root found with default solver.", -1.0, result, 1e-6);

        // Test constructor with specified arguments
        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver(1e-8, 10);
        double result1 = solver1.solve(10, function, -2.0, 1.0);
        assertEquals("Root found with custom solver (higher precision and order).", -1.0, result1, 1e-8);

        // Test exception when order is less than 2
        try {
            new BracketingNthOrderBrentSolver(1e-8, 1);
            fail();
        } catch (NumberIsTooSmallException e) {
            assertEquals("Order must be greater or equal to 2.", e.getMessage());
        }

        // Test allowed solutions
        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver(1e-8, 10);
        double min = -2.0;
        double max = 1.0;
        double startValue = -1.0;
        AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        double result2 = solver2.solve(10, function, min, max, startValue, allowedSolution);
        assertEquals("Root found on the left side.", -1.0, result2, 1e-8);

        allowedSolution = AllowedSolution.RIGHT_SIDE;
        result2 = solver2.solve(10, function, min, max, startValue, allowedSolution);
        assertEquals("Root found on the right side.", 1.0, result2, 1e-8);

        allowedSolution = AllowedSolution.ABOVE_SIDE;
        result2 = solver2.solve(10, function, min, max, startValue, allowedSolution);
        assertTrue("Root found above the interval.", result2 > max);

        allowedSolution = AllowedSolution.BELOW_SIDE;
        result2 = solver2.solve(10, function, min, max, startValue, allowedSolution);
        assertTrue("Root found below the interval.", result2 < min);
    }
}
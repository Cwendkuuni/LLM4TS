import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolveWithDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        double result = solver.solve(10, function, -2.0, 2.0);
        assertEquals("The solver should find the root of the quadratic function x^2 - 4 = 0", 0.0, result, 1e-6);
    }

    @Test
    public void testSolveWithRelativeAndAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1e-10);

        UnivariateFunction function = f -> Math.exp(f) - 3;
        double result = solver.solve(10, function, -2.0, 2.0);
        assertEquals("The solver should find the root of the exponential function e^x - 3 = 0", 1.0862943611198906, result, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveWithMinimalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1);
        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        solver.solve(10, function, -2.0, 2.0);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        UnivariateFunction function = f -> Math.pow(f, 2) + 1;
        double result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
        assertEquals("The solver should return the root below 0", -1.0, result, 1e-6);

        result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ABOVE_SIDE);
        assertEquals("The solver should return the root above 0", 1.0, result, 1e-6);

        result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertLess("The solver should return a root less than 0", result, 0.0);

        result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
        assertGreater("The solver should return a root greater than 0", result, 0.0);

        result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals("The solver should return any root", 0.0, result, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        double startValue = 1.0;
        double result = solver.solve(10, function, -2.0, 2.0, startValue);
        assertEquals("The solver should find the root of the quadratic function x^2 - 4 = 0 starting from a value near the root", 0.0, result, 1e-6);
    }

}
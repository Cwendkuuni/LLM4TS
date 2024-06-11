import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolverWithDefaultValues() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);
        double min = -3.0;
        double max = 3.0;
        double root = Math.E / 2;

        double result = solver.solve(100, function, min, max);

        assertEquals(root, result, 1e-7);
    }

    @Test
    public void testSolverWithCustomValues() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);
        double min = -3.0;
        double max = 3.0;
        double startValue = 1.0;
        double root = Math.E / 2;

        double result = solver.solve(100, function, min, max, startValue);

        assertEquals(root, result, 1e-9);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverWithInvalidMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 0);
    }

    @Test
    public void testAllowedSolutionLeftSide() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);
        double min = -3.0;
        double max = 3.0;
        double root = Math.E / 2;

        solver.setAllowedSolution(AllowedSolution.LEFT_SIDE);
        double result = solver.solve(100, function, min, max);

        assertEquals(root, result, 1e-7);
        assertLess(result, 0);
    }

    @Test
    public void testAllowedSolutionRightSide() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);
        double min = -3.0;
        double max = 3.0;
        double root = Math.E / 2;

        solver.setAllowedSolution(AllowedSolution.RIGHT_SIDE);
        double result = solver.solve(100, function, min, max);

        assertEquals(root, result, 1e-7);
        assertGreater(result, 0);
    }

    @Test
    public void testAllowedSolutionBelowSide() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);
        double min = -3.0;
        double max = 3.0;
        double root = Math.E / 2;

        solver.setAllowedSolution(AllowedSolution.BELOW_SIDE);
        double result = solver.solve(100, function, min, max);

        assertEquals(root, result, 1e-7);
        assertLess(0, result);
    }

    @Test
    public void testAllowedSolutionAboveSide() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        UnivariateFunction function = f -> Math.pow(Math.sin(f), 2) + Math.exp(-f);
        double min = -3.0;
        double max = 3.0;
        double root = Math.E / 2;

        solver.setAllowedSolution(AllowedSolution.ABOVE_SIDE);
        double result = solver.solve(100, function, min, max);

        assertEquals(root, result, 1e-7);
        assertGreater(0, result);
    }

    private void assertGreater(double value, double other) {
        assertTrue("Expected: " + value + " > " + other, value > other);
    }

    private void assertLess(double value, double other) {
        assertTrue("Expected: " + value + " < " + other, value < other);
    }

}
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);

        // test with simple function
        UnivariateFunction function = f -> Math.pow(f, 2) - 4;
        double root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1.0e-6);

        // test with function with root very close to minimum
        function = f -> (f - 0.5) * (f - 0.5) - 0.25;
        root = solver.solve(100, function, -2.0, 0.5, AllowedSolution.LEFT_SIDE);
        assertEquals(0.5, root, 1.0e-6);

        // test with function with root very close to maximum
        function = f -> (f - 1.5) * (f - 1.5) - 2.25;
        root = solver.solve(100, function, 1.5, 2.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(1.5, root, 1.0e-6);

        // test with function with root between minimum and maximum
        function = f -> Math.sin(f);
        root = solver.solve(100, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1.0e-4);

        // test with function with multiple roots
        function = f -> (f - 1.0) * (f - 3.0) * (f - 5.0);
        try {
            solver.solve(100, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            fail("Should throw NoBracketingException");
        } catch (NoBracketingException e) {
            // expected
        }

        // test with function with negative roots
        function = f -> Math.pow(f, 2) + 4;
        root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, root, 1.0e-6);

        root = solver.solve(100, function, -2.0, 2.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, root, 1.0e-6);

        // test with function with roots at end points
        function = f -> (f - 1.0) * (f - 2.0);
        root = solver.solve(100, function, 1.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertEquals(1.0, root, 1.0e-6);

        root = solver.solve(100, function, 1.0, 2.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, root, 1.0e-6);

        // test with function with roots at start value
        function = f -> (f - 0.5) * (f - 0.5) - 0.25;
        try {
            solver.solve(100, function, 0.5, 0.5, AllowedSolution.LEFT_SIDE);
            fail("Should throw MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        root = solver.solve(100, function, 0.5, 0.5, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.5, root, 1.0e-6);

        // test with function with roots at minimum
        function = f -> (f - 0.5) * (f - 0.5) - 0.25;
        try {
            solver.solve(100, function, 0.5, 0.5, AllowedSolution.MINIMUM);
            fail("Should throw MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        root = solver.solve(100, function, 0.5, 0.5, AllowedSolution.MAXIMUM);
        assertEquals(0.5, root, 1.0e-6);
    }

}
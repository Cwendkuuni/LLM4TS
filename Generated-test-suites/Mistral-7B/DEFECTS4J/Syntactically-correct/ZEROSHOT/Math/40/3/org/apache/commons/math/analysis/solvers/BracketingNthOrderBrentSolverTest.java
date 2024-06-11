import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolver() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);

        try {
            double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 1.0e-6);
        } catch (NoBracketingException e) {
            fail("Bracketing should exist for the given function and interval.");
        }

        try {
            double root = solver.solve(100, f, 1.0, 1.0, AllowedSolution.LEFT_SIDE);
            assertEquals(-1.0, root, 1.0e-6);
        } catch (NoBracketingException e) {
            fail("Bracketing should exist for the given function and interval.");
        }

        try {
            double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(1.0, root, 1.0e-6);
        } catch (NoBracketingException e) {
            fail("Bracketing should exist for the given function and interval.");
        }

        try {
            double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
            assertEquals(-1.0, root, 1.0e-6);
        } catch (NoBracketingException e) {
            fail("Bracketing should exist for the given function and interval.");
        }

        try {
            double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
            assertEquals(1.0, root, 1.0e-6);
        } catch (NoBracketingException e) {
            fail("Bracketing should exist for the given function and interval.");
        }

        try {
            solver = new BracketingNthOrderBrentSolver(1.0e-6, 5, 1.0e-6, 1.0e-8);
            double root = solver.solve(100, f, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 1.0e-8);
        } catch (NoBracketingException e) {
            fail("Bracketing should exist for the given function and interval.");
        }

        try {
            UnivariateFunction f2 = new UnivariateFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) - 1.0;
                }
            };
            solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);
            double root = solver.solve(100, f2, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            fail("Function sin(x) - 1 does not have a definite root within [-3, 3].");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            UnivariateFunction f3 = new UnivariateFunction() {
                @Override
                public double value(double x) {
                    if (x < 0) {
                        return Math.pow(x, 2);
                    } else {
                        throw new MathIllegalArgumentException("x must be negative.");
                    }
                }
            };
            solver = new BracketingNthOrderBrentSolver(1.0e-6, 5);
            double root = solver.solve(100, f3, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            fail("Function x^2 for x < 0 should not be solvable.");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

}
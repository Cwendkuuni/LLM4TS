package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /**
     * A simple test function for testing the solver.
     */
    private static final UnivariateFunction FUNCTION = f -> Math.pow(f, 2) - 2;

    /**
     * Test solver with default settings.
     */
    @Test
    public void testDefaultSettings() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(FUNCTION, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found", 0.0, root, 1e-6);
    }

    /**
     * Test solver with specified settings.
     */
    @Test
    public void testSpecifiedSettings() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        double root = solver.solve(FUNCTION, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found", 0.0, root, 1e-9);
    }

    /**
     * Test solver with negative maximal order.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(-2);
    }

    /**
     * Test solver with function that does not bracket the root.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketingFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> (f > 0) ? 1 / f : 0;
        double root = solver.solve(function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    /**
     * Test solver with function with infinite root.
     */
    @Test
    public void testInfiniteRootFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> 1 / f;
        double root = solver.solve(function, -1.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found", Double.POSITIVE_INFINITY, root, 1.0);
    }

    /**
     * Test solver with function with several roots.
     */
    @Test
    public void testMultipleRootsFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> (f - 1) * (f - 2) * (f - 3);
        double root = solver.solve(function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root not found", 1.0, root, 1e-6);
    }

}
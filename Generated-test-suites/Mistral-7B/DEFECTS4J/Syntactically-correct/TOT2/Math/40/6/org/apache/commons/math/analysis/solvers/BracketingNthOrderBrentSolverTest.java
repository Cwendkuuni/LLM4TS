package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest extends AbstractTest {

    /**
     * Test solver with default parameters.
     */
    @Test
    public void testDefaultSolver() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 2;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double root = solver.solve(f, -3.0, 3.0);
        assertEquals("The root must be approximately -1.0", -0.9999999999999999, root, 1e-11);
    }

    /**
     * Test solver with specified parameters.
     */
    @Test
    public void testSpecifiedSolver() {
        final UnivariateFunction f = x -> Math.pow(x, 2) - 2;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-10, 10);
        final double root = solver.solve(f, -3.0, 3.0);
        assertEquals("The root must be approximately -1.0", -1.0, root, 1e-11);
    }

    /**
     * Test solver with a function that does not bracket the root.
     */
    @Test
    public void testNoBracketing() {
        final UnivariateFunction f = x -> Math.sin(x) * Math.cos(x);
        assertThrows(NoBracketingException.class, () -> {
            new BracketingNthOrderBrentSolver().solve(f, -2.0, 2.0);
        });
    }

    /**
     * Test solver with a null function.
     */
    @Test
    public void testNullFunction() {
        assertThrows(MathIllegalArgumentException.class, () -> {
            new BracketingNthOrderBrentSolver().solve(null, -1.0, 1.0);
        });
    }

    /**
     * Test solver with a function with a root outside the specified interval.
     */
    @Test
    public void testRootOutsideInterval() {
        final UnivariateFunction f = x -> (x - 3) * (x - 5);
        assertThrows(MathIllegalArgumentException.class, () -> {
            new BracketingNthOrderBrentSolver().solve(f, -2.0, 2.0);
        });
    }

    /**
     * Test solver with a function with a root exactly at the start value.
     */
    @Test
    public void testRootAtStartValue() {
        final UnivariateFunction f = x -> x * x - 2;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double root = solver.solve(f, 1.0, 1.0);
        assertEquals("The root must be exactly 1.0", 1.0, root, 1e-15);
    }

    /**
     * Test solver with a function with a root exactly at the end value.
     */
    @Test
    public void testRootAtEndValue() {
        final UnivariateFunction f = x -> x * x - 2;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double root = solver.solve(f, -1.0, 1.0);
        assertEquals("The root must be exactly 1.0", 1.0, root, 1e-15);
    }

    /**
     * Test solver with a function with a root exactly at the min value.
     */
    @Test
    public void testRootAtMinValue() {
        final UnivariateFunction f = x -> x * x - 2;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double root = solver.solve(f, Double.NEGATIVE_INFINITY, 1.0);
        assertEquals("The root must be exactly 1.0", 1.0, root, 1e-15);
    }

    /**
     * Test solver with a function with a root exactly at the max value.
     */
    @Test
    public void testRootAtMaxValue() {
        final UnivariateFunction f = x -> x * x - 2;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double root = solver.solve(f, -1.0, Double.POSITIVE_INFINITY);
        assertEquals("The root must be exactly 1.0", 1.0, root, 1e-15);
    }

}
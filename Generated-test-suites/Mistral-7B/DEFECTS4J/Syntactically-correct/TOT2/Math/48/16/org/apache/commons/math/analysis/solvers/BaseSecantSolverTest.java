package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new IllinoisSolver(1e-6, 100);
    }

    /**
     * Test solver with a simple polynomial function.
     */
    @Test
    public void testPolynomialFunction() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 4, 0});
        double root = solver.solve(function, -5.0, 5.0);
        assertEquals(3.0, root, 1e-7);
    }

    /**
     * Test solver with a function that has a root between the bounds.
     */
    @Test
    public void testRootInInterval() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, 1});
        double root = solver.solve(function, -1.5, 1.5);
        assertEquals(0.0, root, 1e-7);
    }

    /**
     * Test solver with a function that has a root outside the bounds.
     */
    @Test(expected = ConvergenceException.class)
    public void testRootOutsideInterval() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, 1});
        solver.solve(function, -5.0, -1.5);
    }

    /**
     * Test solver with a function that has multiple roots.
     */
    @Test
    public void testMultipleRoots() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1, -2, 1, -2, 1});
        double root1 = solver.solve(function, -5.0, 1.0);
        double root2 = solver.solve(function, 1.0, 5.0);
        assertEquals(1.0, root1, 1e-7);
        assertEquals(-2.0, root2, 1e-7);
    }

    /**
     * Test solver with a function that has a root with large relative error.
     */
    @Test
    public void testLargeRelativeError() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1, 1, 1e10});
        double root = solver.solve(function, -1e11, 1e11);
        assertEquals(-1e11, root, 1e-7);
    }

    /**
     * Test solver with a function that has a root with large absolute error.
     */
    @Test
    public void testLargeAbsoluteError() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1e11, 1, 1});
        double root = solver.solve(function, -1e11, 1e11);
        assertEquals(-1e11, root, 1e-7);
    }

    /**
     * Test solver with a function that has a root with large function value error.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testLargeFunctionValueError() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1, 1, Double.POSITIVE_INFINITY});
        solver.solve(function, -1.0, 1.0);
    }

}
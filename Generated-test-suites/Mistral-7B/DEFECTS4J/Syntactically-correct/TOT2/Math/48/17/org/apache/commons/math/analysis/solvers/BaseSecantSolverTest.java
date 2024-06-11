package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;

/**
 * Test cases for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Tests the solver with a simple polynomial function.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testSimplePolynomial() throws Exception {
        final UnivariateFunction f = new PolynomialFunctions.Quadratic(1, 2, 3);
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        final double root = solver.solve(f, -5.0, 5.0);
        assertEquals(1.0, root, 1e-6);
    }

    /**
     * Tests the solver with a polynomial function that has no real roots.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = ConvergenceException.class)
    public void testNoRealRoots() throws Exception {
        final UnivariateFunction f = new PolynomialFunctions.Cubic(-1, 1, -1, 1);
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Tests the solver with a polynomial function that has complex roots.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testComplexRoots() throws Exception {
        final UnivariateFunction f = new PolynomialFunctions.QuarticComplex(1, 1, 1, -1, -1);
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Tests the solver with a discontinuous function.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = MathInternalError.class)
    public void testDiscontinuousFunction() throws Exception {
        final UnivariateFunction f = new DiscontinuousFunction();
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Tests the solver with a function that does not bracket the root.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoBracketing() throws Exception {
        final UnivariateFunction f = new FunctionWithNoBracketing();
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Tests the solver with a function that throws an exception on every
     * evaluation.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testExceptionOnEveryEvaluation() throws Exception {
        final UnivariateFunction f = new FunctionWithMaxEvalExceeded();
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        solver.solve(f, -5.0, 5.0);
    }

    /**
     * Tests the solver with a function that returns NaN on every evaluation.
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNaNOnEveryEvaluation() throws Exception {
        final UnivariateFunction f = new FunctionWithNaN();
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        solver.solve(f, -5.0, 5.0);
    }
}

class FunctionWithNoBracketing implements UnivariateFunction {

    @Override
    public double value(double x) {
        if (x < 0) {
            return x * x;
        }
        return -x * x;
    }
}

class FunctionWithMaxEvalExceeded implements UnivariateFunction {

    private int count = 0;

    @Override
    public double value(double x) throws MaxEvalExceededException {
        count++;
        if (count > 10) {
            throw new MaxEvalExceededException();
        }
        return Math.sin(x);
    }
}

class FunctionWithNaN implements UnivariateFunction {

    @Override
    public double value(double x) {
        return Double.NaN;
    }

}

class DiscontinuousFunction implements UnivariateFunction {

    private double x;

    @Override
    public double value(double x) {
        if (this.x == x) {
        throw new MathInternalError();
    }
    this.x = x;
    return x * x;
}
}
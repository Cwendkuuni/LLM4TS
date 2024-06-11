package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the {@link BisectionSolver} class.
 */
public class BisectionSolverTest {

    /**
     * Test for the solver with a simple function.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -3.0, 3.0);
            Assert.assertEquals(0.0, Precision.doubleEquals(root, 1.0, 1e-6), 0.0);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Solver should have converged before maximum iterations.");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should not have thrown an exception.");
        }
    }

    /**
     * Test for the solver with a function with a singularity at the root.
     */
    @Test
    public void testSingularityAtRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x >= 0.0) {
                    return Math.log(Math.abs(x));
                }
                throw new UnsupportedOperationException("Function not defined for negative x.");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, 0.0, 1.0);
            Assert.assertEquals(0.0, Precision.doubleEquals(root, 0.0, 1e-6), 0.0);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Solver should have converged before maximum iterations.");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should not have thrown an exception.");
        }
    }

    /**
     * Test for the solver with a function with roots at the end of the interval.
     */
    @Test
    public void testRootsAtEnd() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -2.0, 2.0);
            Assert.assertEquals(0.0, Precision.doubleEquals(root, 0.5, 1e-6), 0.0);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("Solver should have converged before maximum iterations.");
        } catch (FunctionEvaluationException e) {
            Assert.fail("Function evaluation should not have thrown an exception.");
        }
    }

    /**
     * Test for the solver with a function that has no root in the interval.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testNoRootInInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) + 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -2.0, 2.0);
    }

    /**
     * Test for the solver with a function that throws an exception for all x.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFunctionThrowsException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws NumberIsTooSmallException {
                throw new UnsupportedOperationException("Not defined for this x.");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.0, 1.0);
    }
}
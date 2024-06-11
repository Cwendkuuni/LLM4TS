package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalEvaluationException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the BracketingNthOrderBrentSolver class
 */
public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1e-8, 10);
    }

    /**
     * Test for root finding in the interval [0, 1]
     */
    @Test
    public void testRootfindingInInterval() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double result = solver.solve(100, f, 0, 1);
        Assert.assertEquals(0, result, 1e-8);
    }

    /**
     * Test for root finding in the interval [-1, 1]
     */
    @Test
    public void testRootfindingInSymmetricInterval() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        double result = solver.solve(100, f, -1, 1);
        Assert.assertEquals(0, result, 1e-8);
    }

    /**
     * Test for root finding in the interval [1, Infinity)
     */
    @Test
    public void testRootfindingInInterval1ToInfinity() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - x - 1;
            }
        };
        double result = solver.solve(100, f, 1, Double.POSITIVE_INFINITY);
        Assert.assertEquals(1, result, 1e-8);
    }

    /**
     * Test for root finding in the interval [-Infinity, 1]
     */
    @Test
    public void testRootfindingInIntervalMinusInfinityTo1() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - x - 1;
            }
        };
        double result = solver.solve(100, f, Double.NEGATIVE_INFINITY, 1);
        Assert.assertEquals(-1, result, 1e-8);
    }

    /**
     * Test for root finding in the interval [0, Infinity) with a function that
     * always returns zero
     */
    @Test(expected = NoBracketingException.class)
    public void testNoRootsInInterval() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        solver.solve(100, f, 0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test for root finding in the interval [-Infinity, Infinity) with a
     * function that always returns zero
     */
    @Test(expected = NoBracketingException.class)
    public void testNoRootsInIntervalMinusInfinityToPlusInfinity() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        solver.solve(100, f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Test for root finding with a function that causes a maximum number of
     * evaluations
     */
    @Test(expected = MaxEvalEvaluationException.class)
    public void testMaxEvaluations() {
        UnivariateFunction f = new UnivariateFunction() {
            private int count = 0;

            @Override
            public double value(double x) {
                count++;
                return Math.sin(Math.PI * x) / (1 + count);
            }
        };
        solver.solve(1, f, 0, 1);
    }
}
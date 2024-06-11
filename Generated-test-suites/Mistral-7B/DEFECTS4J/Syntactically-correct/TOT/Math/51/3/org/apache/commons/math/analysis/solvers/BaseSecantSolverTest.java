package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        try {
            double root = solver.solve(100, f, -2.5, 2.5);
            Assert.assertEquals(0.0, root, 1e-8);
        } catch (MaxEvalException | NumberIsTooSmallException | TooManyIterationsException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testIllinois() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolver(
                1e-6, BaseSecantSolver.Method.ILLINOIS);
        try {
            double root = solver.solve(100, f, -3, 3);
            Assert.assertEquals(Math.PI / 2, root, 1e-5);
        } catch (MaxEvalException | NumberIsTooSmallException | TooManyIterationsException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testPegasus() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolver(
                1e-6, BaseSecantSolver.Method.PEGASUS);
        try {
            double root = solver.solve(100, f, -2, 2);
            Assert.assertEquals(-1.0, root, 1e-8);
        } catch (MaxEvalException | NumberIsTooSmallException | TooManyIterationsException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testOutOfBracketing() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolver(
                1e-6, BaseSecantSolver.Method.PEGASUS, 1e-8, 1e-8);
        try {
            solver.solve(100, f, 3, -3);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFunctionValueAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2.71828;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolver(
                1e-8, BaseSecantSolver.Method.PEGASUS, 1e-12);
        try {
            double root = solver.solve(100, f, -2, 2);
            Assert.assertEquals(0.0, root, 1e-8);
            Assert.assertTrue(Precision.isClose(solver.getFunctionValue(), 0.0, 1e-12));
        } catch (MaxEvalException | NumberIsTooSmallException | TooManyIterationsException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver.BaseSecantSolver(
                1e-6, BaseSecantSolver.Method.PEGASUS, 1e-6, 1e-6, 1e-6,
                BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        try {
            double root = solver.solve(100, f, -3, 3);
            Assert.assertEquals(-2.0, root, 1e-8);
        } catch (MaxEvalException | NumberIsTooSmallException | TooManyIterationsException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }
}
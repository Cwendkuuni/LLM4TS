package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * A simple function for testing purposes.
     */
    private static UnivariateRealFunction testFunction = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x) - x;
        }

        @Override
        public double derivative(double x) {
            return 1 - Math.cos(x);
        }
    };

    /**
     * Test the {@link BaseSecantSolver} with the default constructor and
     * {@link Method#REGULA_FALSI} method.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test
    public void testRegulaFalsi() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI);
        double root = solver.solve(20, testFunction, -3.0, 3.0);
        Assert.assertEquals(0.0, testFunction.value(root), 1e-6);
    }

    /**
     * Test the {@link BaseSecantSolver} with the constructor with given
     * relative and absolute accuracy and {@link Method#ILLINOIS} method.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test
    public void testIllinois() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.01, Method.ILLINOIS);
        double root = solver.solve(20, testFunction, -3.0, 3.0);
        Assert.assertEquals(0.0, testFunction.value(root), 1e-6);
    }

    /**
     * Test the {@link BaseSecantSolver} with the constructor with given
     * relative, absolute accuracy, and function value accuracy and
     * {@link Method#PEGASUS} method.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test
    public void testPegasus() throws ConvergenceException {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.01, 0.001, Method.PEGASUS);
        double root = solver.solve(20, testFunction, -3.0, 3.0);
        Assert.assertEquals(0.0, testFunction.value(root), 1e-6);
    }

    /**
     * Test the {@link BaseSecantSolver} with a function that has no roots.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testNoRoots() throws ConvergenceException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + x;
            }

            @Override
            public double derivative(double x) {
                return 1 + Math.cos(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI);
        solver.solve(20, testFunction, -10.0, 10.0);
    }

    /**
     * Test the {@link BaseSecantSolver} with a function that has multiple roots.
     *
     * @throws ConvergenceException If the solver fails to converge.
     */
    @Test
    public void testMultipleRoots() throws ConvergenceException {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0) - 4.0;
            }

            @Override
            public double derivative(double x) {
                return 4.0 * (x - 2.0);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.REGULA_FALSI);
        double root1 = solver.solve(20, testFunction, -5.0, 5.0);
        Assert.assertEquals(1.0, testFunction.value(root1), 1e-6);
        double root2 = solver.solve(20, testFunction, root1, 5.0);
        Assert.assertEquals(-1.0, testFunction.value(root2), 1e-6);
    }
}
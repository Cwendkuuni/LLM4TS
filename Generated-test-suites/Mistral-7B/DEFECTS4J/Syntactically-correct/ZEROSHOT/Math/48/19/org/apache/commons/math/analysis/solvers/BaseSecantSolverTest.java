package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;

public class BaseSecantSolverTest {

    private static UnivariateRealFunction function = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    };

    @org.junit.Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        try {
            double root = solver.solve(20, function, -10.0, 10.0, 0.0);
            assertEquals("Solution is not correct.", -1.0, Precision.round(root, 4));
        } catch (ConvergenceException e) {
            fail("Solver should have found root.");
        }
    }

    @org.junit.Test
    public void testIllinois() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, function);
        try {
            double root = solver.solve(20, function, -10.0, 10.0, 0.0);
            assertEquals("Solution is not correct.", -1.0, Precision.round(root, 4));
        } catch (ConvergenceException e) {
            fail("Solver should have found root.");
        }
    }

    @org.junit.Test
    public void testPegasus() {
        BaseSecantSolver solver = new PegasusSolver(1e-6, function);
        try {
            double root = solver.solve(20, function, -10.0, 10.0, 0.0);
            assertEquals("Solution is not correct.", -1.0, Precision.round(root, 4));
        } catch (ConvergenceException e) {
            fail("Solver should have found root.");
        }
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testFailedConvergence() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, function);
        double root = solver.solve(0, function, -10.0, 10.0, 0.0);
        fail("Solver should not have found root.");
    }
}
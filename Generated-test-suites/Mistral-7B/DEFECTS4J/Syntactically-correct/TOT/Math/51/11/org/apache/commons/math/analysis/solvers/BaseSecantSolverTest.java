package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.AllowedSolution;
import org.apache.commons.math3.analysis.solvers.Method;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };

        double root = solver.solve(10, f, -1.0, 1.0, 0.0);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testIllinoisSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };

        try {
            double root = solver.solve(10, f, -1.0, 1.0, 0.0);
            fail("Expected MaxEvalException to be thrown.");
        } catch (MaxEvalException e) {
            // Expected
        }

        root = solver.solve(10, f, -1.0, 1.0, 0.0, AllowedSolution.LEFT_SIDE);
        // Approximately pi / 2
        assertEquals(Math.PI / 2, root, 1e-3);
    }

    @Test
    public void testPegasusSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(Method.PEGASUS) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };

        double root = solver.solve(10, f, -1.0, 1.0, 0.0);
        assertEquals(Math.log(2.0) / Math.E, root, 1e-6);
    }
}
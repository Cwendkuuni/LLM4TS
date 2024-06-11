package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4.0;
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        double root = solver.solve(50, function, -2.0, 2.0, 0.0);
        assertEquals("Incorrect root found using Regula Falsi method", 0.0, root, 1e-7);
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4.0;
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        double root = solver.solve(50, function, -2.0, 2.0, 0.0);
        assertEquals("Incorrect root found using Illinois method", 0.0, root, 1e-7);
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4.0;
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                return super.doSolve();
            }
        };
        double root = solver.solve(50, function, -2.0, 2.0, 0.0);
        assertEquals("Incorrect root found using Pegasus method", 0.0, root, 1e-7);
    }

    @Test(expected = MaxEvalException.class)
    public void testSolve_MaxEvalException() {
        UnivariateRealFunction function = x -> { throw new RuntimeException(); };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                int counter = 0;
                while (true) {
                    counter++;
                    double x = this.min + 0.5 * (this.max - this.min);
                    if (counter > 50) {
                        throw new MaxEvalException("Maximum number of evaluations exceeded");
                    }
                    double fx = function.value(x);
                    if (Precision.abs(fx) < Precision.abs(this.getRelativeAccuracy() * this.max)) {
                        return x;
                    }
                    this.min = x;
                }
            }
        };
        solver.solve(50, function, 0.0, 1.0, 0.0);
    }
}
package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        double root = solver.solve(f, -5.0, 5.0);
        Assert.assertEquals(0.0, root, 1e-6);

        solver = new PegasusSolver(1e-6, PegasusSolver.Method.PEGASUS);
        root = solver.solve(f, -5.0, 5.0);
        Assert.assertEquals(0.0, root, 1e-6);

        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        root = solver.solve(f, -5.0, 5.0);
        Assert.assertEquals(0.0, root, 1e-6);

        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 100);
        try {
            root = solver.solve(f, -5.0, 5.0);
            Assert.fail();
        } catch (MaxEvalExceededException e) {
            // Expected
        }

        solver = new PegasusSolver(1e-6, PegasusSolver.Method.PEGASUS, 1e-12, 1e-10);
        root = solver.solve(f, -5.0, 5.0);
        Assert.assertEquals(0.0, root, 1e-6);

        solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1e-12, 1e-10);
        try {
            root = solver.solve(f, -5.0, 5.0);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithRelativeAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(0.01, IllinoisSolver.Method.ILLINOIS);
        double root = solver.solve(f, -5.0, 5.0);
        Assert.assertTrue(FastMath.abs(root - 0.44948974278317) < 0.001);

        solver = new PegasusSolver(0.01, PegasusSolver.Method.PEGASUS);
        root = solver.solve(f, -5.0, 5.0);
        Assert.assertTrue(FastMath.abs(root - 0.44948974278317) < 0.001);

        solver = new RegulaFalsiSolver(0.01, RegulaFalsiSolver.Method.REGULA_FALSI);
        root = solver.solve(f, -5.0, 5.0);
        Assert.assertTrue(FastMath.abs(root - 0.44948974278317) < 0.001);
    }

    @Test
    public void testSolverWithFunctionValueAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return FastMath.exp(x) - FastMath.E;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS,
                1e-12, 1e-10);
        double root = solver.solve(f, -5.0, 5.0);
        Assert.assertEquals(1.0, root, 1e-6);

        solver = new PegasusSolver(1e-6, PegasusSolver.Method.PEGASUS,
                1e-12, 1e-10);
        root = solver.solve(f, -5.0, 5.0);
        Assert.assertEquals(1.0, root, 1e-6);

        solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI,
                1e-12, 1e-10);
        root = solver.solve(f, -5.0, 5.0);
        Assert.assertEquals(1.0, root, 1e-6);
    }
}
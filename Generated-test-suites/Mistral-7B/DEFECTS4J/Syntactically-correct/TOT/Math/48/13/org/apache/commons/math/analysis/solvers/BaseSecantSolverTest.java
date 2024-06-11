package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BracketedException;
import org.apache.commons.math3.analysis.solvers.NDerivativeUnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalEvaluationException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     *
     * @throws BracketedException if solver encounters a problem with bracketing
     * @throws MaxEvalEvaluationException if solver reaches the maximum number of evaluations
     * @throws MathIllegalArgumentException if solver is initialized with invalid arguments
     * @throws NumberIsTooSmallException if solver encounters a number that is too small
     */
    @org.junit.Test
    public void testSolve() throws BracketedException, MaxEvalEvaluationException,
            MathIllegalArgumentException, NumberIsTooSmallException {

        // Test with the Regula Falsi method
        BaseSecantSolver<UnivariateFunction> regulaFalsiSolver = new RegulaFalsiSolver(
                0.001, new RegulaFalsiSolver.Method(RegulaFalsiSolver.Method.REGULA_FALSI));
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        double x = regulaFalsiSolver.solve(100, f, -1.0, 1.0, 0.0);
        assertEquals(Math.PI / 2, x, 1e-6);

        // Test with the Illinois method
        BaseSecantSolver<UnivariateFunction> illinoisSolver = new RegulaFalsiSolver(
                0.001, new RegulaFalsiSolver.Method(RegulaFalsiSolver.Method.ILLINOIS));
        x = illinoisSolver.solve(100, f, -1.0, 1.0, 0.0);
        assertEquals(Math.PI / 2, x, 1e-6);

        // Test with the Pegasus method
        BaseSecantSolver<UnivariateFunction> pegasusSolver = new RegulaFalsiSolver(
                0.001, new RegulaFalsiSolver.Method(RegulaFalsiSolver.Method.PEGASUS));
        x = pegasusSolver.solve(100, f, -1.0, 1.0, 0.0);
        assertEquals(Math.PI / 2, x, 1e-6);

        // Test with an incorrect number of evaluations
        assertThrows(MaxEvalEvaluationException.class, () -> {
            regulaFalsiSolver = new RegulaFalsiSolver(1, f, -1.0, 1.0, 0.0);
            regulaFalsiSolver.solve(0, f, -1.0, 1.0, 0.0);
        });

        // Test with invalid arguments
        assertThrows(MathIllegalArgumentException.class, () -> {
            regulaFalsiSolver = new RegulaFalsiSolver(0.0, null);
        });

        // Test with a function with no roots
        UnivariateFunction noRoots = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        assertThrows(BracketedException.class, () -> {
            regulaFalsiSolver = new RegulaFalsiSolver(0.001, new RegulaFalsiSolver.Method(
                    RegulaFalsiSolver.Method.REGULA_FALSI));
            regulaFalsiSolver.solve(100, noRoots, -1.0, 1.0, 0.0);
        });

        // Test with a function with only one root
        UnivariateFunction oneRoot = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        x = regulaFalsiSolver.solve(100, oneRoot, -2.0, 2.0, 0.0);
        assertEquals(0.0, x, 1e-6);

        // Test with a function with multiple roots
        UnivariateFunction multipleRoots = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 1) * (x - 2) * (x - 3);
            }
        };
        x = regulaFalsiSolver.solve(100, multipleRoots, -5.0, 5.0, 0.0);
        assertEquals(1.0, x, 1e-6);

        // Test with a function with roots very close together
        UnivariateFunction closeRoots = new NDerivativeUnivariateFunction(1.0,
                new UnivariateFunction() {
                    @Override
                    public double value(double x) {
                        return Math.cos(x) - 0.5;
                    }
                });
        x = regulaFalsiSolver.solve(100, closeRoots, -1.0, 1.0, 0.0);
        assertEquals(Math.PI, x, 1e-6);

        // Test with a function with a root at a very large value
        UnivariateFunction largeRoot = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 100;
            }
        };
        x = regulaFalsiSolver.solve(100, largeRoot, -1000.0, 1000.0, 0.0);
        assertEquals(10.0, x, 1e-6);

        // Test with a function with a root at a very small value
        UnivariateFunction smallRoot = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1e-10;
            }
        };
        assertThrows(NumberIsTooSmallException.class, () -> {
            regulaFalsiSolver = new RegulaFalsiSolver(1e-12, new RegulaFalsiSolver.Method(
                    RegulaFalsiSolver.Method.PEGASUS));
            regulaFalsiSolver.solve(100, smallRoot, -1e-15, 1e-15, 0.0);
        });

    }

}
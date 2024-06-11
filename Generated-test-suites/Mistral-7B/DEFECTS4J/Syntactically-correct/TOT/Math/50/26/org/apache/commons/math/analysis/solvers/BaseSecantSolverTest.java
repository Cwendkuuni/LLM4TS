package org.apache.commons.math.analysis.solvers.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver.BracketingStrategy;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(0.001, 0.001, BracketingStrategy.BRACKET_ONCE);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals(2.0, Precision.round(root, 2), 0.0);
    }

    @Test
    public void testIllinoisSolver() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2.0;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(new IllinoisSolver(), 0.001, 0.001);
        double root = solver.solve(f, -5.0, 5.0);
        assertEquals(Math.E / 2.0, Precision.round(root, 2), 0.0);
    }

    @Test
    public void testPegasusSolver() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(new PegasusSolver(), 0.001, 0.001);
        double root = solver.solve(f, -Math.PI, Math.PI);
        assertEquals(Math.PI / 2.0, Precision.round(root, 2), 0.0);
    }

    @Test(expected = MathRuntimeException.class)
    public void testSolverWithNonBracketingFunction() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return x * x;
                } else {
                    throw new MathRuntimeException("Function is not defined for negative values.");
                }
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(0.001, 0.001);
        assertThrows(MathRuntimeException.class, () -> solver.solve(f, -1.0, 1.0));
    }

    @Test
    public void testSecantSolver() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        UnivariateRealSolver solver = new SecantSolver(0.001, 2.0, 3.0);
        double root = solver.solve(f);
        assertEquals(2.0, Precision.round(root, 2), 0.0);
    }

    @Test
    public void testSecantSolverWithDifferentInitialGuesses() {
        UnivariateRealFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        UnivariateRealSolver solver = new SecantSolver(0.001, 1.0, 2.0);
        double root = solver.solve(f);
        assertEquals(2.0, Precision.round(root, 2), 0.0);

        solver.reset();
        solver.solve(f);
        double root2 = solver.solve(f);
        assertEquals(2.0, Precision.round(root2, 2), 0.0);
        assertEquals(root, root2, 0.0);
    }
}
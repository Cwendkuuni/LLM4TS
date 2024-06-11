package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolveRegulaFalsi() {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -2.0, 1.0,
                1.0, -1.5);
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        double root = solver.solve(f, -1.0, 1.0);
        Assert.assertEquals(0.0, Precision.abs(root - (-0.5)), 1.0e-6);
    }

    @Test
    public void testSolveIllinois() {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -2.0, 1.0,
                1.0, -1.5);
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);
        double root = solver.solve(f, -1.0, 1.0);
        Assert.assertEquals(0.0, Precision.abs(root - (-0.5)), 1.0e-6);
    }

    @Test
    public void testSolvePegasus() {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -2.0, 1.0,
                1.0, -1.5);
        BaseSecantSolver solver = new PegasusSolver(1.0e-6);
        double root = solver.solve(f, -1.0, 1.0);
        Assert.assertEquals(0.0, Precision.abs(root - (-0.5)), 1.0e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveSecant() {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -2.0, 1.0,
                1.0, -1.5);
        BaseSecantSolver solver = new BaseSecantSolver(1.0e-6, Method.SECANT);
        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEval() {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -2.0, 1.0,
                1.0, -1.5);
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        solver.solve(f, -1.0, 1.0, 1000);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        UnivariateFunction f = new PolynomialFunctions.Quartic(1.0, -2.0, 1.0,
                1.0, 1.5);
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        solver.solve(f, -1.0, 1.0);
    }
}
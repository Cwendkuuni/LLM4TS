package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_PolynomialFunction() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, -3.0, 1.0}, 0.0);
        BaseSecantSolver solver = new IllinoisSolver(1e-8, 2);
        double root = solver.solve(50, function, -5.0, 5.0, 0.0);
        Assert.assertEquals(1.0, Precision.round(root, 6));

        solver = new PegasusSolver(1e-8, 2);
        root = solver.solve(50, function, -5.0, 5.0, 0.0);
        Assert.assertEquals(1.0, Precision.round(root, 6));

        solver = new RegulaFalsiSolver(1e-8, new RegulaFalsiSolver.BracketedRealRoot());
        try {
            root = solver.solve(50, function, -5.0, 5.0, 0.0);
            Assert.fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolver_IllegalAbsoluteAccuracy() {
        new IllinoisSolver(-1.0, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolver_IllegalMaxEval() {
        new IllinoisSolver(1e-8, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_NullFunction() {
        new IllinoisSolver(1e-8, 2, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_NullMin() {
        new IllinoisSolver(1e-8, 2, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, null, Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_NullMax() {
        new IllinoisSolver(1e-8, 2, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, null, Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_NullAllowedSolution() {
        new IllinoisSolver(1e-8, 2, new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, null, null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSolver_MaxEvalExceeded() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, -2.0, 1.0, -1.0}, 0.0);
        BaseSecantSolver solver = new IllinoisSolver(1e-8, 1);
        try {
            solver.solve(1, function, -10.0, 10.0, 0.0);
            Assert.fail("Expected MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected
        }
    }
}
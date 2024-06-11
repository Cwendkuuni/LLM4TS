package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver_RegulaFalsi() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(0.001, 0.0);
        try {
            double root = solver.solve(f, -5.0, 5.0);
            Assert.assertEquals(0.0, root, 0.001);
        } catch (MaxEvalException e) {
            Assert.fail("Maximum number of evaluations exceeded.");
        }
    }

    @Test
    public void testBaseSecantSolver_Illinois() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(0.001, 0.0, 0.0);
        try {
            double root = solver.solve(f, -5.0, 5.0);
            Assert.assertEquals(0.0, root, 0.001);
        } catch (MaxEvalException e) {
            Assert.fail("Maximum number of evaluations exceeded.");
        }
    }

    @Test
    public void testBaseSecantSolver_Pegasus() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new PegasusSolver(0.001, 0.0, 0.0, 0.0);
        try {
            double root = solver.solve(f, 1.0, 2.0);
            Assert.assertEquals(Math.PI / 2.0, root, 0.001);
        } catch (MaxEvalException e) {
            Assert.fail("Maximum number of evaluations exceeded.");
        } catch (NumberIsTooLargeException e) {
            Assert.fail("Function value is too large.");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBaseSecantSolver_UnsupportedMethod() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, 0.0, new BaseSecantSolver.Method[] {BaseSecantSolver.Method.SECANT});
        try {
            solver.setMaxEvaluations(100);
            double root = solver.solve(f, 1.0, 2.0);
            Assert.fail("Should not reach this point.");
        } catch (Exception e) {
            // Expected UnsupportedOperationException
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSecantSolver_InvalidAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, 0.0, new BaseSecantSolver.Method[] {BaseSecantSolver.Method.ILLINOIS});
        try {
            solver.solve(f, -5.0, 5.0, AllowedSolution.ABOVE_SIDE);
            Assert.fail("Should not reach this point.");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testBaseSecantSolver_CustomAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, 0.0, new BaseSecantSolver.Method[] {BaseSecantSolver.Method.ILLINOIS}, AllowedSolution.LEFT_SIDE);
        try {
            double root = solver.solve(f, -5.0, 5.0);
            Assert.assertFalse(Precision.isClose(root, 0.0, 0.001));
            Assert.assertTrue(Precision.isClose(root, -2.0, 0.001));
        } catch (MaxEvalException e) {
            Assert.fail("Maximum number of evaluations exceeded.");
        }
    }

}
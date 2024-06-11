package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.analysis.solvers.BaseSecantSolver.*;
import static org.apache.commons.math3.analysis.solvers.BaseSecantSolver.Method.*;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)} with
     * default accuracy and {@link Method#REGULA_FALSI} method.
     * @throws ConvergenceException
     * @throws MathInternalError
     * @throws MaxEvalExceededException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testBaseSecantSolverRegulaFalsi() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        final double root = 2.0;
        final double tolerance = 1e-7;
        final BaseSecantSolver solver = new BaseSecantSolver(tolerance, REGULA_FALSI);
        double x = solver.solve(100, function, -3.0, 3.0);
        Assert.assertEquals(root, x, tolerance);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)} with
     * default accuracy and {@link Method#ILLINOIS} method.
     * @throws ConvergenceException
     * @throws MathInternalError
     * @throws MaxEvalExceededException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testBaseSecantSolverIllinois() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 1.0;
            }
        };
        final double root = 1.23456;
        final double tolerance = 1e-5;
        final BaseSecantSolver solver = new BaseSecantSolver(tolerance, ILLINOIS);
        double x = solver.solve(100, function, -2.0, 2.0);
        Assert.assertEquals(root, x, tolerance);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)} with
     * default accuracy and {@link Method#PEGASUS} method.
     * @throws ConvergenceException
     * @throws MathInternalError
     * @throws MaxEvalExceededException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testBaseSecantSolverPegasus() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2.71828;
            }
        };
        final double root = 0.5;
        final double tolerance = 1e-4;
        final BaseSecantSolver solver = new BaseSecantSolver(tolerance, 1.0e-6, 0.01, PEGASUS);
        double x = solver.solve(100, function, -1.0, 1.0);
        Assert.assertEquals(root, x, tolerance);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, double, Method)} with
     * default accuracy and {@link Method#ILLINOIS} method.
     * @throws ConvergenceException
     * @throws MathInternalError
     * @throws MaxEvalExceededException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testBaseSecantSolverIllinoisWithFunctionValueAccuracy() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        final double root = 2.0;
        final double tolerance = 1e-7;
        final double functionValueTolerance = 1e-8;
        final BaseSecantSolver solver = new BaseSecantSolver(tolerance, 1.0e-6, functionValueTolerance, ILLINOIS);
        double x = solver.solve(100, function, -3.0, 3.0);
        Assert.assertEquals(root, x, tolerance);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, double, Method)} with
     * custom accuracy and {@link Method#PEGASUS} method.
     * @throws ConvergenceException
     * @throws MathInternalError
     * @throws MaxEvalExceededException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testBaseSecantSolverPegasusWithCustomAccuracy() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2.71828;
            }
        };
        final double root = 0.5;
        final double tolerance = 1e-5;
        final double functionValueTolerance = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(tolerance, 1.0e-6, functionValueTolerance, PEGASUS);
        double x = solver.solve(100, function, -1.0, 1.0);
        Assert.assertEquals(root, x, tolerance);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, double, Method)} with
     * custom accuracy and {@link Method#ILLINOIS} method.
     * @throws ConvergenceException
     * @throws MathInternalError
     * @throws MaxEvalExceededException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testBaseSecantSolverIllinoisWithCustomAccuracy() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 1.0;
            }
        };
        final double root = 1.23456;
        final double tolerance = 1e-5;
        final double functionValueTolerance = 1e-6;
        final BaseSecantSolver solver = new BaseSecantSolver(tolerance, 1.0e-6, functionValueTolerance, ILLINOIS);
        double x = solver.solve(100, function, -2.0, 2.0);
        Assert.assertEquals(root, x, tolerance);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, double, Method)} with
     * custom accuracy, custom function value accuracy, and {@link Method#REGULA_FALSI} method.
     * @throws ConvergenceException
     * @throws MathInternalError
     * @throws MaxEvalExceededException
     * @throws MathIllegalArgumentException
     */
    @Test
    public void testBaseSecantSolverRegulaFalsiWithCustomAccuracy() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        final double root = 2.0;
        final double tolerance = 1e-7;
        final double functionValueTolerance = 1e-8;
        final BaseSecantSolver solver = new BaseSecantSolver(tolerance, 1.0e-6, functionValueTolerance, REGULA_FALSI);
        double x = solver.solve(100, function, -3.0, 3.0);
        Assert.assertEquals(root, x, tolerance);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double,double)} that checks bracketing condition.
     * @throws MathIllegalArgumentException
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testVerifyBracketingFails() {
        final BaseSecantSolver solver = new BaseSecantSolver(1.0);
        solver.verifyBracketing(1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double,double)} that checks bracketing condition.
     */
    @Test
    public void testVerifyBracketingSucceeds() {
        final BaseSecantSolver solver = new BaseSecantSolver(1.0);
        solver.verifyBracketing(1.0, 2.0);
    }

}
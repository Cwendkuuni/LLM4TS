package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with {@link Method#REGULA_FALSI} method.
     */
    @Test
    public void testRegulaFalsi() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, f.getValue(0.0), Method.REGULA_FALSI);
        final double root = solver.solve(10, f, -5.0, 5.0, 2.0);
        Assert.assertEquals(0.0, Precision.round(root, 7));
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with {@link Method#ILLINOIS} method and {@link AllowedSolution#LEFT_SIDE}.
     */
    @Test
    public void testIllinoisLeftSide() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) - 2.0;
            }
        };
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.ILLINOIS, AllowedSolution.LEFT_SIDE);
        final double root = solver.solve(10, f, -5.0, 5.0, 3.0);
        Assert.assertEquals(1.0, Precision.round(root, 2));
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * method with {@link Method#PEGASUS} method and {@link AllowedSolution#RIGHT_SIDE}.
     */
    @Test
    public void testPegasusRightSide() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - Math.log(2.0);
            }
        };
        final BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS, AllowedSolution.RIGHT_SIDE);
        final double root = solver.solve(10, f, 1.0, 2.0, 1.5);
        Assert.assertEquals(1.0, Precision.round(Math.exp(root), 2));
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with {@link Method#REGULA_FALSI} method and {@link AllowedSolution#BELOW_SIDE}.
     */
    @Test
    public void testRegulaFalsiBelowSide() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, f.getValue(0.0), Method.REGULA_FALSI, AllowedSolution.BELOW_SIDE);
        try {
            solver.solve(10, f, -5.0, 5.0, 3.0);
            Assert.fail("Expected PrecisionException to be thrown.");
        } catch (final PrecisionException e) {
            // Expected error
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with {@link Method#ILLINOIS} method and {@link AllowedSolution#ABOVE_SIDE}.
     */
    @Test
    public void testIllinoisAboveSide() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.ILLINOIS, AllowedSolution.ABOVE_SIDE);
        try {
            solver.solve(10, f, -5.0, 5.0, 3.0);
            Assert.fail("Expected PrecisionException to be thrown.");
        } catch (final PrecisionException e) {
            // Expected error
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with {@link Method#PEGASUS} method and {@link AllowedSolution#ABOVE_SIDE}.
     */
    @Test
    public void testPegasusAboveSide() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        try {
            solver.solve(10, f, -5.0, 5.0, 3.0);
            Assert.fail("Expected PrecisionException to be thrown.");
        } catch (final PrecisionException e) {
            // Expected error
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * method with {@link Method#REGULA_FALSI} method and {@link AllowedSolution#LEFT_SIDE}
     * and {@link MaxIterationsExceededException} exception.
     */
    @Test
    public void testRegulaFalsiMaxIterations() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(1e-10, Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        try {
            solver.solve(1, f, -5.0, 5.0, 3.0);
            Assert.fail("Expected MaxIterationsExceededException to be thrown.");
        } catch (final MaxIterationsExceededException e) {
            // Expected error
        }
    }
}
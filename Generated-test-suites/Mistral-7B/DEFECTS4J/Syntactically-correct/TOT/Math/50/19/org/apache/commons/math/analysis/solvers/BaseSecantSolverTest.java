package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestConstants;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * the {@link BaseSecantSolver#RegulaFalsi} method.
     */
    @Test
    public void testRegulaFalsi() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(10, function, -2.0, 2.0, 0.0);
        Assert.assertEquals(0.0, root, TestConstants.DOUBLE_EPSILON);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * the {@link BaseSecantSolver#Illinois} method.
     */
    @Test
    public void testIllinois() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(10, function, 0.0, 2.0 * Math.PI, 1.0);
        Assert.assertEquals(Math.PI, root, TestConstants.DOUBLE_EPSILON);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * the {@link BaseSecantSolver#Pegasus} method.
     */
    @Test
    public void testPegasus() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 6 * x + 3;
            }
        };

        BaseSecantSolver solver = new PegasusSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, function, -3.0, 3.0, 1.0);
        Assert.assertEquals(-1.0, root, TestConstants.DOUBLE_EPSILON);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * invalid inputs.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        UnivariateRealFunction function = null;
        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -2.0, 2.0, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * invalid inputs.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullMin() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        double min = null;
        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, min, 2.0, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * invalid inputs.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullMax() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        double max = null;
        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -2.0, max, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStartValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        double startValue = -1.0;
        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -2.0, 2.0, startValue);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -2.0, 2.0, 0.0, BaseSecantSolver.AllowedSolution.NULL_SIDE);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * a function that doesn't bracket the root.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -2.0, 2.0, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with a
     * function that has a singularity at the root.
     */
    @Test
    public void testSingularity() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new NumberIsTooLargeException("Function has a singularity at the root.");
                }
                return Math.log(Math.abs(x - 1));
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(10, function, -1.5, 1.5, 0.0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with
     * a function that has a root with very large absolute value.
     */
    @Test
    public void testLargeRoot() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * Math.pow(10, 10);
            }
        };

        BaseSecantSolver solver = new PegasusSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(10, function, -1.0e11, 1.0e11, 0.0);
        Assert.assertTrue(Precision.abs(root) > 1.0e10);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with a
     * function that has a very large maximum evaluation count.
     */
    @Test(expected = MaxEvalException.class)
    public void testMaxEval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(TestConstants.DOUBLE_EPSILON, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(1, function, -1.0, 1.0, 0.0);
    }
}
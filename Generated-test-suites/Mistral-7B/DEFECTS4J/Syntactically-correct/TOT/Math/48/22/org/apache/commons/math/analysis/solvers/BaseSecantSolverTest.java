package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private BaseSecantSolver<UnivariateRealFunction> solver;
    private final UnivariateRealFunction function;

    public BaseSecantSolverTest(UnivariateRealFunction function) {
        this.function = function;
    }

    @Parameterized.Parameters(name = "{index}: function={0}")
    public static Collection<Object[]> functions() {
        return Arrays.asList(new Object[][] {
                {new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        return x * x - 1;
                    }
                }},
                {new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        return Math.sin(x) - x;
                    }
                }},
                {new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        return 1 / (1 + x);
                    }
                }},
                {new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        return x * Math.exp(-x);
                    }
                }},
                {new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        return Math.sqrt(Math.abs(x)) - 1;
                    }
                }}
        });
    }

    @Before
    public void setup() {
        solver = new PegasusSolver(1e-6, function);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        new PegasusSolver(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAccuracy() {
        new PegasusSolver(-1.0, function);
    }

    @Test(expected = ConvergenceException.class)
    public void testFunctionWithNoRoots() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        final BaseSecantSolver<UnivariateRealFunction> solver = new PegasusSolver(0.01, function);
        solver.solve(10, function, -1.0, 1.0, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        solver.solve(1, function, -1.0, 1.0, 0.0);
    }

    @Test
    public void testSolveWithBracketing() {
        final double root = function.findRoot(0.01);
        final double[] bounds = {-1.0, root, 1.0};
        final double result = solver.solve(10, function, bounds[0], bounds[1], bounds[1]);
        assertEquals(root, result, 1e-8);
    }

    @Test
    public void testSolveWithNoBracketing() {
        final double root = function.findRoot(0.01);
        final double result = solver.solve(10, function, -1.0, 1.0, 0.0);
        assertEquals(root, result, 1e-8);
    }

    @Test
    public void testSolveWithBracketingAndAllowedSide() {
        final double root = function.findRoot(0.01);
        final double[] bounds = {-1.0, root, 1.0};
        final AllowedSolution allowed = AllowedSolution.LEFT_SIDE;
        final double result = solver.solve(10, function, bounds[0], bounds[1], bounds[1], allowed);
        assertEquals(root, result, 1e-8);
    }

    @Test
    public void testSolveWithBracketingAndAllowedSideBelow() {
        final double root = function.findRoot(0.01);
        final double[] bounds = {-1.0, root, 1.0};
        final AllowedSolution allowed = AllowedSolution.BELOW_SIDE;
        final double result = solver.solve(10, function, bounds[0], bounds[1], bounds[1], allowed);
        assertTrue(result < root);
    }

    @Test
    public void testSolveWithBracketingAndAllowedSideAbove() {
        final double root = function.findRoot(0.01);
        final double[] bounds = {-1.0, root, 1.0};
        final AllowedSolution allowed = AllowedSolution.ABOVE_SIDE;
        final double result = solver.solve(10, function, bounds[0], bounds[1], bounds[1], allowed);
        assertTrue(result > root);
    }

    @Test
    public void testSolveWithNoBracketingButRootInInterval() {
        final double root = function.findRoot(0.01);
        final double result = solver.solve(10, function, -1.0, 1.0, 0.0);
        assertEquals(root, result, 1e-8);
    }

    @Test
    public void testSolveWithNoBracketingButRootOutsideInterval() {
        final double root = function.findRoot(0.01);
        final double result = solver.solve(10, function, -1.0, 1.0, 0.5);
        assertFalse("Expected an exception when the root is outside the interval.",
                result >= 0.0);
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        final AllowedSolution allowed = AllowedSolution.LEFT_SIDE;
        final int maxEval = 1;
        solver = new PegasusSolver(1e-6, function);
        solver.solve(maxEval, function, -1.0, 1.0, 0.0, allowed);
    }
}
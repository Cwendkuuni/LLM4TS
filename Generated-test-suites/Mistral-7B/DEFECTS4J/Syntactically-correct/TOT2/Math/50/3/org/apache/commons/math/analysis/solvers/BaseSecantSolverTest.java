package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.ILLINOIS);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        solver = new BaseSecantSolver(0.01, Method.PEGASUS);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithAllowedSolution() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.ILLINOIS);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.LEFT_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.RIGHT_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.ABOVE_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.BELOW_SIDE), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        solver = new BaseSecantSolver(0.01, Method.PEGASUS);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.LEFT_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.RIGHT_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.ABOVE_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.BELOW_SIDE), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.LEFT_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.RIGHT_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.ABOVE_SIDE), 0.01);
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0, 0.0, AllowedSolution.BELOW_SIDE), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double)}.
     */
    @Test
    public void testSolveWithoutAllowedSolution() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.ILLINOIS);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        solver = new BaseSecantSolver(0.01, Method.PEGASUS);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        solver = new BaseSecantSolver(0.01, Method.REGULA_FALSI);
        try {
            assertEquals(-2.0, solver.solve(100, func, -3.0, 3.0), 0.01);
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidStartValue() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.ILLINOIS);
        solver.solve(100, func, -3.0, 2.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidStartValueAndAllowedSolution() {
        UnivariateRealFunction func = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(0.01, Method.ILLINOIS);
        solver.solve(100, func, -3.0, 2.0, AllowedSolution.LEFT_SIDE);
    }
}
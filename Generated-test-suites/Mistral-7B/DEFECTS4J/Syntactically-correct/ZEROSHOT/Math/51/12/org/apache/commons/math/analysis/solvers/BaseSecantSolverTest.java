package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        function = f -> Math.pow(Math.E, -x) - x;
    }

    @Test
    public void testSolve() {
        try {
            double root = solver.solve(100, function, 1.0, 3.0, 1.5);
            assertEquals(Math.E, Precision.isClose(root, 2.718281828), 1e-6);
        } catch (MaxEvalExceededException e) {
            fail("Test should have found the root within the given evaluations.");
        }
    }

    @Test
    public void testSolveNotBracketing() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        try {
            solver.solve(100, function, 3.0, 1.0, 1.5);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolveBelow() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.BELOW_SIDE);

        double root = solver.solve(100, function, 1.0, 3.0, 1.5);
        assertEquals(Math.E, Precision.isClose(root, 2.718281828), 1e-6);
    }

    @Test
    public void testSolveAbove() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.ABOVE_SIDE);

        double root = solver.solve(100, function, 1.0, 3.0, 1.5);
        assertEquals(Math.E, Precision.isClose(root, 2.718281828), 1e-6);
    }

    @Test
    public void testSolveLeft() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.LEFT_SIDE);

        double root = solver.solve(100, function, 1.0, 3.0, 1.5);
        assertEquals(Math.E, Precision.isClose(root, 2.718281828), 1e-6);
    }

    @Test
    public void testSolveRight() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, AllowedSolution.RIGHT_SIDE);

        double root = solver.solve(100, function, 1.0, 3.0, 1.5);
        assertEquals(Math.E, Precision.isClose(root, 2.718281828), 1e-6);
    }
}
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setup() {
        function = f -> Math.pow(f, 2) - 4.0;
        solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
    }

    @Test
    public void testSolveWithMinAndMax() {
        double result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-7);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithoutMinOrMax() {
        solver.solve(10, function, 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testSolveWithNullFunction() {
        solver.solve(10, null, -2.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithInvalidFunction() {
        UnivariateRealFunction invalidFunction = f -> {
            throw new IllegalArgumentException();
        };
        solver.solve(10, invalidFunction, -2.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithoutBracketing() {
        solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        solver.solve(10, function, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        double result = solver.solve(10, function, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, result, 1e-7);
    }

    @Test
    public void testSolveWithMaxEval() {
        double result = solver.solve(5, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
        assertEquals(-1.0, result, 1e-7);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSolveWithMaxEvalTooSmall() {
        solver.solve(0, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
    }
}

class AllowedSolution {
    public static final int LEFT_SIDE = 0;
    public static final int RIGHT_SIDE = 1;
    public static final int BELOW_SIDE = 2;
    public static final int ABOVE_SIDE = 3;
    public static final int ANY_SIDE = 4;
}
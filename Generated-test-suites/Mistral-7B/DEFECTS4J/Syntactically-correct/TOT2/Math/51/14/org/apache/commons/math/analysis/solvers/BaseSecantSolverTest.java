package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RootFindersException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-9;
    private static final double FUNCTION_MAX = 10.0;
    private static final double FUNCTION_MIN = -10.0;
    private UnivariateRealFunction function;
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        solver = new RegulaFalsiSolver(EPSILON);
    }

    @Test
    public void testSolve_simple() {
        double root = solver.solve(function, FUNCTION_MIN, FUNCTION_MAX);
        assertEquals(2.0, root, EPSILON);
    }

    @Test
    public void testSolve_noBracketing() {
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        solver = new RegulaFalsiSolver(EPSILON);
        try {
            solver.solve(function, 0.0, 0.0);
            fail();
        } catch (RootFindersException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_maxEval() {
        solver = new RegulaFalsiSolver(EPSILON, 100);
        try {
            solver.solve(function, FUNCTION_MIN, FUNCTION_MAX);
            fail();
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_allowedSolution() {
        solver = new RegulaFalsiSolver(EPSILON, UnivariateSolver.AllowedSolution.LEFT_SIDE);
        double root = solver.solve(function, FUNCTION_MIN, FUNCTION_MAX);
        assertTrue(Function.value(root) <= 0.0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_invalidAllowedSolution() {
        solver = new RegulaFalsiSolver(EPSILON, UnivariateSolver.AllowedSolution.INVALID);
        solver.solve(function, FUNCTION_MIN, FUNCTION_MAX);
    }

}
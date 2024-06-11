package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        this.function = new MyFunction();
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
    }

    @Test
    public void testSolve_MinMax_AnySide() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-7);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolve_MinMax_MaxEval() {
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 2);
        this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = NullArgumentException.class)
    public void testSolve_NullFunction() {
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        this.solver.solve(10, null, -10.0, 10.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_MinMax_InvalidMin() {
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        this.solver.solve(10, this.function, Double.NEGATIVE_INFINITY, 10.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_MinMax_InvalidMax() {
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        this.solver.solve(10, this.function, -10.0, Double.POSITIVE_INFINITY, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolve_MinMax_AllowedSide_Left() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.LEFT_SIDE);
        assertTrue(root <= 0.0);
    }

    @Test
    public void testSolve_MinMax_AllowedSide_Right() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.RIGHT_SIDE);
        assertTrue(root >= 0.0);
    }

    @Test
    public void testSolve_MinMax_AllowedSide_Below() {
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.BELOW_SIDE);
        assertTrue(root < 0.0);
    }

    @Test
    public void testSolve_MinMax_AllowedSide_Above() {
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ABOVE_SIDE);
        assertTrue(root > 0.0);
    }

    @Test
    public void testSolve_MinMax_NoBracketing() {
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS);
        try {
            this.solver.solve(10, new MyNoBracketingFunction(), -1.0, 1.0, AllowedSolution.ANY_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_MinMax_FunctionValueAccuracy() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE, 1e-9);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testSolve_MinMax_RelativeAccuracy() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE, 1e-4, 1e-6);
        double expected = Precision.round(0.0, 6);
        assertEquals(expected, root, 1e-6);
    }

    @Test
    public void testSolve_MinMax_AbsoluteAccuracy() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE, 1e-4, 1e-5);
        double expected = Precision.round(0.0, 5);
        assertEquals(expected, root, 1e-5);
    }

    @Test
    public void testSolve_MinMax_FunctionValueAndRelativeAccuracy() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE, 1e-9, 1e-6);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolve_MinMax_FunctionValueAndAbsoluteAccuracy() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE, 1e-9, 1e-5);
        double expected = Precision.round(0.0, 5);
        assertEquals(expected, root, 1e-5);
    }

    @Test
    public void testSolve_MinMax_FunctionValueAndBothAccuracies() {
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE, 1e-9, 1e-5, 1e-6);
        double expected = Precision.round(0.0, 6);
        assertEquals(expected, root, 1e-6);
    }

    @Test
    public void testSolve_MinMax_NoImprovement() {
        this.solver = new IllinoisSolver(1e-6, IllinoisSolver.Method.ILLINOIS, 1);
        double root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);

        double initialRoot = root;
        while (true) {
            root = this.solver.solve(10, this.function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
            assertEquals(initialRoot, root, 1e-6);
        }
    }

    // Add more test methods here as needed for other methods and configurations.

    private static class MyFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2.0;
        }

    }

    private static class MyNoBracketingFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            if (x > 0.0) {
                return -Math.sqrt(x);
            } else {
                return Math.sqrt(-x);
            }
        }

    }

}
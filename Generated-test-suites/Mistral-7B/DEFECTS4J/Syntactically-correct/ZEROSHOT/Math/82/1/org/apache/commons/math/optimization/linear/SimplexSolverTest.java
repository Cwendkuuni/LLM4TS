package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

/**
 * Test class for SimplexSolver.
 */
public class SimplexSolverTest {

    @Test
    public void testEmptyTableau() {
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            fail("Expected exception");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void testBasicExample() {
        double[][] f = {{3.0, 2.0}, {-1.0, -1.0}, {-2.0, -4.0}};
        SimplexSolver solver = new SimplexSolver();
        try {
            RealPointValuePair solution = solver.doOptimize();
            PointValuePair expectedSolution = new PointValuePair(new double[]{-0.3333333333333333, -0.6666666666666667}, 5.3333333333333335);
            assertEquals(expectedSolution, solution);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testUnboundedSolution() {
        double[][] f = {{3.0, 2.0}, {-1.0, 1.0}, {-2.0, -4.0}};
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        double[][] f = {{3.0, 2.0}, {-1.0, 1.0}, {-2.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testMaxIterationsExceeded() {
        double[][] f = {{3.0, 2.0}, {-1.0, -1.0}, {-2.0, -4.0}, {-1.0, -1.0}};
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.setMaxIterations(0);
            solver.doOptimize();
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testGetPivotColumn() {
        double[][] tableau = {{3.0, 2.0, -2.0}, {-1.0, 1.0, 1.0}, {-2.0, -1.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(new SimplexTableau(tableau));
        int expectedPivotColumn = 1;
        assertEquals(expectedPivotColumn, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        double[][] tableau = {{3.0, 2.0, -2.0}, {-1.0, 1.0, 1.0}, {-2.0, -1.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(1, new SimplexTableau(tableau));
        int expectedPivotRow = 1;
        assertEquals(expectedPivotRow, pivotRow.intValue());
    }

    @Test
    public void testIsPhase1Solved() {
        double[][] tableau = {{3.0, 2.0, -2.0}, {-1.0, 1.0, 1.0}, {-2.0, -1.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        boolean phase1Solved = solver.isPhase1Solved(new SimplexTableau(tableau));
        assertTrue(phase1Solved);
    }

    @Test
    public void testIsPhase1Solved_unbounded() {
        double[][] tableau = {{3.0, 2.0, -2.0}, {-1.0, 1.0, 1.0}, {-2.0, 1.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        boolean phase1Solved = solver.isPhase1Solved(new SimplexTableau(tableau));
        assertFalse(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        double[][] tableau = {{3.0, 2.0, -2.0, 5.0}, {-1.0, 1.0, 1.0, 1.0}, {-2.0, -1.0, 0.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        boolean optimal = solver.isOptimal(new SimplexTableau(tableau));
        assertTrue(optimal);
    }

    @Test
    public void testIsOptimal_unbounded() {
        double[][] tableau = {{3.0, 2.0, -2.0, 5.0}, {-1.0, 1.0, 1.0, 1.0}, {-2.0, -1.0, 0.0, 0.0}, {-1.0, -1.0, 1.0, 0.0}};
        SimplexSolver solver = new SimplexSolver();
        boolean optimal = solver.isOptimal(new SimplexTableau(tableau));
        assertFalse(optimal);
    }

}
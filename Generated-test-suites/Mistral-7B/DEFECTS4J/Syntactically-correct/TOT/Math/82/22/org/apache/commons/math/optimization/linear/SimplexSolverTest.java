package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealColumnVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() {
        double[][] f = {
            {2.0, 1.0, -1.0},
            {-1.0, 1.0, 2.0},
            {-1.0, -1.0, 1.0}
        };
        SimplexTableau tableau = new SimplexTableau(
            new Array2DRowRealMatrix(f),
            new Object[]{},
            GoalType.MINIMIZE,
            true,
            1.0e-6
        );

        SimplexSolver solver = new SimplexSolver(1.0e-6);
        RealPointValuePair solution = null;
        try {
            solution = solver.doOptimize();
        } catch (OptimizationException e) {
            e.printStackTrace();
            fail("Solver threw an exception: " + e.getMessage());
        }

        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{{-1.0 / 3.0, 2.0 / 3.0}}).getColumnVector(0);
        RealVector actualSolution = solution.getPoint();

        assertEquals("Incorrect solution.", expectedSolution, actualSolution);
        assertEquals("Objective function value not as expected.", -3.0, solution.getValue(), 1.0e-6);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testUnboundedSolution() {
        double[][] f = {
            {2.0, 1.0, -1.0},
            {-1.0, 1.0, 2.0},
            {-1.0, -1.0, 1.0},
            {0.0, 0.0, 1.0}
        };
        SimplexTableau tableau = new SimplexTableau(
            new Array2DRowRealMatrix(f),
            new Object[]{},
            GoalType.MINIMIZE,
            true,
            1.0e-6
        );

        SimplexSolver solver = new SimplexSolver(1.0e-6);
        solver.doOptimize();
    }

    @Test(expected = NoSolutionException.class)
    public void testNoFeasibleSolution() {
        double[][] f = {
            {2.0, 1.0, -1.0},
            {-1.0, 1.0, 2.0},
            {-1.0, -1.0, 1.0},
            {0.0, 1.0, 1.0}
        };
        SimplexTableau tableau = new SimplexTableau(
            new Array2DRowRealMatrix(f),
            new Object[]{},
            GoalType.MINIMIZE,
            true,
            1.0e-6
        );

        SimplexSolver solver = new SimplexSolver(1.0e-6);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testMaxIterations() {
        double[][] f = {
            {2.0, 1.0, -1.0},
            {-1.0, 1.0, 2.0},
            {-1.0, -1.0, 1.0},
            {0.0, 0.0, 1.0},
            {0.0, 0.0, 1.0}
        };
        SimplexTableau tableau = new SimplexTableau(
            new Array2DRowRealMatrix(f),
            new Object[]{},
            GoalType.MINIMIZE,
            true,
            1.0e-6,
            100
        );

        SimplexSolver solver = new SimplexSolver(1.0e-6, 50);
        try {
            solver.doOptimize();
            fail("Solver did not throw an exception.");
        } catch (MaxEvalExceededException e) {
            // Expected result
        }
    }
}
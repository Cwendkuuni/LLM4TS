package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testOptimizeSimpleExample() {
        double[][] f = {{-1.0, -2.0, 3.0}, {-3.0, 2.0, 1.0}, {1.0, 1.0, 1.0}};
        double[][] constraints = {{-1.0, 1.0, 1.0}, {-2.0, 2.0, 1.0}, {1.0, -1.0, 1.0}};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        RealPointValuePair solution = solver.doOptimize();
        assertNotNull(solution);
        assertEquals(-3.0, solution.getPoint()[0], 0.001);
        assertEquals(-5.0, solution.getValue(), 0.001);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testOptimizeUnboundedSolution() {
        double[][] f = {{-1.0, -2.0}, {-3.0, 2.0}, {1.0, 1.0}};
        double[][] constraints = {{0.0, 1.0}, {-2.0, 2.0}, {1.0, -1.0}};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testOptimizeNoFeasibleSolution() {
        double[][] f = {{-1.0, -2.0}, {-3.0, 2.0}, {1.0, 1.0}};
        double[][] constraints = {{1.0, 1.0}, {-2.0, 2.0}, {1.0, -1.0}};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        solver.doOptimize();
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterations() {
        double[][] f = {{-1.0, -2.0}, {-3.0, 2.0}, {1.0, 1.0}};
        double[][] constraints = {{0.0, 1.0}, {-2.0, 2.0}, {1.0, -1.0}};
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        SimplexSolver solverWithMaxIterations = new SimplexSolver(0.0);
        RealPointValuePair solution = solverWithMaxIterations.doOptimize();
    }
}
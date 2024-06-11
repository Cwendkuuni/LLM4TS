package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test method for {@link SimplexSolver#SimplexSolver()} and {@link SimplexSolver#SimplexSolver(double, int)}.
     */
    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver solverWithArgs = new SimplexSolver(1e-7, 5);
        assertEquals(1e-6, solver.epsilon, 0.0);
        assertEquals(10, solverWithArgs.maxUlps);
        assertEquals(1e-7, solverWithArgs.epsilon, 0.0);
    }

    /**
     * Test method for {@link SimplexSolver#getPivotColumn(SimplexTableau)} and {@link SimplexSolver#getPivotRow(SimplexTableau, int)}.
     */
    @Test
    public void testGetPivotColumnAndRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}}, 0),
                                                       new LinearConstraint[]{},
                                                       GoalType.MINIMIZE,
                                                       true,
                                                       1e-6,
                                                       10);
        Integer pivotCol = new SimplexSolver().getPivotColumn(tableau);
        Integer pivotRow = new SimplexSolver().getPivotRow(tableau, pivotCol.intValue());
        assertEquals(1, pivotCol.intValue());
        assertEquals(0, pivotRow.intValue());
    }

    /**
     * Test method for {@link SimplexSolver#doIteration(SimplexTableau)}.
     */
    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}}, 0),
                                                       new LinearConstraint[]{},
                                                       GoalType.MINIMIZE,
                                                       true,
                                                       1e-6,
                                                       10);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        double[][] matrix = tableau.getMatrix();
        assertEquals(1.0, Precision.round(matrix[0][0], 15), 0.0);
        assertEquals(0.0, Precision.round(matrix[0][1], 15), 0.0);
        assertEquals(0.0, Precision.round(matrix[1][0], 15), 0.0);
        assertEquals(-1.0 / 3.0, Precision.round(matrix[1][1], 15), 0.0);
    }

    /**
     * Test method for {@link SimplexSolver#solvePhase1(SimplexTableau)}.
     */
    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}}, 0),
                                                       new LinearConstraint[]{},
                                                       GoalType.MINIMIZE,
                                                       true,
                                                       1e-6,
                                                       10);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}}, 0),
                                                       new LinearConstraint[]{},
                                                       GoalType.MINIMIZE,
                                                       true,
                                                       1e-6,
                                                       10);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
            solver.dropPhase1Objective();
            PointValuePair solution = solver.doOptimize();
            assertEquals(-3.0, solution.getPoint()[0], 0.0);
            assertEquals(-3.0, solution.getValue(), 0.0);
        } catch (UnboundedSolutionException | MaxCountExceededException | NoFeasibleSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link SimplexSolver#solve(LinearObjectiveFunction, LinearConstraint[], GoalType, boolean[], double, int)}
     * with a feasible solution.
     */
    @Test
    public void testSolveWithFeasibleSolution() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new LinearExpression(new double[][]{{1.0}, {1.0}}, new double[]{3.0}), Relation.LEQ),
            new LinearConstraint(new LinearExpression(new double[][]{{1.0}, {2.0}}, new double[]{4.0}), Relation.LEQ)};
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints, GoalType.MINIMIZE, true, 1e-6, 10);
        assertEquals(-3.0, solution.getPoint()[0], 0.0);
        assertEquals(-3.0, solution.getValue(), 0.0);
    }

    /**
     * Test method for {@link SimplexSolver#solve(LinearObjectiveFunction, LinearConstraint[], GoalType, boolean[], double, int)}
     * with an unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testSolveWithUnboundedSolution() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new LinearExpression(new double[][]{{1.0}, {1.0}}, new double[]{3.0}), Relation.LEQ),
            new LinearConstraint(new LinearExpression(new double[][]{{1.0}, {2.0}}, new double[]{4.0}), Relation.LEQ),
            new LinearConstraint(new LinearExpression(new double[][]{{1.0}}, new double[]{0.0}), Relation.LEQ)};
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objective, constraints, GoalType.MINIMIZE, true, 1e-6, 10);
    }

    /**
     * Test method for {@link SimplexSolver#solve(LinearObjectiveFunction, LinearConstraint[], GoalType, boolean[], double, int)}
     * with a no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolveWithNoFeasibleSolution() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new LinearExpression(new double[][]{{1.0}, {1.0}}, new double[]{3.0}), Relation.LT),
            new LinearConstraint(new LinearExpression(new double[][]{{1.0}, {2.0}}, new double[]{4.0}), Relation.LT)};
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objective, constraints, GoalType.MINIMIZE, true, 1e-6, 10);
    }
}
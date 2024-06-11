package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Test;

/**
 * Test class for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test for {@link SimplexSolver#SimplexSolver()} constructor.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.getEpsilon(), 0);
        assertEquals(10, solver.getMaxUlps());
    }

    /**
     * Test for {@link SimplexSolver#SimplexSolver(double, int)} constructor.
     */
    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(1.0e-8, 5);
        assertEquals(1.0e-8, solver.getEpsilon(), 0);
        assertEquals(5, solver.getMaxUlps());
    }

    /**
     * Test for {@link SimplexSolver#getPivotColumn(SimplexTableau)} method.
     */
    @Test
    public void testGetPivotColumn() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[][]{{1.0, 2.0}}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}}, Relationship.LEQ, 3.0),
            new LinearConstraint(new double[][]{{2.0, 2.0}}, Relationship.LEQ, 4.0)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(func, constraints, GoalType.MINIMIZE, null, solver.epsilon, solver.maxUlps);
        int pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    /**
     * Test for {@link SimplexSolver#getPivotRow(SimplexTableau, int)} method.
     */
    @Test
    public void testGetPivotRow() throws UnboundedSolutionException {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[][]{{1.0, 2.0}}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}}, Relationship.LEQ, 3.0),
            new LinearConstraint(new double[][]{{2.0, 2.0}}, Relationship.LEQ, 4.0)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(func, constraints, GoalType.MINIMIZE, null, solver.epsilon, solver.maxUlps);
        int pivotColumn = solver.getPivotColumn(tableau);
        int pivotRow = solver.getPivotRow(tableau, pivotColumn.intValue());
        assertEquals(0, pivotRow.intValue());
    }

    /**
     * Test for {@link SimplexSolver#doIteration(SimplexTableau)} method.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[][]{{1.0, 2.0}}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}}, Relationship.LEQ, Double.POSITIVE_INFINITY)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(func, constraints, GoalType.MINIMIZE, null, solver.epsilon, solver.maxUlps);
        solver.doIteration(tableau);
    }

    /**
     * Test for {@link SimplexSolver#doIteration(SimplexTableau)} method.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testDoIterationMaxCountExceeded() throws MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[][]{{1.0, 2.0}}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}}, Relationship.LEQ, 3.0),
            new LinearConstraint(new double[][]{{2.0, 2.0}}, Relationship.LEQ, 4.0)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(func, constraints, GoalType.MINIMIZE, null, solver.epsilon, solver.maxUlps);
        for (int i = 0; i < 1000; i++) {
            solver.doIteration(tableau);
        }
    }

    /**
     * Test for {@link SimplexSolver#solvePhase1(SimplexTableau)} method.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[][]{{1.0, 2.0}}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}}, Relationship.LEQ, 3.0),
            new LinearConstraint(new double[][]{{2.0, 2.0}}, Relationship.LEQ, Double.POSITIVE_INFINITY)
        };
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(func, constraints, GoalType.MINIMIZE, null, solver.epsilon, solver.maxUlps);
        solver.solvePhase1(tableau);
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()} method.
     */
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, 4.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[][]{{1.0, 2.0}}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}}, Relationship.LEQ, 3.0),
            new LinearConstraint(new double[][]{{2.0, 2.0}}, Relationship.LEQ, 4.0)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        assertEquals(4.0, result.getValue(), 0.0);
        assertEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.0);
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()} method with error handling.
     */
    @Test
    public void testDoOptimizeErrorHandling() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}}, Relationship.LEQ, 0.0),
            new LinearConstraint(new double[][]{{1.0, 2.0}}, Relationship.LEQ, 2.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}}, Relationship.LEQ, 3.0),
            new LinearConstraint(new double[][]{{2.0, 2.0}}, Relationship.LEQ, Double.POSITIVE_INFINITY)
        };
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            assertThrows(UnboundedSolutionException.class, () -> solver.doIteration(new SimplexTableau(func, constraints, GoalType.MINIMIZE, null, solver.epsilon, solver.maxUlps)));
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }
}
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraints;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testCustomEpsilonConstructor() {
        SimplexSolver solver = new SimplexSolver(1.0e-5);
        assertEquals(1.0e-5, solver.epsilon, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPivotColumnWithEmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1}), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        tableau.getNumObjectiveFunctions();
        solver.getPivotColumn(tableau);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPivotRowWithEmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1}), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        tableau.getNumObjectiveFunctions();
        solver.getPivotRow(0, tableau);
    }

    @Test
    public void testGetPivotColumn() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexTableau tableau = new SimplexTableau(objFunc, constraints);
        SimplexSolver solver = new SimplexSolver();
        int pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(1, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexTableau tableau = new SimplexTableau(objFunc, constraints);
        SimplexSolver solver = new SimplexSolver();
        int pivotRow = solver.getPivotRow(1, tableau);
        assertEquals(1, pivotRow);
    }

    @Test
    public void testDoIterationWithEmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1}), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doIteration(tableau);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexTableau tableau = new SimplexTableau(objFunc, constraints);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexTableau tableau = new SimplexTableau(objFunc, constraints);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        int pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(1, pivotColumn);
        int pivotRow = solver.getPivotRow(pivotColumn, tableau);
        assertEquals(1, pivotRow);
    }

    @Test
    public void testIsPhase1SolvedWithEmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1}), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        assertFalse(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsPhase1Solved() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexTableau tableau = new SimplexTableau(objFunc, constraints);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimalWithEmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1}), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        assertFalse(solver.isOptimal(tableau));
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexTableau tableau = new SimplexTableau(objFunc, constraints);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1WithEmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1}), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
            fail("Expected OptimizationException");
        } catch (OptimizationException e1) {
            try {
                solver.solvePhase1(tableau);
                fail("Expected OptimizationException");
            } catch (OptimizationException e2) {
                // expected
            }
        }
    }

    @Test
    public void testSolvePhase1() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexTableau tableau = new SimplexTableau(objFunc, constraints);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            fail("Unexpected OptimizationException");
        }
        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testOptimize() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1, 2}, new double[]{-1, 1});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();
        PointValuePair expectedSolution = new PointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, -1.0 / 3.0);
        assertEquals(expectedSolution.getPoint(), solution.getPoint(), 0.0);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 0.0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testOptimizeWithNoFeasibleSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraints constraints = new LinearConstraints(new double[][]{{1, 1}, {1, 2}}, new double[]{1, 2});
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(new SimplexTableau(objFunc, constraints));
        solver.discardArtificialVariables();
        try {
            solver.doIteration(new SimplexTableau(objFunc, constraints));
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }
}
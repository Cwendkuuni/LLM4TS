import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testConstruction() {
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver solverWithEpsilon = new SimplexSolver(0.001);
        assertEquals(0.0, solver.getEpsilon(), 0.0);
        assertEquals(0.001, solverWithEpsilon.getEpsilon(), 0.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new SimpleOptimizationFunction(x -> Math.pow(x, 2))),
                new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1e-6);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertNotNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new SimpleOptimizationFunction(x -> Math.pow(x, 2))),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2), new LinearConstraint(-1, 2, 3)}, LinearObjectiveFunction.MINIMIZE, false, 1e-6);
        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertNotNull(pivotRow);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new SimpleOptimizationFunction(x -> Math.pow(x, 2))),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2), new LinearConstraint(-1, 2, 3)}, LinearObjectiveFunction.MINIMIZE, false, 1e-6);
        new SimplexSolver().doIteration(tableau);
        double[][] expectedTableau = {
                {1.000000000000002, 0.0, -1.000000000000001, 2.0, 0.0},
                {0.0, 1.0, 1.0, 3.0, -2.0},
                {0.0, 0.0, 0.0, 0.0, -3.0},
                {0.0, 0.0, 0.0, 0.0, 0.0}
        };
        assertArrayEquals(expectedTableau, tableau.getTableau(), 0.0);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new SimpleOptimizationFunction(x -> Math.pow(x, 2))),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2), new LinearConstraint(-1, 2, 3)}, LinearObjectiveFunction.MINIMIZE, false, 1e-6);
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(0, 2, -1.1);
        assertFalse(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new SimpleOptimizationFunction(x -> Math.pow(x, 2))),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2), new LinearConstraint(-1, 2, 3)}, LinearObjectiveFunction.MINIMIZE, false, 1e-6);
        tableau.setEntry(0, 2, 0.0);
        assertFalse(new SimplexSolver().isOptimal(tableau));

        tableau.setEntry(0, 2, 3.0);
        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new SimpleOptimizationFunction(x -> Math.pow(x, 2))),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2), new LinearConstraint(-1, 2, 3)}, LinearObjectiveFunction.MINIMIZE, false, 1e-6);
        tableau.setEntry(0, tableau.getWidth() - 1, 1.0);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1WithMaxIterationsExceeded() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new SimpleOptimizationFunction(x -> Math.pow(x, 2))),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2), new LinearConstraint(-1, 2, 3)}, LinearObjectiveFunction.MINIMIZE, false, 1e-6);
        tableau.setEntry(0, tableau.getWidth() - 1, 1.0);
        tableau.setEntry(1, tableau.getWidth() - 1, 1.0);
        new SimplexSolver(1).solvePhase1(tableau);
    }

    @Test
    public void testOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new SimpleOptimizationFunction(x -> Math.pow(x, 2))),
                new LinearConstraint[]{new LinearConstraint(1, 1, 2), new LinearConstraint(-1, 2, 3)}, LinearObjectiveFunction.MINIMIZE, false, 1e-6);
        tableau.setEntry(0, tableau.getWidth() - 1, 1.0);
        LinearPointValuePair result = new SimplexSolver().doOptimize();
        PointValuePair expectedResult = new PointValuePair(1.0, 3.0);
        assertEquals(expectedResult, result);
    }

    private void assertArrayEquals(double[][] expected, double[][] actual, double epsilon) {
        for (int i = 0; i < actual.length; i++) {
            for (int j = 0; j < actual[i].length; j++) {
                assertEquals(expected[i][j], actual[i][j], epsilon);
            }
        }
    }
}
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);
        assertEquals(10, solver.maxUlps);
    }

    /**
     * Test constructor with specified settings.
     */
    @Test
    public void testSpecifiedConstructor() {
        double epsilon = 1.0e-8;
        int maxUlps = 5;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        assertEquals(epsilon, solver.epsilon, 0.0);
        assertEquals(maxUlps, solver.maxUlps);
    }

    /**
     * Test getPivotColumn method.
     */
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{0.0, 1.0}, 1.0), new LinearConstraint(new double[]{1.0, 1.0}, 2.0)},
                GoalType.MINIMIZE, true, 1.0e-6, 10);

        Integer expected = 0;
        Integer actual = solver.getPivotColumn(tableau);
        assertEquals(expected, actual);
    }

    /**
     * Test getPivotRow method with no tie in minimum ratio test.
     */
    @Test
    public void testGetPivotRowNoTie() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{0.0, 1.0}, 1.0), new LinearConstraint(new double[]{1.0, 1.0}, 2.0)},
                GoalType.MINIMIZE, true, 1.0e-6, 10);

        Integer expected = 1;
        Integer actual = solver.getPivotRow(tableau, 0);
        assertEquals(expected, actual);
    }

    /**
     * Test getPivotRow method with tie in minimum ratio test.
     */
    @Test
    public void testGetPivotRowTie() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{0.0, 1.0}, 1.0), new LinearConstraint(new double[]{1.0, 1.0}, 2.0)},
                GoalType.MINIMIZE, true, 1.0e-6, 10);

        Integer expected = 2;
        Integer actual = solver.getPivotRow(tableau, 0);
        assertEquals(expected, actual);
    }

    /**
     * Test doIteration method.
     */
    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{0.0, 1.0}, 1.0), new LinearConstraint(new double[]{1.0, 1.0}, 2.0)},
                GoalType.MINIMIZE, true, 1.0e-6, 10);

        Integer pivotCol = 0;
        Integer pivotRow = solver.getPivotRow(tableau, pivotCol);
        double pivotVal = tableau.getEntry(pivotRow, pivotCol);

        solver.doIteration(tableau);

        double newPivotVal = tableau.getEntry(pivotRow, pivotCol);
        assertEquals(pivotVal / (1.0 + epsilon), newPivotVal, maxUlps);

        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow) {
                double newEntry = tableau.getEntry(i, pivotCol);
                double oldEntry = tableau.getEntry(i, pivotCol) - pivotVal * tableau.getEntry(i, pivotCol) / pivotVal;
                assertEquals(oldEntry, newEntry, maxUlps);
            }
        }
    }

    /**
     * Test solvePhase1 method with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{Double.POSITIVE_INFINITY}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{0.0, 1.0}, 1.0), new LinearConstraint(new double[]{1.0, 1.0}, 0.0)},
                GoalType.MINIMIZE, true, 1.0e-6, 10);
        solver.solvePhase1(tableau);
    }

    /**
     * Test solvePhase1 method with unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1UnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 0.0, Double.POSITIVE_INFINITY, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{0.0, 1.0}, 1.0), new LinearConstraint(new double[]{1.0, 1.0}, 2.0)},
                GoalType.MINIMIZE, true, 1.0e-6, 10);
        solver.solvePhase1(tableau);
    }

    /**
     * Test solvePhase1 method with optimal solution.
     */
    @Test
    public void testSolvePhase1OptimalSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0, 1.0, 2.0}, 0),
                new LinearConstraint[]{new LinearConstraint(new double[]{0.0, 1.0}, 1.0), new LinearConstraint(new double[]{1.0, 1.0}, 2.0)},
                GoalType.MINIMIZE, true, 1.0e-6, 10);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();

        PointValuePair solution = solver.doOptimize();
        assertEquals(-1.0, solution.getValue(), maxUlps);
        assertEquals(new double[]{1.0 / (1.0 + epsilon), 0.0, 0.0, 1.0 / (1.0 + epsilon)}, solution.getPoint(), maxUlps);
    }
}
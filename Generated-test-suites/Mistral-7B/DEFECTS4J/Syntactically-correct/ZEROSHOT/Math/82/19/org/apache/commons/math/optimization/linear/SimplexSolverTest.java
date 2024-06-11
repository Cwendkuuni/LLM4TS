import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
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
        SimplexSolver solver = new SimplexSolver(0.001);
        assertEquals(0.001, solver.epsilon, 0.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        Integer pivotRow = new SimplexSolver().getPivotRow(1, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().getPivotRow(0, tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().doIteration(tableau);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 0), 1.0e-6);
        assertEquals(2.0 / 3.0, tableau.getEntry(1, 1), 1.0e-6);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 2), 1.0e-6);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        assertFalse(new SimplexSolver().isPhase1Solved(tableau));
        new SimplexSolver().doIteration(tableau);
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        assertFalse(new SimplexSolver().isOptimal(tableau));
        new SimplexSolver().doIteration(tableau);
        new SimplexSolver().doIteration(tableau);
        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().solvePhase1(tableau);
        assertEquals(0.0, tableau.getEntry(0, 0), 1.0e-6);
        assertEquals(0.0, tableau.getEntry(0, 1), 1.0e-6);
        assertEquals(1.0 / 3.0, tableau.getEntry(0, 2), 1.0e-6);
        assertEquals(2.0 / 3.0, tableau.getEntry(1, 0), 1.0e-6);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 1), 1.0e-6);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 2), 1.0e-6);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, null),
                new LinearConstraintSet(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, 2.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, 3.0),
                        new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY)
                }), GoalType.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().solvePhase1(tableau);
        tableau.discardArtificialVariables();
        RealPointValuePair result = new SimplexSolver().doOptimize();
        PointValuePair expectedResult = new PointValuePair(new double[]{2.0 / 3.0, -1.0 / 3.0}, -1.0);
        assertEquals(expectedResult.getValue(), result.getValue(), 1.0e-6);
        assertEquals(expectedResult.getPoint(), result.getPoint(), 1.0e-6);
    }
}
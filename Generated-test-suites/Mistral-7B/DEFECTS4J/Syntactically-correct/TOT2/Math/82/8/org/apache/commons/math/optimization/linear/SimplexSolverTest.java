import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.math.MaxEval;
import org.junit.Test;
import org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testConstructorWithDefaultSettings() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0);
    }

    @Test
    public void testConstructorWithCustomSettings() {
        SimplexSolver solver = new SimplexSolver(0.001);
        assertEquals(0.001, solver.epsilon, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeEpsilon() {
        new SimplexSolver(-1.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1, 2}, new double[]{0, 1, 2}),
                new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        Integer pivotCol = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(0, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1, 2}, new double[]{0, 1, 2}),
                new LinearConstraint[]{new LinearConstraint(1.0, 1, Relationship.LEQ, Double.POSITIVE_INFINITY),
                        new LinearConstraint(1.0, 2, Relationship.LEQ, Double.POSITIVE_INFINITY)},
                GoalType.MINIMIZE, false, 1.0e-6);
        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1, 2}, new double[]{0, 1, 2}),
                new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        new SimplexSolver().doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1, 2}, new double[]{0, 1, 2}),
                new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(0, 1, 3.0);
        assertFalse(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testIsOptimalWithNoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1, 2}, new double[]{0, 1, 2}),
                new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        tableau.setEntry(0, tableau.getWidth() - 1, 1.0);
        assertFalse(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testIsOptimalWithOptimalSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1, 2}, new double[]{0, 1, 2}),
                new LinearConstraint[0], GoalType.MINIMIZE, false, 1.0e-6);
        tableau.setEntry(0, tableau.getWidth() - 1, 0.0);
        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 1, 2}, new double[]{0, 1, 2}),
                new LinearConstraint[]{new LinearConstraint(1.0, 1, Relationship.LEQ, Double.POSITIVE_INFINITY),
                        new LinearConstraint(1.0, 2, Relationship.LEQ, Double.POSITIVE_INFINITY)},
                GoalType.MINIMIZE, false, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        assertTrue(Math.abs(tableau.getEntry(0, 1) - 0.0) <= 1.0e-6);
        assertTrue(Math.abs(tableau.getEntry(0, 2) - 1.0) <= 1.0e-6);
        assertEquals(0, tableau.getNumArtificialVariables());
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, 1, 2}, new double[]{0, 1, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(1.0, 1, Relationship.LEQ, Double.POSITIVE_INFINITY),
                new LinearConstraint(1.0, 2, Relationship.LEQ, Double.POSITIVE_INFINITY)};

        SimplexTableau tableau = new SimplexTableau(objective, constraints, GoalType.MINIMIZE, false, 1.0e-6);

        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.doOptimize();
        PointValuePair expected = new PointValuePair(new double[]{0.0, 1.0, 2.0}, -1.0);
        assertEquals(expected.getValue(), result.getValue(), Precision.SAFE_EPSILON);
        assertEquals(expected.getPoint(), result.getPoint(), Precision.SAFE_EPSILON);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testDoOptimizeWithMaxIterationsExceeded() throws OptimizationException {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];

        SimplexTableau tableau = new SimplexTableau(objective, constraints, GoalType.MINIMIZE, false, 1.0e-6);
        MaxEval maxEval = new MaxEval(new MaxEval.Eval() {
            @Override
            public double value() {
                return Double.POSITIVE_INFINITY;
            }
        }, 1);

        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(1);
        solver.setMaxEvaluations(maxEval);
        solver.doOptimize();
    }
}
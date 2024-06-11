import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.epsilon, 0);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(1.0e-4);
        Assert.assertEquals(1.0e-4, solver.epsilon, 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        int pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(0, pivotColumn.intValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPivotColumnWithEmptyTableau() {
        SimplexTableau emptyTableau = new SimplexTableau(new LinearObjectiveFunction(new double[0]),
                Arrays.asList(), null, true, 1.0e-6);
        new SimplexSolver().getPivotColumn(emptyTableau);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        int pivotRow = new SimplexSolver().getPivotRow(0, tableau).intValue();
        Assert.assertEquals(1, pivotRow);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPivotRowWithNullTableau() {
        new SimplexSolver().getPivotRow(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPivotRowWithOutOfBoundsColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        new SimplexSolver().getPivotRow(tableau.getWidth(), tableau);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        new SimplexSolver().doIteration(tableau);

        double[][] expectedTableau = {
                {-1.0, 0.0, 2.0, 3.0, 0.0, 1.0, 0.0},
                {0.0, 1.0, -1.0/3.0, -1.0/3.0, 2.0/3.0, -1.0/3.0, 1.0},
                {0.0, 0.0, 1.0/3.0, 2.0/3.0, 1.0/3.0, -2.0/3.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        };

        Assert.assertArrayEquals(expectedTableau, tableau.getMatrix().getData(), 0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction
                (new double[]{-1, 2}), Arrays.asList(new LinearConstraint[]{
                new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        tableau.setEntry(1, 1, Double.NEGATIVE_INFINITY);

        new SimplexSolver().doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        Assert.assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(0, 0, Double.NEGATIVE_INFINITY);
        Assert.assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(0, 1, 0.0);
        Assert.assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(1, tableau.getWidth() - 2, 0.0);
        Assert.assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(1, tableau.getWidth() - 1, 0.0);
        Assert.assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        Assert.assertFalse(new SimplexSolver().isOptimal(tableau));

        tableau.setEntry(1, tableau.getWidth() - 2, Double.NEGATIVE_INFINITY);
        Assert.assertFalse(new SimplexSolver().isOptimal(tableau));

        tableau.setEntry(1, tableau.getWidth() - 1, 0.0);
        Assert.assertFalse(new SimplexSolver().isOptimal(tableau));

        tableau.setEntry(0, 1, 0.0);
        Assert.assertFalse(new SimplexSolver().isOptimal(tableau));

        tableau.setEntry(0, 2, 0.0);
        Assert.assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        double[][] expectedTableau = {
                {-1.0, 0.0, 2.0, 3.0, 0.0, 1.0, 0.0},
                {0.0, 1.0, -1.0/3.0, -1.0/3.0, 2.0/3.0, -1.0/3.0, 1.0},
                {0.0, 0.0, 1.0/3.0, 2.0/3.0, 1.0/3.0, -2.0/3.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        };

        Assert.assertArrayEquals(expectedTableau, tableau.getMatrix().getData(), 0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithUnfeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        tableau.setEntry(1, tableau.getWidth() - 1, Double.NEGATIVE_INFINITY);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1WithUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        tableau.setEntry(0, 1, Double.NEGATIVE_INFINITY);
        new SimplexSolver().solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2),
                        new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 3)}),
                null, true, 1.0e-6);

        PointValuePair solution = new SimplexSolver().doOptimize();

        Assert.assertEquals(2.0, solution.getPoint()[0], 0);
        Assert.assertEquals(3.0, solution.getPoint()[1], 0);
        Assert.assertEquals(1.0, solution.getValue(), 0);
    }
}
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest {

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(SimplexSolver.DEFAULT_EPSILON, solver.epsilon, 0.0);

        SimplexSolver solverWithCustomEpsilon = new SimplexSolver(0.001);
        Assert.assertEquals(0.001, solverWithCustomEpsilon.epsilon, 0.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 2}, 0),
                Arrays.asList(
                        new LinearConstraint(1, new double[]{1, 1}, 3),
                        new LinearConstraint(2, new double[]{3, 2}, 5)
                ),
                LinearObjectiveFunction.MINIMIZE,
                true,
                SimplexSolver.DEFAULT_EPSILON
        );

        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 2}, 0),
                Arrays.asList(
                        new LinearConstraint(1, new double[]{1, 1}, 3),
                        new LinearConstraint(2, new double[]{3, 2}, 5)
                ),
                LinearObjectiveFunction.MINIMIZE,
                true,
                SimplexSolver.DEFAULT_EPSILON
        );

        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        Assert.assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 2}, 0),
                Arrays.asList(
                        new LinearConstraint(1, new double[]{1, 1}, 3),
                        new LinearConstraint(2, new double[]{3, 2}, 5)
                ),
                LinearObjectiveFunction.MINIMIZE,
                true,
                SimplexSolver.DEFAULT_EPSILON
        );

        new SimplexSolver().doIteration(tableau);

        Assert.assertEquals(-1, tableau.getEntry(1, 0), 0.0);
        Assert.assertEquals(1, tableau.getEntry(1, 1), 0.0);
        Assert.assertEquals(0, tableau.getEntry(1, 2), 0.0);
        Assert.assertEquals(3, tableau.getEntry(1, 3), 0.0);
        Assert.assertEquals(4.5, tableau.getEntry(1, 4), 0.0);
        Assert.assertEquals(0, tableau.getEntry(1, 5), 0.0);
        Assert.assertEquals(0, tableau.getEntry(1, 6), 0.0);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 2}, 0),
                Arrays.asList(
                        new LinearConstraint(1, new double[]{1, 1}, 3),
                        new LinearConstraint(2, new double[]{3, 2}, 5)
                ),
                LinearObjectiveFunction.MINIMIZE,
                true,
                SimplexSolver.DEFAULT_EPSILON
        );

        Assert.assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        new SimplexSolver().doIteration(tableau);
        Assert.assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 2}, 0),
                Arrays.asList(
                        new LinearConstraint(1, new double[]{1, 1}, 3),
                        new LinearConstraint(2, new double[]{3, 2}, 5)
                ),
                LinearObjectiveFunction.MINIMIZE,
                true,
                SimplexSolver.DEFAULT_EPSILON
        );

        Assert.assertFalse(new SimplexSolver().isOptimal(tableau));

        new SimplexSolver().doIteration(tableau);
        Assert.assertTrue(new SimplexSolver().isOptimal(tableau));

        tableau.addArtificialVariable(new LinearConstraintSet(2, new double[]{-1, 1}, LinearObjectiveFunction.MAXIMIZE));
        Assert.assertFalse(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 2}, 0),
                Arrays.asList(
                        new LinearConstraint(1, new double[]{1, 1}, 3),
                        new LinearConstraint(2, new double[]{3, 2}, 5)
                ),
                LinearObjectiveFunction.MINIMIZE,
                true,
                SimplexSolver.DEFAULT_EPSILON
        );

        new SimplexSolver().solvePhase1(tableau);

        Assert.assertEquals(-1, tableau.getEntry(1, 0), 0.0);
        Assert.assertEquals(1, tableau.getEntry(1, 1), 0.0);
        Assert.assertEquals(0, tableau.getEntry(1, 2), 0.0);
        Assert.assertEquals(3, tableau.getEntry(1, 3), 0.0);
        Assert.assertEquals(4.5, tableau.getEntry(1, 4), 0.0);
        Assert.assertEquals(0, tableau.getEntry(1, 5), 0.0);
        Assert.assertEquals(0, tableau.getEntry(1, 6), 0.0);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
                new LinearObjectiveFunction(new double[]{-1, 2}, 0),
                Arrays.asList(
                        new LinearConstraint(1, new double[]{1, 1}, 3),
                        new LinearConstraint(2, new double[]{3, 2}, 5)
                ),
                LinearObjectiveFunction.MINIMIZE,
                true,
                SimplexSolver.DEFAULT_EPSILON
        );

        PointValuePair solution = new SimplexSolver().doOptimize();

        Assert.assertEquals(-1.0, solution.getPoint()[0], 0.0);
        Assert.assertEquals(-2.5, solution.getPoint()[1], 0.0);
        Assert.assertEquals(-3.0, solution.getValue(), 0.0);
    }
}
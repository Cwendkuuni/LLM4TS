import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_EmptyConstraints() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, 3.0);
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        int expected[] = {1, 1};
        RealPointValuePair expectedResult = new RealPointValuePair(expected);
        RealPointValuePair actualResult = solver.doOptimize(f, constraints, goalType);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDoOptimize_SingleConstraint() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, 3.0);
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        int expected[] = {1, 1};
        RealPointValuePair expectedResult = new RealPointValuePair(expected);
        RealPointValuePair actualResult = solver.doOptimize(f, new LinearConstraint[]{constraint}, goalType);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDoOptimize_MultipleConstraints() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.5, 0.5}, 2.5);
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        int expected1[] = {1, 2};
        int expected2[] = {2, 2};
        RealPointValuePair expectedResult1 = new RealPointValuePair(expected1);
        RealPointValuePair expectedResult2 = new RealPointValuePair(expected2);
        RealPointValuePair actualResult1 = solver.doOptimize(f, new LinearConstraint[]{constraint1}, goalType);
        RealPointValuePair actualResult2 = solver.doOptimize(f, new LinearConstraint[]{constraint2}, goalType);
        Assert.assertEquals(expectedResult1, actualResult1);
        Assert.assertEquals(expectedResult2, actualResult2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimize_InvalidConstraints() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, 3.0);
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver.GoalType goalType = SimplexSolver.GoalType.MINIMIZE;
        LinearConstraint invalidConstraint = new LinearConstraint(new double[]{1.0}, 2.0);
        solver.doOptimize(f, new LinearConstraint[]{constraint, invalidConstraint}, goalType);
    }

    @Test
    public void testIsOptimal_EmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new double[0][], new LinearConstraint[0], SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        boolean actual = SimplexSolver.getInstance().isOptimal(tableau);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsOptimal_NonOptimalTableau() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.1, -1.0, 1.1}, {-1.1, -1.0, -1.0}, {0.0, 1.0, 0.0}},
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 0.0}, 1.0)},
                SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        boolean actual = SimplexSolver.getInstance().isOptimal(tableau);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsOptimal_OptimalTableau() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.1, -1.0, 1.1}, {-1.1, -1.0, -1.0}, {0.0, 1.0, 0.0}},
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 0.0}, 1.0)},
                SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        tableau.setEntry(2, 2, 0.0);
        tableau.setEntry(2, 3, 1.0);
        boolean actual = SimplexSolver.getInstance().isOptimal(tableau);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsPhase1Solved_EmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new double[0][], new LinearConstraint[0], SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        boolean actual = SimplexSolver.getInstance().isPhase1Solved(tableau);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsPhase1Solved_NonSolvedTableau() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.1, -1.0, 1.1}, {-1.1, -1.0, -1.0}, {0.0, 1.0, 0.0}},
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 0.0}, 1.0)},
                SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        boolean actual = SimplexSolver.getInstance().isPhase1Solved(tableau);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsPhase1Solved_SolvedTableau() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.1, -1.0, 1.1}, {-1.1, -1.0, -1.0}, {0.0, 1.0, 0.0}},
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 0.0}, 1.0)},
                SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        tableau.setEntry(2, 2, 0.0);
        boolean actual = SimplexSolver.getInstance().isPhase1Solved(tableau);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.1, 0.2, 1.1, 1.3}, {-1.1, -1.2, -1.3, -1.4}, {0.0, 1.0, 0.0, 0.0}},
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 0.0}, 1.0)},
                SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        int expected = 3;
        int actual = SimplexSolver.getInstance().getPivotColumn(tableau).intValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.1, 0.2, 1.1, 1.3}, {-1.1, -1.2, -1.3, -1.4}, {0.0, 1.0, 0.0, 0.0}},
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 0.0}, 1.0)},
                SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        int expected = 1;
        int actual = SimplexSolver.getInstance().getPivotRow(3, tableau).intValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{{0.1, 0.2, 1.1, 1.3}, {-1.1, -1.2, -1.3, -1.4}, {0.0, 1.0, 0.0, 0.0}},
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 0.0}, 1.0)},
                SimplexSolver.GoalType.MINIMIZE, false, 0.0);
        SimplexSolver solver = new SimplexSolver();

        // First iteration
        solver.doIteration(tableau);
        double[][] expectedMatrix = {{0.1, 0.2, 1.1, 1.3}, {-1.1, -1.2, -1.3, -1.4}, {1.0, 0.0, 0.0, -1.0}};
        PointValuePair[] expectedSolution = {new PointValuePair(1.0, 1.0), new PointValuePair(0.0, 0.0)};
        Assert.assertArrayEquals(expectedMatrix, tableau.getMatrix(), 0.0);
        Assert.assertArrayEquals(expectedSolution, tableau.getSolution(), 0.0);

        // Second iteration
        solver.doIteration(tableau);
        expectedMatrix = new double[][]{{1.0, 0.0, 0.0, -1.0}, {-1.1, -1.2, -1.3, -1.4}, {0.1, 0.2, 1.1, 1.3}};
        expectedSolution = new PointValuePair[]{new PointValuePair(0.0, 0.0), new PointValuePair(1.0, 1.0)};
        Assert.assertArrayEquals(expectedMatrix, tableau.getMatrix(), 0.0);
        Assert.assertArrayEquals(expectedSolution, tableau.getSolution(), 0.0);

        // Third iteration
        solver.doIteration(tableau);
        expectedMatrix = new double[][]{{1.0, 0.0, 0.0, -1.0}, {0.0, 1.0, 0.0, -1.0}, {0.1, 0.2, 1.1, 1.3}};
        expectedSolution = new PointValuePair[]{new PointValuePair(0.0, 0.0), new PointValuePair(1.0, 1.0)};
        Assert.assertArrayEquals(expectedMatrix, tableau.getMatrix(), 0.0);
        Assert.assertArrayEquals(expectedSolution, tableau.getSolution(), 0.0);
    }
}
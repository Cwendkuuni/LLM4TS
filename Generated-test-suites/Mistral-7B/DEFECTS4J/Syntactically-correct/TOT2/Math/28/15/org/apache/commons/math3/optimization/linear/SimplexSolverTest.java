import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test for the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

    /**
     * Test for the constructor with custom epsilon and maxUlps.
     */
    @Test
    public void testCustomConstructor() {
        double epsilon = 1.0e-8;
        int maxUlps = 5;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
    }

    /**
     * Test for the getPivotColumn method with a valid tableau.
     */
    @Test
    public void testGetPivotColumnValidTableau() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-2, 1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[]{3, 1}, Relationship.LEQ, 4),
            new LinearConstraint(new double[]{1, -1}, Relationship.GEQ, 0)
        };
        SimplexTableau tableau = new SimplexTableau(objective, constraints, null, null, solver.epsilon, solver.maxUlps);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        Assert.assertEquals(0, pivotColumn.intValue());
    }

    /**
     * Test for the getPivotColumn method with an empty tableau.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnEmptyTableau() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[0]), new LinearConstraint[0], null, null, solver.epsilon, solver.maxUlps);
        solver.getPivotColumn(tableau);
    }

    /**
     * Test for the getPivotRow method with a valid tableau and column.
     */
    @Test
    public void testGetPivotRowValidTableau() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-2, 1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[]{3, 1}, Relationship.LEQ, 4),
            new LinearConstraint(new double[]{1, -1}, Relationship.GEQ, 0)
        };
        SimplexTableau tableau = new SimplexTableau(objective, constraints, null, null, solver.epsilon, solver.maxUlps);
        Integer pivotRow = solver.getPivotRow(tableau, 0);
        Assert.assertEquals(1, pivotRow.intValue());
    }

    /**
     * Test for the doIteration method with a valid tableau.
     */
    @Test
    public void testDoIterationValidTableau() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-2, 1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[]{3, 1}, Relationship.LEQ, 4),
            new LinearConstraint(new double[]{1, -1}, Relationship.GEQ, 0)
        };
        SimplexTableau tableau = new SimplexTableau(objective, constraints, null, null, solver.epsilon, solver.maxUlps);
        solver.doIteration(tableau);
        Assert.assertTrue(Precision.equals(tableau.getEntry(1, 1), 1d, solver.maxUlps));
        Assert.assertTrue(Precision.equals(tableau.getEntry(1, 3), 0d, solver.maxUlps));
    }

    /**
     * Test for the doIteration method with an empty tableau.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationEmptyTableau() throws MaxCountExceededException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[0]), new LinearConstraint[0], null, null, solver.epsilon, solver.maxUlps);
        solver.doIteration(tableau);
    }

    /**
     * Test for the solvePhase1 method with a valid tableau.
     */
    @Test
    public void testSolvePhase1ValidTableau() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-2, 1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[]{3, 1}, Relationship.LEQ, 4),
            new LinearConstraint(new double[]{1, -1}, Relationship.GEQ, 0)
        };
        SimplexTableau tableau = new SimplexTableau(objective, constraints, null, null, solver.epsilon, solver.maxUlps);
        solver.solvePhase1(tableau);
        Assert.assertFalse(Precision.equals(tableau.getEntry(0, tableau.getRhsOffset()), 0d, solver.epsilon));
    }

    /**
     * Test for the doOptimize method with a valid problem.
     */
    @Test
    public void testDoOptimizeValidProblem() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-2, 1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[]{3, 1}, Relationship.LEQ, 4),
            new LinearConstraint(new double[]{1, -1}, Relationship.GEQ, 0)
        };
        PointValuePair solution = solver.doOptimize();
        Assert.assertNotNull(solution);
        Assert.assertEquals(2, solution.getPoint().length);
        Assert.assertTrue(Precision.isClose(solution.getValue(), -5d, solver.epsilon));
    }

    /**
     * Test for the doOptimize method with an infeasible problem.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeInfeasibleProblem() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[]{3, 1}, Relationship.LEQ, 4),
            new LinearConstraint(new double[]{1, -1}, Relationship.GEQ, 0)
        };
        PointValuePair solution = solver.doOptimize();
    }

    /**
     * Test for the doOptimize method with an unbounded problem.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnboundedProblem() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-2, 1}, Relationship.LEQ, 5),
            new LinearConstraint(new double[]{3, 1}, Relationship.LEQ, 4),
            new LinearConstraint(new double[]{1, -1}, Relationship.LEQ, 0)
        };
        PointValuePair solution = solver.doOptimize();
    }
}
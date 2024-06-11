import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.simple.testfunctions.RosenbrockFunction;
import org.apache.commons.math3.optim.nonlinear.simple.testfunctions.SphereFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertNotNull(solver);
        SimplexSolver solverWithEpsilon = new SimplexSolver(0.001);
        Assert.assertNotNull(solverWithEpsilon);
    }

    /**
     * Tests the getPivotColumn method.
     */
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createTestTableau();
        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        Assert.assertEquals(2, pivotColumn.intValue());
    }

    /**
     * Tests the getPivotRow method.
     */
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createTestTableau();
        Integer pivotRow = new SimplexSolver().getPivotRow(2, tableau);
        Assert.assertEquals(4, pivotRow.intValue());
    }

    /**
     * Tests the doIteration method.
     */
    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = createTestTableau();
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        Assert.assertEquals(1.0, tableau.getEntry(4, 2), solver.epsilon);
        Assert.assertEquals(0.0, tableau.getEntry(4, 3), solver.epsilon);
        Assert.assertEquals(0.0, tableau.getEntry(4, 4), solver.epsilon);
        Assert.assertEquals(0.0, tableau.getEntry(4, 5), solver.epsilon);
    }

    /**
     * Tests the isPhase1Solved method.
     */
    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = createTestTableau();
        boolean isPhase1Solved = new SimplexSolver().isPhase1Solved(tableau);
        Assert.assertFalse(isPhase1Solved);
        tableau = createTestTableau2();
        isPhase1Solved = new SimplexSolver().isPhase1Solved(tableau);
        Assert.assertTrue(isPhase1Solved);
    }

    /**
     * Tests the isOptimal method.
     */
    @Test
    public void testIsOptimal() throws OptimizationException {
        SimplexTableau tableau = createTestTableau();
        boolean isOptimal = new SimplexSolver().isOptimal(tableau);
        Assert.assertFalse(isOptimal);
        tableau = createTestTableau2();
        isOptimal = new SimplexSolver().isOptimal(tableau);
        Assert.assertTrue(isOptimal);
    }

    /**
     * Tests the solvePhase1 method.
     */
    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = createTestTableau();
        new SimplexSolver().solvePhase1(tableau);
        Assert.assertEquals(4.0, tableau.getEntry(4, 2), 0.0);
        Assert.assertEquals(3.0, tableau.getEntry(4, 3), 0.0);
        Assert.assertEquals(1.0, tableau.getEntry(4, 4), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(4, 5), 0.0);

        tableau = createTestTableau2();
        new SimplexSolver().solvePhase1(tableau);
        Assert.assertEquals(2.0, tableau.getEntry(2, 3), 0.0);
        Assert.assertEquals(1.0, tableau.getEntry(2, 4), 0.0);
        Assert.assertEquals(0.0, tableau.getEntry(2, 5), 0.0);
    }

    /**
     * Tests the doOptimize method.
     */
    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = createTestTableau();
        RealPointValuePair solution = new SimplexSolver().doOptimize();
        Assert.assertEquals(1.0, solution.getPoint().getComponent(3), 0.0);
        Assert.assertEquals(1.0, solution.getPoint().getComponent(4), 0.0);
        Assert.assertEquals(1.0, solution.getValue(), 0.0);

        tableau = createTestTableau2();
        solution = new SimplexSolver().doOptimize();
        Assert.assertEquals(1.0, solution.getPoint().getComponent(2), 0.0);
        Assert.assertEquals(1.0, solution.getPoint().getComponent(3), 0.0);
        Assert.assertEquals(-3.0, solution.getValue(), 0.0);
    }

    /**
     * Tests the optimization of a linear problem with bounds.
     */
    @Test
    public void testLinearProblemWithBounds() throws OptimizationException {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[]{3.0, 1.0}, Relationship.LEQ, 4.0);
        constraints[1] = new LinearConstraint(new double[]{1.0, 2.0}, Relationship.GEQ, 0.0);
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(objectiveFunction, constraints);
        Assert.assertEquals(new double[]{0.75, 2.25}, solution.getPoint(), 0.0);
        Assert.assertEquals(-1.5, solution.getValue(), 0.0);
    }

    /**
     * Tests the optimization of a nonlinear problem using the simplex solver.
     */
    @Test
    public void testNonlinearProblem() {
        RosenbrockFunction rosenbrock = new RosenbrockFunction(new double[]{-3.0, -2.0}, new double[]{1.0, 1.0});
        SimplexSolver solver = new SimplexSolver();
        try {
            RealPointValuePair solution = solver.optimize(rosenbrock);
            Assert.fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }
    }

    /**
     * Tests the optimization of a nonconvex problem using the simplex solver.
     */
    @Test
    public void testNonconvexProblem() {
        SphereFunction sphere = new SphereFunction(2.0, new double[]{-3.0, -3.0});
        SimplexSolver solver = new SimplexSolver();
        try {
            RealPointValuePair solution = solver.optimize(sphere);
            Assert.fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }
    }

    /**
     * Creates a test tableau with no feasible solution.
     * @return the test tableau
     */
    private SimplexTableau createTestTableauNoFeasibleSolution() {
        double[][] f = new double[][]{
            { 1.0, 1.0, 1.0, 1.0, 1.0 },
            { 1.0, 1.0, 1.0, -1.0, -1.0 },
            { -1.0, -1.0, -1.0, 1.0, 1.0 },
            { -1.0, -1.0, -1.0, -1.0, -1.0 }
        };
        return new SimplexTableau(f, null, null, null, 1e-6);
    }

    /**
     * Creates a test tableau.
     * @return the test tableau
     */
    private SimplexTableau createTestTableau() {
        double[][] f = new double[][]{
            { 1.0, 1.0, 1.0, 1.0, 1.0 },
            { 1.0, 1.0, 1.0, -1.0, -1.0 },
            { -1.0, -1.0, -1.0, 1.0, 1.0 },
            { -1.0, -1.0, -1.0, -1.0, -1.0 }
        };
        return new SimplexTableau(f, null, null, null, 1e-6);
    }

    /**
     * Creates a test tableau 2.
     * @return the test tableau
     */
    private SimplexTableau createTestTableau2() {
        double[][] f = new double[][]{
            { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 },
            { 1.0, 1.0, 1.0, 1.0, 1.0, -1.0 },
            { 1.0, 1.0, 1.0, 1.0, 0.0, 1.0 },
            { 1.0, 1.0, 1.0, 0.0, -1.0, 1.0 },
            { 1.0, 1.0, 1.0, 0.0, -1.0, -1.0 },
            { 1.0, 1.0, 1.0, -1.0, -1.0, -1.0 },
            { 0.0, 0.0, 0.0, 3.0, 3.0, 3.0 }
        };
        return new SimplexTableau(f, null, null, null, 1e-6);
    }
}
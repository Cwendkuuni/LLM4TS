import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    @Test
    public void testSolveSimpleProblem() {
        // given
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, 5.0);
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[]{-1.0, 2.0}, 2.0);

        SimplexSolver solver = new SimplexSolver();

        // when
        PointValuePair solution = solver.optimize(f, constraints);

        // then
        Assert.assertNotNull(solution);
        Assert.assertEquals(5.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{3.0 / 5.0, 1.0 - 3.0 * 3.0 / 5.0}, solution.getPoint(), 0.0);
    }

    @Test
    public void testSolveProblemWithNoSolution() {
        // given
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);

        SimplexSolver solver = new SimplexSolver();

        // expect
        try {
            solver.optimize(f, constraints);
            Assert.fail("Expected UnboundedSolutionException or MaxCountExceededException");
        } catch (UnboundedSolutionException | MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testSolveProblemWithMaxCountExceeded() {
        // given
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[0];

        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);

        // expect
        try {
            solver.optimize(f, constraints);
            Assert.fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testSolveProblemWithFeasibleSolution() {
        // given
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 3.0);
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);

        SimplexSolver solver = new SimplexSolver();

        // when
        PointValuePair solution = solver.optimize(f, constraints);

        // then
        Assert.assertNotNull(solution);
        Assert.assertEquals(3.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{1.0 / 2.0, 1.0 / 2.0}, solution.getPoint(), 0.0);
    }

    @Test
    public void testSolveProblemWithGoalTypeMinimize() {
        // given
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, 5.0);
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[]{-1.0, 2.0}, 2.0);

        SimplexSolver solver = new SimplexSolver(SimplexSolver.GOAL_TYPE_MINIMIZE);

        // when
        PointValuePair solution = solver.optimize(f, constraints);

        // then
        Assert.assertNotNull(solution);
        Assert.assertEquals(5.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{3.0 / 5.0, 1.0 - 3.0 * 3.0 / 5.0}, solution.getPoint(), 0.0);
    }

    @Test
    public void testSolveProblemWithGoalTypeMaximize() {
        // given
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, 5.0);
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[]{-1.0, 2.0}, 2.0);

        SimplexSolver solver = new SimplexSolver(SimplexSolver.GOAL_TYPE_MAXIMIZE);

        // when
        PointValuePair solution = solver.optimize(f, constraints);

        // then
        Assert.assertNotNull(solution);
        Assert.assertEquals(5.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{3.0 / 5.0, 1.0 - 3.0 * 3.0 / 5.0}, solution.getPoint(), 0.0);
    }

}
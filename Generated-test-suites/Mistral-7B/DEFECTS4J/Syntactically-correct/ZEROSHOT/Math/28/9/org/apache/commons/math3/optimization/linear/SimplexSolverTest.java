import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleProblem() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{0.0, 1.0}, Double.NEGATIVE_INFINITY)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.optimize(function, constraints, goalType);
        assertNotNull(solution);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.0, 1.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{0.0, 1.0}, Double.NEGATIVE_INFINITY)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        solver.optimize(function, constraints, goalType);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{0.0, 1.0}, Double.NEGATIVE_INFINITY)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-4, 1);

        for (int i = 0; i < 1000; i++) {
            solver.optimize(function, constraints, goalType);
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = {
            new LinearConstraint(new double[]{1.0, 1.0}, Double.NEGATIVE_INFINITY),
            new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{0.0, 1.0}, Double.NEGATIVE_INFINITY)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(function, constraints, goalType);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }
}
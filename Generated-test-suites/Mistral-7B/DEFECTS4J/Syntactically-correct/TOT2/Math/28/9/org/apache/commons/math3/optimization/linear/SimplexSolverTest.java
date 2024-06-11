import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.NonNegativeConstraint;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.linear.GoalType;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testLinearProgramWithOneConstraint() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{-3.0}}, new double[]{2.0});
        LinearConstraint constraint = new LinearConstraintSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, 1.0, Relationship.LEQ);
        LinearConstraint[] constraints = new LinearConstraint[]{constraint};
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{0.0, 0.0};
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);

        // Exercise
        PointValuePair solution = solver.optimize(function, constraints, goalType, new NonNegativeConstraint(bounds));

        // Verify
        Assert.assertArrayEquals(new double[]{0.0}, solution.getPoint(), 1e-6);
        Assert.assertEquals(2.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testLinearProgramWithMultipleConstraints() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, new double[]{2.0});
        LinearConstraint constraint1 = new LinearConstraintSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, 1.0, Relationship.LEQ);
        LinearConstraint constraint2 = new LinearConstraintSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, 2.0, Relationship.LEQ);
        LinearConstraint[] constraints = new LinearConstraint[]{constraint1, constraint2};
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{0.0, 0.0};
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);

        // Exercise
        PointValuePair solution = solver.optimize(function, constraints, goalType, new NonNegativeConstraint(bounds));

        // Verify
        Assert.assertArrayEquals(new double[]{0.5, 0.5}, solution.getPoint(), 1e-6);
        Assert.assertEquals(1.0, solution.getValue(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, new double[]{-2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{0.0, 0.0};
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);

        // Exercise and Verify Exception
        solver.optimize(function, constraints, goalType, new NonNegativeConstraint(bounds));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, new double[]{2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{0.0, 0.0};
        SimplexSolver solver = new SimplexSolver(1.0e-12, 10);

        // Exercise and Verify Exception
        solver.optimize(function, constraints, goalType, new NonNegativeConstraint(bounds));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, new double[]{2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{0.0, 0.0};
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);

        // Exercise and Verify Exception
        solver.optimize(function, constraints, goalType, new LinearConstraintSet(new double[][]{new double[]{1.0}, new double[]{-1.0}}, 1.0, Relationship.LEQ));
    }

    @Test
    public void testRestrictToNonNegative() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, new double[]{2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{Double.NEGATIVE_INFINITY, 0.0};
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);

        // Exercise
        PointValuePair solution = solver.optimize(function, constraints, goalType, new NonNegativeConstraint(bounds));

        // Verify
        Assert.assertArrayEquals(new double[]{0.0}, solution.getPoint(), 1e-6);
        Assert.assertEquals(2.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testRestrictToNonNegativeWithBounds() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, new double[]{2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{0.0, 1.0};
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);

        // Exercise
        PointValuePair solution = solver.optimize(function, constraints, goalType, new NonNegativeConstraint(bounds));

        // Verify
        Assert.assertTrue(solution.getPoint()[0] >= 0);
        Assert.assertTrue(solution.getPoint()[1] >= 0);
        Assert.assertEquals(1.0, solution.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroEpsilon() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, new double[]{2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(0.0, 10);

        // Exercise and Verify Exception
        solver.optimize(function, constraints, goalType, new NonNegativeConstraint(new double[]{}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxUlps() throws Exception {
        // Setup
        LinearObjectiveFunction function = new LinearObjectiveFunctionSet(new double[][]{new double[]{1.0}, new double[]{1.0}}, new double[]{2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1.0e-6, -10);

        // Exercise and Verify Exception
        solver.optimize(function, constraints, goalType, new NonNegativeConstraint(new double[]{}));
    }
}
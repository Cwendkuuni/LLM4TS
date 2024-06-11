import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0}, new double[]{0.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objectiveFunction);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 0.0);
        Assert.assertNull(result.getPoint());
    }

    @Test
    public void testSimpleLinearProgram() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 1.0}, 3.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objectiveFunction, constraint1, constraint2);
        Assert.assertEquals(5.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{0.6, 0.4}, result.getPoint(), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedLinearProgram() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{0.0, 0.0});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objectiveFunction);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleLinearProgram() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, 1.0);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objectiveFunction, constraint1, constraint2);
    }

    @Test
    public void testMultipleObjectiveFunctions() {
        LinearObjectiveFunction objectiveFunction1 = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearObjectiveFunction objectiveFunction2 = new LinearObjectiveFunction(new double[]{1.0, -2.0}, new double[]{3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 1.0}, 3.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objectiveFunction1, objectiveFunction2, constraint1, constraint2);
        Assert.assertEquals(6.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{0.6, 0.4}, result.getPoint(), 0.0);
    }

    @Test
    public void testGoalType() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 1.0}, 3.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objectiveFunction, constraint1, constraint2, GoalType.MINIMIZE);
        Assert.assertEquals(5.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{0.6, 0.4}, result.getPoint(), 0.0);
        PointValuePair resultMax = solver.optimize(objectiveFunction, constraint1, constraint2, GoalType.MAXIMIZE);
        Assert.assertEquals(-5.0, resultMax.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{0.4, 0.6}, resultMax.getPoint(), 0.0);
    }

    @Test
    public void testNonNegativeConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0, GoalType.LEQUAL);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 1.0}, 3.0, GoalType.GREATER_EQUAL);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objectiveFunction, constraint1, constraint2);
        Assert.assertEquals(5.0, result.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{0.6, 0.4}, result.getPoint(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 1.0}, 3.0);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objectiveFunction, constraint1, constraint2, GoalType.INVALID);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 1.0}, 3.0);
        SimplexSolver solver = new SimplexSolver(0.001, 1);
        solver.setMaxIterations(1);
        try {
            solver.optimize(objectiveFunction, constraint1, constraint2);
        } catch (NoFeasibleSolutionException e) {
            throw new RuntimeException("Should not have encountered a NoFeasibleSolutionException", e);
        }
    }
}
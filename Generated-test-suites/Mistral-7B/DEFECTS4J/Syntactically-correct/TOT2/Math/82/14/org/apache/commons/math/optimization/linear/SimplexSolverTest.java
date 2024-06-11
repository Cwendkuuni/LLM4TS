import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.ArrayRealLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.ArrayRealLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() throws OptimizationException {
        LinearObjectiveFunction f = new ArrayRealLinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
        LinearConstraintSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f);
        Assert.assertNull(solution);
    }

    @Test
    public void testSingleConstraint() throws OptimizationException {
        LinearObjectiveFunction f = new ArrayRealLinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearConstraint constraint = new ArrayRealLinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint);
        LinearObjectiveFunctionSet objectives = new LinearObjectiveFunctionSet(f);
        LinearConstraintSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objectives, constraints, GoalType.MAXIMIZE);
        Assert.assertEquals(0.6, solution.getPoint()[0], 1e-5);
        Assert.assertEquals(1.5, solution.getValue(), 1e-5);
    }

    @Test
    public void testMultipleConstraints() throws OptimizationException {
        LinearObjectiveFunction f = new ArrayRealLinearObjectiveFunction(new double[]{1.0, 2.0, 3.0}, new double[]{3.0, 4.0, 5.0});
        LinearConstraint constraint1 = new ArrayRealLinearConstraint(new double[]{1.0, 0.0}, 2.0);
        LinearConstraint constraint2 = new ArrayRealLinearConstraint(new double[]{0.0, 1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint1, constraint2);
        LinearObjectiveFunctionSet objectives = new LinearObjectiveFunctionSet(f);
        LinearConstraintSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objectives, constraints, GoalType.MAXIMIZE);
        Assert.assertEquals(1.0 / 3.0, solution.getPoint()[0], 1e-5);
        Assert.assertEquals(1.0 / 2.0, solution.getPoint()[1], 1e-5);
        Assert.assertEquals(1.0 / 3.0 * 3.0 + 1.0 / 2.0 * 4.0 + 1.0 / 3.0 * 5.0, solution.getValue(), 1e-5);
    }

    @Test
    public void testNegativeObjective() throws OptimizationException {
        LinearObjectiveFunction f = new ArrayRealLinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-3.0, -4.0});
        LinearConstraint constraint = new ArrayRealLinearConstraint(new double[]{1.0, 1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint);
        LinearObjectiveFunctionSet objectives = new LinearObjectiveFunctionSet(f);
        LinearConstraintSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objectives, constraints, GoalType.MINIMIZE);
        Assert.assertEquals(-0.6, solution.getPoint()[0], 1e-5);
        Assert.assertEquals(-1.5, solution.getValue(), 1e-5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNonLinearObjective() throws OptimizationException {
        LinearObjectiveFunction f = new ArrayRealLinearObjectiveFunction(new double[]{1.0}, new double[]{Math.exp(2.0)});
        LinearConstraint constraint = new ArrayRealLinearConstraint(new double[]{1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint);
        LinearObjectiveFunctionSet objectives = new LinearObjectiveFunctionSet(f);
        LinearConstraintSolver solver = new SimplexSolver();
        solver.optimize(objectives, constraints, GoalType.MAXIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyObjectives() throws OptimizationException {
        LinearObjectiveFunctionSet objectives = new LinearObjectiveFunctionSet();
        LinearConstraint constraint = new ArrayRealLinearConstraint(new double[]{1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint);
        LinearConstraintSolver solver = new SimplexSolver();
        solver.optimize(objectives, constraints, GoalType.MAXIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstraintsAndObjectives() throws OptimizationException {
        LinearObjectiveFunctionSet objectives = new LinearObjectiveFunctionSet();
        LinearConstraintSolver solver = new SimplexSolver();
        solver.optimize(objectives, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullObjectives() throws OptimizationException {
        LinearObjectiveFunctionSet objectives = null;
        LinearConstraint constraint = new ArrayRealLinearConstraint(new double[]{1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSet(constraint);
        LinearConstraintSolver solver = new SimplexSolver();
        solver.optimize(objectives, constraints, GoalType.MAXIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstraints() throws OptimizationException {
        LinearObjectiveFunction f = new ArrayRealLinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearObjectiveFunctionSet objectives = new LinearObjectiveFunctionSet(f);
        LinearConstraintSolver solver = new SimplexSolver();
        solver.optimize(objectives, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGoalType() throws OptimizationException {
        LinearObjectiveFunction f = new ArrayRealLinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        LinearObjectiveFunctionSet objectives = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSolver solver = new SimplexSolver();
        solver.optimize(objectives, constraints, null);
    }
}
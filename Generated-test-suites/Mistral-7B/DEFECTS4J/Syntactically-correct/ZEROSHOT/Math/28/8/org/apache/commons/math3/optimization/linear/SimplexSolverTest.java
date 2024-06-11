import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.RealVector2D;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    /**
     * Test optimization with an equality constraint.
     */
    @Test
    public void testOptimizationWithEqualityConstraint() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 1}, new double[]{0.1});
        LinearConstraint constraint = new LinearConstraint(1, new double[]{0, 1}, 0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(constraint);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearOptimizationProblem(objectiveFunction, constraints, GoalType.MINIMIZE));
        Assert.assertEquals(0.1, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{0.0, 1.0}, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with inequality constraints.
     */
    @Test
    public void testOptimizationWithInequalityConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 1}, new double[]{0.1});
        LinearConstraint constraint1 = new LinearConstraint(1, new double[]{0, 1}, 2);
        LinearConstraint constraint2 = new LinearConstraint(1, new double[]{0, 1}, 1);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearOptimizationProblem(objectiveFunction, constraints, GoalType.MINIMIZE));
        Assert.assertEquals(0.1, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{0.0, 0.5}, solution.getPoint(), 1e-6);
    }

    /**
     * Test optimization with unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testOptimizationWithUnboundedSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1, 1}, new double[]{-1, -1});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearOptimizationProblem(objectiveFunction, new LinearConstraintSet(), GoalType.MINIMIZE));
    }

    /**
     * Test optimization with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testOptimizationWithNoFeasibleSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 1}, new double[]{0.1});
        LinearConstraint constraint = new LinearConstraint(1, new double[]{0, 1}, 2);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(constraint);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearOptimizationProblem(objectiveFunction, constraints, GoalType.MINIMIZE));
    }

    /**
     * Test optimization with maximum iteration count exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testOptimizationWithMaxIterationCountExceeded() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1, 1}, new double[]{0.1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver(1e-2, 1);
        PointValuePair solution = solver.optimize(new LinearOptimizationProblem(objectiveFunction, constraints, GoalType.MINIMIZE));
    }

}
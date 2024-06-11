import static org.junit.Assert.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunctionSet;

import java.util.List;

public class SimplexSolverTest {

    @org.junit.Test
    public void testEmptyConstraints() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunctionSet(new double[]{-1, 2}, new double[]{0d});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objective);
        assertEquals(Double.NEGATIVE_INFINITY, result.getValue(), epsilon);
        assertEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, result.getPoint(), epsilon);
    }

    @org.junit.Test
    public void testSimpleExample() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunctionSet(new double[]{-3, 2}, new double[]{12.5d});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 1}, 2);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1, 1}, 1);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objective, constraints);
        assertEquals(12.5d, result.getValue(), epsilon);
        assertEquals(new double[]{3d / 2d, 1.5d}, result.getPoint(), epsilon);
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunctionSet(new double[]{-1, 2}, new double[]{0d});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 1}, 2);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1, 1}, 1);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
        SimplexSolver solver = new SimplexSolver(0.001d, 1);
        solver.setMaxIterations(1);
        PointValuePair result = solver.optimize(objective, constraints);
        fail("MaxCountExceededException expected.");
    }

    @org.junit.Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunctionSet(new double[]{-1, 2}, new double[]{0d});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 1}, 2);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1000, 1}, 1);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objective, constraints);
        fail("UnboundedSolutionException expected.");
    }

    @org.junit.Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunctionSet(new double[]{-1, 2}, new double[]{0d});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1, 1}, 2);
        constraints.addConstraint(constraint1);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1, 1}, 1);
        constraints.addConstraint(constraint2);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0, 1}, 1);
        constraints.addConstraint(constraint3);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objective, constraints);
        fail("NoFeasibleSolutionException expected.");
    }
}
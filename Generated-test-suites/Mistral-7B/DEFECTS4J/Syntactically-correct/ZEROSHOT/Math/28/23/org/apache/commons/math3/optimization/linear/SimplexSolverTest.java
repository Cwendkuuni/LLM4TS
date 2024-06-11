import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() throws OptimizeException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(f, constraints, goalType);
        assertNull(result);
    }

    @Test
    public void testNoSolution() throws OptimizeException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(1.0, new double[]{1.0}, null);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(f, constraints, goalType);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSimpleExample() throws OptimizeException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.0, 3.0}, new double[]{4.0, -1.0});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(1.0, new double[]{1.0, 1.0}, null);
        constraints[1] = new LinearConstraint(0.0, new double[]{2.0, -1.0}, null);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(f, constraints, goalType);
        assertEquals(new double[]{2.0 / 3.0, 1.0 - 2.0 / 3.0}, result.getPoint(), 0.001);
        assertEquals(-4.0 / 3.0, result.getValue(), 0.001);
    }

    @Test
    public void testMaxCountExceeded() throws OptimizeException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(1.0, new double[]{1.0}, null);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.setMaxIterations(0);
            solver.optimize(f, constraints, goalType);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

}
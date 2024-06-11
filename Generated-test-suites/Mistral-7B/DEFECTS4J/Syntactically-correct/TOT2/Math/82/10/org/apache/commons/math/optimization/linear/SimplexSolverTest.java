import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationException;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstructor() {
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }
    }

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 2.0, ConstraintQuality.EQUAL));
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(new PointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, -1.0 / 3.0), solution, 0.001);
    }

    @Test
    public void testCustomEpsilonConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001);
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 2.0, ConstraintQuality.EQUAL));
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(new PointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, -1.0 / 3.0), solution, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullObjectiveFunction() {
        SimplexSolver solver = new SimplexSolver();
        LinearConstraintSet constraints = new LinearConstraintSet();
        solver.doOptimize(null, constraints, LinearObjectiveFunction.GOAL_MINIMIZE, false, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstraints() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        solver.doOptimize(objFunction, null, LinearObjectiveFunction.GOAL_MINIMIZE, false, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGoalType() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        solver.doOptimize(objFunction, constraints, null, false, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEpsilon() {
        SimplexSolver solver = new SimplexSolver(-0.01);
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 2.0, ConstraintQuality.EQUAL));
        PointValuePair solution = solver.doOptimize();
        fail("Should not reach this point");
    }

    @Test
    public void testUnboundedSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{0.0, 0.0}, new double[]{-1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        try {
            solver.doOptimize(objFunction, constraints, LinearObjectiveFunction.GOAL_MINIMIZE, false, 0.0);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testNoFeasibleSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{0.0, 1.0}, new double[]{1.0, -1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 2.0, ConstraintQuality.EQUAL));
        try {
            solver.doOptimize(objFunction, constraints, LinearObjectiveFunction.GOAL_MINIMIZE, false, 0.0);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

}
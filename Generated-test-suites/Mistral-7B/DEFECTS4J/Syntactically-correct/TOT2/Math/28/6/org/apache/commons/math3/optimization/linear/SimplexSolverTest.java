import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintViolationException;
import org.apache.commons.math3.optim.nonlinear.scalar.NonNegativeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBound;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimpleOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, 0);
        LinearConstraint g1 = new LinearConstraint(new double[]{1.0, 1.0}, 1.0);
        LinearConstraint g2 = new LinearConstraint(new double[]{1.0, 2.0}, 2.0);
        LinearConstraint g3 = new LinearConstraint(new double[]{1.0, -1.0}, 0.0);
        LinearConstraint[] constraints = {g1, g2, g3};
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunctionSet objectiveFunctions = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        PointValuePair solution = null;
        try {
            solution = solver.optimize(objectiveFunctions, constraintSet, GoalType.MINIMIZE,
                    new NonNegativeConstraint(new SimpleBound(0.0, Double.POSITIVE_INFINITY)));
        } catch (LinearConstraintViolationException e) {
            Assert.fail("A feasible solution should exist: " + e.getMessage());
        }
        Assert.assertNotNull(solution);
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{-1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizationWithNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0, 1.0}, 0);
        LinearConstraint g1 = new LinearConstraint(new double[]{1.0, 1.0}, 1.0);
        LinearConstraint g2 = new LinearConstraint(new double[]{1.0, 2.0}, 2.0);
        LinearConstraint g3 = new LinearConstraint(new double[]{1.0, -1.0}, 0.0);
        LinearConstraint[] constraints = {g1, g2, g3};
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunctionSet objectiveFunctions = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        try {
            solver.optimize(objectiveFunctions, constraintSet, GoalType.MINIMIZE,
                    new NonNegativeConstraint(new SimpleBound(0.0, Double.POSITIVE_INFINITY)));
            Assert.fail("A no feasible solution should have been thrown: No exceptions were thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testOptimizationWithMaxIterations() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0, 1.0}, 0);
        LinearConstraint g1 = new LinearConstraint(new double[]{1.0, 1.0}, 1.0);
        LinearConstraint g2 = new LinearConstraint(new double[]{1.0, 2.0}, 2.0);
        LinearConstraint g3 = new LinearConstraint(new double[]{1.0, -1.0}, 0.0);
        LinearConstraint[] constraints = {g1, g2, g3};
        SimplexSolver solver = new SimplexSolver(1, 0); // Set max iterations to 1
        LinearObjectiveFunctionSet objectiveFunctions = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        try {
            solver.optimize(objectiveFunctions, constraintSet, GoalType.MINIMIZE,
                    new NonNegativeConstraint(new SimpleBound(0.0, Double.POSITIVE_INFINITY)));
            Assert.fail("A no feasible solution or maximum iterations should have been thrown: No exceptions were thrown");
        } catch (NoFeasibleSolutionException | MaxCountExceededException e) {
            // Expected
        }
    }
}
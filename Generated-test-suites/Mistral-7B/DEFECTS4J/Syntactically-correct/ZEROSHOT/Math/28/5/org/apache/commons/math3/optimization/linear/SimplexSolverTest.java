import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.LinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValues;
import org.apache.commons.math3.optim.initializer.AchievingDirectionInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.initializer.SimpleBoundingBoxInitializer;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolver() throws Exception {
        // Test case for a simple linear optimization problem
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 3.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, new double[]{1.0, 1.0}, 3.0));
        constraints.addConstraint(new LinearConstraint(2.0, new double[]{1.0, 2.0}, 2.0));

        LinearOptimizer solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(func, constraints, GoalType.MINIMIZE, null);

        // Verify the solution
        double[] expectedSolution = new double[]{0.5, 1.5};
        double expectedOptimumValue = -2.5;
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 0.0);
        Assert.assertEquals(expectedOptimumValue, solution.getValue(), 0.0);

        // Test case for an unbounded solution
        LinearObjectiveFunction func2 = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 3.0});
        LinearConstraintSet constraints2 = new LinearConstraintSet();

        // Unbounded constraint
        LinearConstraint constraint = new LinearConstraint(Double.POSITIVE_INFINITY, new double[]{1.0}, 0.0);
        constraints2.addConstraint(constraint);

        try {
            solver.optimize(func2, constraints2, GoalType.MINIMIZE, null);
            Assert.fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Expected
        }

        // Test case for a problem with no feasible solution
        LinearObjectiveFunction func3 = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 3.0});
        LinearConstraintSet constraints3 = new LinearConstraintSet();
        LinearConstraint constraint2 = new LinearConstraint(Double.NEGATIVE_INFINITY, new double[]{1.0}, 0.0);
        constraints3.addConstraint(constraint2);

        try {
            solver.optimize(func3, constraints3, GoalType.MINIMIZE, null);
            Assert.fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }

        // Test case for a problem with max iterations exceeded
        SimplexSolver solverMaxIter = new SimplexSolver(1, 0);
        LinearObjectiveFunction funcMaxIter = new LinearObjectiveFunction(new double[]{1.0}, new double[]{1.0});
        LinearConstraintSet constraintsMaxIter = new LinearConstraintSet();

        constraintsMaxIter.addConstraint(new LinearConstraint(1.0, new double[]{1.0}, 0.0));

        try {
            solverMaxIter.optimize(funcMaxIter, constraintsMaxIter, GoalType.MINIMIZE, null);
            Assert.fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        // Testing initializers
        LinearObjectiveFunction funcInit = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0, 3.0});
        LinearConstraintSet constraintsInit = new LinearConstraintSet();
        constraintsInit.addConstraint(new LinearConstraint(1.0, new double[]{1.0, 1.0}, 1.0));
        constraintsInit.addConstraint(new LinearConstraint(1.0, new double[]{1.0, 2.0}, 3.0));

        SimplexSolver solverInit = new SimplexSolver();
        solverInit.setInitialDirections(new AchievingDirectionInitializer());
        PointValuePair solutionInit = solverInit.optimize(funcInit, constraintsInit, GoalType.MINIMIZE, null);

        double[] expectedSolutionInit = new double[]{1.0 / 3.0, 1.0};
        double expectedOptimumValueInit = 1.0 / 3.0;
        Assert.assertArrayEquals(expectedSolutionInit, solutionInit.getPoint(), 0.0);
        Assert.assertEquals(expectedOptimumValueInit, solutionInit.getValue(), 0.0);

        SimplexSolver solverInitBB = new SimplexSolver();
        solverInitBB.setInitialDirections(new SimpleBoundingBoxInitializer(2));
        PointValuePair solutionInitBB = solverInitBB.optimize(funcInit, constraintsInit, GoalType.MINIMIZE, null);

        double[] expectedSolutionInitBB = new double[]{0.0, 1.0};
        double expectedOptimumValueInitBB = 1.0;
        Assert.assertArrayEquals(expectedSolutionInitBB, solutionInitBB.getPoint(), 0.0);
        Assert.assertEquals(expectedOptimumValueInitBB, solutionInitBB.getValue(), 0.0);
    }
}
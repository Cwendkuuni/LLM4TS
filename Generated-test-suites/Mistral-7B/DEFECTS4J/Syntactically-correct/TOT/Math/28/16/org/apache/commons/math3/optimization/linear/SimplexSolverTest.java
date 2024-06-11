package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test for the normal case when the optimization problem has a solution.
     */
    @Test
    public void testNormalCase() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 5.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{0.0, 1.0}, 4.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{constraint1, constraint2});
        PointValuePair expectedResult = new PointValuePair(new double[]{0.5, 2.5}, -2.5);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(objectiveFunction, constraints);

        Assert.assertEquals(expectedResult.getPoint(), result.getPoint(), 1e-8);
        Assert.assertEquals(expectedResult.getValue(), result.getValue(), 1e-8);
    }

    /**
     * Test for the error case when the optimization problem has no feasible solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{0.0, 1.0}, 4.0);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{constraint1, constraint2});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objectiveFunction, constraints);
    }

    /**
     * Test for the error case when the optimization problem has an unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 5.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{0.0, 1.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY);
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{constraint1, constraint2});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objectiveFunction, constraints);
    }

    /**
     * Test for the error case when the optimization problem exceeds the maximum iteration count.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationCount()
        throws MaxCountExceededException {
        SimplexSolver solver = new SimplexSolver(1e-12, 1);
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        solver.optimize(objectiveFunction, constraints);
    }
}
package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Tests that {@link SimplexSolver} finds the optimal solution for a simple
     * example.
     */
    @Test
    public void testOptimalSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, -3}, new double[]{5, 7});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[]{-1, 1}, 2);
        constraints[1] = new LinearConstraint(new double[]{1, 1}, 3);
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(objective, constraints);
        PointValuePair solution = solver.optimize(problem);
        Assert.assertEquals(0, solution.getPoint()[0], 1e-6);
        Assert.assertEquals(0, solution.getPoint()[1], 1e-6);
        Assert.assertEquals(3.0 / 2.0, solution.getValue(), 1e-6);
    }

    /**
     * Tests that {@link SimplexSolver} throws an exception when the problem
     * has no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, -3}, new double[]{5, 7});
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[]{-1, 1}, 1);
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(objective, constraints);
        solver.optimize(problem);
    }

    /**
     * Tests that {@link SimplexSolver} throws an exception when the problem
     * is unbounded.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, -3}, new double[]{5, 7});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(objective, constraints);
        solver.optimize(problem);
    }

    /**
     * Tests that {@link SimplexSolver} throws an exception when the maximal
     * number of iterations is exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1, -3}, new double[]{5, 7});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[]{-1, 1}, 2);
        constraints[1] = new LinearConstraint(new double[]{1, 1}, 3);
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(objective, constraints);
        solver.setMaxIterations(0);
        solver.optimize(problem);
    }
}
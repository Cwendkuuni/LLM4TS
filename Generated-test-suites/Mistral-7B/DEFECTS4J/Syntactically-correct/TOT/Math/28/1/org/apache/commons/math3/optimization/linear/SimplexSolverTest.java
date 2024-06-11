package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleGoalType;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test that the solver returns the correct solution for a simple linear optimization problem.
     * @throws MaxCountExceededException if the maximal iteration count has been exceeded
     * @throws NoFeasibleSolutionException if the model is found not to have a bounded solution
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     */
    @Test
    public void testSimpleOptimization()
        throws MaxCountExceededException, NoFeasibleSolutionException, UnboundedSolutionException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1d, 2d}, {3d, 4d}}, new double[]{-5d, -4d});
        LinearConstraint[] constraints = new LinearConstraint[]{{new LinearConstraint(1d, 0d, Double.POSITIVE_INFINITY),
                                                                 new LinearConstraint(0d, 1d, Double.POSITIVE_INFINITY)}};
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints, SimpleGoalType.MINIMIZE, null);
        Assert.assertEquals(-6d, solution.getValue(), epsilon);
        Assert.assertArrayEquals(new double[]{1d / 2d, 1d / 2d}, solution.getPoint(), epsilon);
    }

    /**
     * Test that the solver returns the correct solution for a linear optimization problem with bounds.
     * @throws MaxCountExceededException if the maximal iteration count has been exceeded
     * @throws NoFeasibleSolutionException if the model is found not to have a bounded solution
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     */
    @Test
    public void testOptimizationWithBounds()
        throws MaxCountExceededException, NoFeasibleSolutionException, UnboundedSolutionException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1d, 2d}, {3d, 4d}}, new double[]{-5d, -4d});
        LinearConstraint[] constraints = new LinearConstraint[]{{new LinearConstraint(1d, 0d, 2d),
                                                                 new LinearConstraint(0d, 1d, 4d)},
                                                               {new LinearConstraint(0d, 2d, Double.POSITIVE_INFINITY)}};
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints, SimpleGoalType.MINIMIZE, null);
        Assert.assertEquals(-10d, solution.getValue(), epsilon);
        Assert.assertArrayEquals(new double[]{1d, 1d}, solution.getPoint(), epsilon);
    }

    /**
     * Test that the solver returns a no feasible solution exception for an infeasible optimization problem.
     * @throws MaxCountExceededException if the maximal iteration count has been exceeded
     * @throws NoFeasibleSolutionException if the model is found not to have a bounded solution
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testInfeasibleOptimization()
        throws MaxCountExceededException, NoFeasibleSolutionException, UnboundedSolutionException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1d, 2d}, {3d, 4d}}, new double[]{-5d, -4d});
        LinearConstraint[] constraints = new LinearConstraint[]{{new LinearConstraint(1d, 0d, 0d),
                                                                 new LinearConstraint(0d, 1d, 0d)}};
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, SimpleGoalType.MINIMIZE, null);
    }

    /**
     * Test that the solver returns a maximum iteration count exceeded exception for an optimization problem
     * that can't be solved within the given number of iterations.
     * @throws MaxCountExceededException if the maximal iteration count has been exceeded
     * @throws NoFeasibleSolutionException if the model is found not to have a bounded solution
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations()
        throws MaxCountExceededException, NoFeasibleSolutionException, UnboundedSolutionException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1d, 1d}, {1d, 1d}}, new double[]{0d, 0d});
        LinearConstraint[] constraints = new LinearConstraint[]{{new LinearConstraint(1d, 0d, Double.POSITIVE_INFINITY),
                                                                 new LinearConstraint(0d, 1d, Double.POSITIVE_INFINITY)}};
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(f, constraints, SimpleGoalType.MINIMIZE, null);
    }
}
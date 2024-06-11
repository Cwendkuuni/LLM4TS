package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test simplex solver with a linear optimization problem having a bounded optimal solution.
     * @throws Exception if an error occurs
     */
    @Test
    public void testBoundedOptimalSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearObjectiveFunction(new double[][] { { -1d, 1d, 0d }, { 2d, -1d, 1d }, { 3d, -2d, 2d } }, 1d, new LinearConstraint[] { new LinearConstraint(new double[] { 1d, 1d }, 0d ) }));
        Assert.assertEquals(new double[] { 1d / 4d, 1d / 2d }, solution.getPoint(), 0.00001);
        Assert.assertEquals(2d / 3d, solution.getValue(), 0.00001);
    }

    /**
     * Test simplex solver with a linear optimization problem having an unbounded optimal solution.
     * @throws Exception if an error occurs
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedOptimalSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(new LinearObjectiveFunction(new double[][] { { -1d, 1d, 0d }, { 2d, -1d, 1d }, { 3d, -2d, 2d } }, Double.NEGATIVE_INFINITY, new LinearConstraint[] {} ));
    }

    /**
     * Test simplex solver with a linear optimization problem having no feasible solution.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(new LinearObjectiveFunction(new double[][] { { 1d, 1d, 1d }, { 2d, -1d, 1d }, { 3d, -2d, 2d } }, Double.NEGATIVE_INFINITY, new LinearConstraint[] { new LinearConstraint(new double[] { 1d, 1d }, 2d ) }));
    }

    /**
     * Test simplex solver with a linear optimization problem having a maximal iteration count exceeded.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        SimplexSolver solver = new SimplexSolver(0.000001, 10);
        solver.optimize(new LinearObjectiveFunction(new double[][] { { -1d, 1d, 0d }, { 2d, -1d, 1d }, { 3d, -2d, 2d } }, Double.NEGATIVE_INFINITY, new LinearConstraint[] {} ));
    }
}
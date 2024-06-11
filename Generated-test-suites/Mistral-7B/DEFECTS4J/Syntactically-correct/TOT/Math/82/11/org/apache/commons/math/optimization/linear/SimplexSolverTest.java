package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.linear.LinearConstraint;
import org.apache.commons.math.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math.optim.linear.SimplexSolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test a simple linear optimization problem.
     */
    @Test
    public void testSimpleLinearOptimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-2.5, -4}, new double[]{0, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(2, new double[]{1, 1}, 5),
            new LinearConstraint(3, new double[]{1, 1}, 7),
            new LinearConstraint(1, new double[]{1, 1}, 3),
            new LinearConstraint(1, new double[]{1, 1}, 4)
        };
        PointValuePair solution = new SimplexSolver(1e-6).doOptimize();
        assertEquals(0, solution.getPoint()[0], 1e-6);
        assertEquals(2, solution.getPoint()[1], 1e-6);
        assertEquals(-12, solution.getValue(), 1e-6);
    }

    /**
     * Test an optimization problem without a feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0, -2}, new double[]{0, 0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(1, new double[]{1, 1}, 0),
            new LinearConstraint(1, new double[]{1, 1}, 1)
        };
        new SimplexSolver(1e-6).doOptimize();
    }

    /**
     * Test an optimization problem with an unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{0, 0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(1, new double[]{1, 1}, 0)
        };
        new SimplexSolver(1e-6).doOptimize();
    }

    /**
     * Test an optimization problem with a maximal number of iterations exceeded.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0, 0}, new double[]{0, 0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        new SimplexSolver(1e-6, 1).doOptimize();
    }

}
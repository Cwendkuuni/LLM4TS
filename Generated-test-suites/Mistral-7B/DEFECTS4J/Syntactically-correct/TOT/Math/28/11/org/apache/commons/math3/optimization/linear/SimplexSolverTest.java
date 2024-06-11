package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction.Bound;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for the {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    /**
     * A simple test for the solver's optimization capabilities.
     * @throws Exception if an error occurs
     */
    @Test
    public void testOptimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {2.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0}, 0),
            new LinearConstraint(new double[]{-1.0, 1.0}, 0),
            new LinearConstraint(new double[]{1.0}, 1),
            new LinearConstraint(new double[]{-1.0, 1.0}, 2)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(1.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{1.0}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with constraints that are not satisfied.
     * @throws Exception if an error occurs
     */
    @Test
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {2.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0}, 0),
            new LinearConstraint(new double[]{-1.0, 1.0}, 0),
            new LinearConstraint(new double[]{1.0}, 1),
            new LinearConstraint(new double[]{-1.0, 1.0}, 1)
        };
        SimplexSolver solver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> solver.doOptimize());
    }

    /**
     * A test for the solver's optimization capabilities in the case of an unbounded solution.
     * @throws Exception if an error occurs
     */
    @Test
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {2.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize());
    }

    /**
     * A test for the solver's optimization capabilities with a goal of MIN.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMinimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {2.0}}, 0, Bound.MINIMIZE);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0}, 0),
            new LinearConstraint(new double[]{-1.0, 1.0}, 0),
            new LinearConstraint(new double[]{1.0}, 1),
            new LinearConstraint(new double[]{-1.0, 1.0}, 2)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(-2.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.0}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with a goal of MAX.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMaximization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {
            2.0}}, 0, Bound.MAXIMIZE);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0}, 0),
            new LinearConstraint(new double[]{-1.0, 1.0}, 0),
            new LinearConstraint(new double[]{1.0}, 1),
            new LinearConstraint(new double[]{-1.0, 1.0}, 2)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{1.0}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with constraints that are unbounded, but still feasible.
     * @throws Exception if an error occurs
     */
    @Test
    public void testUnboundedButFeasibleConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {2.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0}, Double.POSITIVE_INFINITY),
            new LinearConstraint(new double[]{-1.0, 1.0}, Double.POSITIVE_INFINITY),
            new LinearConstraint(new double[]{1.0}, 1),
            new LinearConstraint(new double[]{-1.0, 1.0}, 2)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(1.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{1.0}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with no constraints.
     * @throws Exception if an error occurs
     */
    @Test
    public void testNoConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {2.0}}, 0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.5}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with an empty objective function.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEmptyObjectiveFunction() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        assertNull(solver.doOptimize());
    }

    /**
     * A test for the solver's optimization capabilities with an empty constraint list.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEmptyConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {2.0}}, 0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.5}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with constraints that may cause numeric instability.
     * @throws Exception if an error occurs
     */
    @Test
    public void testNumericStability() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0e-12, 123.0}, {-1.0e-12, -123.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0e-12, 123.0}, 0),
            new LinearConstraint(new double[]{1.0e-12, -123.0}, 0),
            new LinearConstraint(new double[]{1.0e308, 0}, Double.POSITIVE_INFINITY)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(Double.POSITIVE_INFINITY, solution.getValue(), 1e-6);
        assertEquals(new double[]{Double.POSITIVE_INFINITY}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with a large number of constraints.
     * @throws Exception if an error occurs
     */
    @Test
    public void testLargeNumberOfConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0}, {1.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[1000000];
        for (int i = 0; i < constraints.length; i++) {
            constraints[i] = new LinearConstraint(new double[]{1.0}, i);
        }
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.0}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with a large number of variables.
     * @throws Exception if an error occurs
     */
    @Test
    public void testLargeNumberOfVariables() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[10];
        for (int i = 0; i < constraints.length; i++) {
            constraints[i] = new LinearConstraint(new double[]{1.0, 1.0}, i);
        }
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.5, 0.5}, solution.getPoint());
    }

    /**
     * A test for the solver's optimization capabilities with a large number of iterations.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMaxIterations() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {2.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(1);
        try {
            solver.doOptimize();
            assertFalse(true);
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

}
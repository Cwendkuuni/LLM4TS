package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.junit.Test;
import org.junit.Assert.*;

import org.apache.commons.math.optim.PointValuePair;
import org.apache.commons.math.optim.RealPointValuePair;
import org.apache.commons.math.optim.linear.LinearConstraint;
import org.apache.commons.math.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math.optim.linear.SimplexSolver;
import org.apache.commons.math.optim.linear.SimplexTableau;

/**
 * Tests for SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test for solver with default settings and simple problem.
     */
    @Test
    public void testDefaultSettings() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {2.0, -3.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0}}, Operator.LEQ, 5.0),
                new LinearConstraint(new double[][]{{-1.0, 1.0}, {-1.0}}, Operator.LEQ, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair result = solver.optimize(f, constraints, null, false, 1e-6);
        assertEquals(new double[]{1.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(-3.0, result.getValue(), 1e-6);
    }

    /**
     * Test for solver with custom settings and simple problem.
     */
    @Test
    public void testCustomSettings() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {2.0, -3.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0}}, Operator.LEQ, 5.0),
                new LinearConstraint(new double[][]{{-1.0, 1.0}, {-1.0}}, Operator.LEQ, 1.0)
        };
        SimplexSolver solver = new SimplexSolver(1e-7);
        RealPointValuePair result = solver.optimize(f, constraints, null, false, 1e-6);
        assertEquals(new double[]{1.0, 0.0}, result.getPoint(), 1e-7);
        assertEquals(-3.0, result.getValue(), 1e-7);
    }

    /**
     * Test for solver with error handling.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0}, {1.0}}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, null, false, 1e-6);
    }

    /**
     * Test for solver with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {2.0, -3.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0}}, Operator.LEQ, 5.0),
                new LinearConstraint(new double[][]{{-1.0, 1.0}, {-1.0}}, Operator.LEQ, 0.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, null, false, 1e-6);
    }

    /**
     * Test for solver with MaxIterationsExceededException.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {2.0, -3.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0}}, Operator.LEQ, 5.0),
                new LinearConstraint(new double[][]{{-1.0, 1.0}, {-1.0}}, Operator.LEQ, 1.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(f, constraints, null, false, 1e-6);
    }
}
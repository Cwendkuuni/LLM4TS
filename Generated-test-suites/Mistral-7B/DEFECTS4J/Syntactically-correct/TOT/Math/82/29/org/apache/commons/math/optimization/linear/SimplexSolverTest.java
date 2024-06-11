package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link SimplexSolver} class.
 */
public class SimplexSolverTest {
    private SimplexSolver solver;
    private LinearObjectiveFunction f;
    private LinearConstraintSet constraints;
    private LinearPointValuePair solution;

    @Before
    public void setUp() {
        double[][] coefficients = {
            {1.0, 1.0, -1.0, 2.0},
            {-1.0, 2.0, 1.0, 1.0},
            {2.0, -1.0, 1.0, 2.0},
            {-3.0, 1.0, -1.0, 3.0}
        };
        double[] rhs = {4.0, 4.0, 6.0, 6.0};
        f = new LinearObjectiveFunction(coefficients[0], 0);
        constraints = new LinearConstraintSet();
        for (int i = 0; i < coefficients.length; i++) {
            LinearConstraint constraint = new LinearConstraint(coefficients[i], rhs[i], SimpleGoalType.EQUALS);
            constraints.addConstraint(constraint);
        }
        constraints.addConstraint(new NonNegativeConstraint(3));
        solver = new SimplexSolver(1.0e-6);
        solution = null;
    }

    /**
     * Test for optimal solution.
     */
    @Test
    public void testOptimalSolution() {
        try {
            solution = solver.optimize();
            PointValuePair pointValuePair = solution.getPoint();
            double[] x = pointValuePair.getPoint();
            Assert.assertArrayEquals(new double[]{3.0, 0.0, 0.0, 1.5}, x, 1.0e-6);
            Assert.assertEquals(6.0, solution.getValue(), 1.0e-6);
        } catch (UnboundedSolutionException | NoFeasibleSolutionException | MaxEvalExceededException e) {
            Assert.fail("Should have found an optimal solution: " + e.getMessage());
        }
    }

    /**
     * Test for unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction fUnbounded = new LinearObjectiveFunction(
            new double[][]{{0.0, 1.0}, {-1.0, 1.0}}, 0);
        LinearConstraintSet constraintsUnbounded = new LinearConstraintSet();
        LinearConstraint constraintUnbounded = new LinearConstraint(
            new double[][]{new double[]{1.0, 1.0}}, 0.0, SimpleGoalType.LEQ);
        constraintsUnbounded.addConstraint(constraintUnbounded);
        solver = new SimplexSolver(1.0e-6);
        solver.optimize();
    }

    /**
     * Test for no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction fNoFeasibleSolution = new LinearObjectiveFunction(
            new double[][]{{1.0, 1.0}, {-1.0, 1.0}, {2.0, -1.0}}, 0);
        LinearConstraintSet constraintsNoFeasibleSolution = new LinearConstraintSet();
        LinearConstraint constraintNoFeasibleSolution = new LinearConstraint(
            new double[][]{new double[]{1.0, 1.0}}, 0.0, SimpleGoalType.LEQ);
        LinearConstraint constraintNoFeasibleSolution2 = new LinearConstraint(
            new double[][]{new double[]{1.0, -1.0}}, 0.0, SimpleGoalType.LEQ);
        constraintsNoFeasibleSolution.addConstraint(constraintNoFeasibleSolution);
        constraintsNoFeasibleSolution.addConstraint(constraintNoFeasibleSolution2);
        solver = new SimplexSolver(1.0e-6);
        solver.optimize();
    }
}
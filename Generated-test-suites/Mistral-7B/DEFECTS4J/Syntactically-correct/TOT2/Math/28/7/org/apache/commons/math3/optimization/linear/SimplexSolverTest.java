package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.LinearConstraintSet;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for SimplexSolver class
 */
public class SimplexSolverTest {

    /**
     * Test for empty constraints and objective function
     */
    @Test
    public void testEmptyFunction() {
        LinearObjectiveFunction objectiveFunction = null;
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(objectiveFunction, constraints, GoalType.MINIMIZE, null, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Test for simple objective function and constraints
     */
    @Test
    public void testSimpleFunction() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{
            {1.0, 2.0}}, new double[]{3.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(
                new double[][]{
                    {1.0, 1.0},
                    {1.0, 1.0},
                    {-1.0, 1.0}},
                1.0,
                ConstraintType.EQUALITY
        ));
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(objectiveFunction, constraints, GoalType.MINIMIZE, null, null);
            assertEquals(-3.0, solution.getValue(), 0.0);
            assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 0.0);
        } catch (MaxCountExceededException | NoFeasibleSolutionException | UnboundedSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for objective function with infeasible constraints
     */
    @Test
    public void testInfeasibleConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{
            {1.0, 2.0}}, new double[]{3.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(
                new double[][]{
                    {1.0, 1.0},
                    {1.0, 1.0},
                    {-1.0, 1.0}},
                1.0,
                ConstraintType.LESS_THAN
        ));
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objectiveFunction, constraints, GoalType.MINIMIZE, null, null);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    /**
     * Test for objective function with unbounded constraints
     */
    @Test
    public void testUnboundedConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{
            {1.0, 2.0}}, new double[]{3.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(
                new double[][]{
                    {1.0, 1.0},
                    {1.0, 1.0},
                    {-1.0, Double.NEGATIVE_INFINITY}},
                1.0,
                ConstraintType.LESS_THAN
        ));
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(objectiveFunction, constraints, GoalType.MINIMIZE, null, null);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    /**
     * Test for objective function with bounds
     */
    @Test
    public void testFunctionWithBounds() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{
            {1.0, 2.0}}, new double[]{3.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(
                new double[][]{
                    {1.0, 1.0},
                    {1.0, 1.0},
                    {-1.0, 1.0}},
                1.0,
                ConstraintType.EQUALITY
        ));
        LinearConstraintSet bounds = new LinearConstraintSet();
        bounds.addConstraint(new LinearConstraint(
                new double[][]{
                    {1.0}},
                1.0,
                ConstraintType.EQUALITY
        ));
        bounds.addConstraint(new LinearConstraint(
                new double[][]{
                    {1.0}},
                1.0,
                ConstraintType.LESS_THAN
        ));
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(objectiveFunction, constraints, bounds, null, null);
            assertEquals(-3.0, solution.getValue(), 0.0);
            assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 0.0);
        } catch (MaxCountExceededException | NoFeasibleSolutionException | UnboundedSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for objective function with max iterations
     */
    @Test
    public void testMaxIterations() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{
            {1.0, 2.0},
            {1.0, 2.0}}, new double[]{3.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet bounds = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver(1);
        try {
            PointValuePair solution = solver.optimize(objectiveFunction, constraints, bounds, null, null);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    /**
     * Test for objective function with a solution with a large number of digits after the decimal point
     */
    @Test
    public void testLargeSolutionValue() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[][]{
            {1.0, 2.0}}, new double[]{3.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraintSet bounds = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(objectiveFunction, constraints, bounds, null, null);
            assertEquals(-3.0, solution.getValue(), 1e-20);
            assertArrayEquals(new double[]{1.0, 1.0}, solution.getPoint(), 0.0);
        } catch (MaxCountExceededException | NoFeasibleSolutionException | UnboundedSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}
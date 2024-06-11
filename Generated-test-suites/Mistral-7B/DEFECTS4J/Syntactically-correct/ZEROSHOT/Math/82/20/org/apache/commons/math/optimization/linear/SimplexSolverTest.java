package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearlyConstrainedMinimizationProblem;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

/**
 * Tests for SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test for SimplexSolver with 2D array of double as objective function.
     */
    @Test
    public void testSimplexSolver2DObjectiveFunction() {
        double[][] f = {{-3.0, 2.0}, {-5.0, 4.0}};
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, 1.0, 2.0, SimpleBounds.UNBOUNDED, SimpleBounds.UNBOUNDED));
        constraints.addConstraint(new LinearConstraint(1.0, 2.0, 3.0, SimpleBounds.UNBOUNDED, SimpleBounds.UNBOUNDED));
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(f);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            RealPointValuePair solution = simplexSolver.optimize(new LinearlyConstrainedMinimizationProblem(objectiveFunction, constraints, goalType));
            assertEquals("Invalid solution for testSimplexSolver2DObjectiveFunction.", new PointValuePair(2.0, 1.0), solution.getPoint());
            assertEquals("Invalid objective function value for testSimplexSolver2DObjectiveFunction.", -3.0, solution.getValue(), 0.0);
        } catch (Exception e) {
            fail("Exception was thrown during testSimplexSolver2DObjectiveFunction: " + e.getMessage());
        }
    }

    /**
     * Test for SimplexSolver with 1D array of double as objective function.
     */
    @Test
    public void testSimplexSolver1DObjectiveFunction() {
        double[] f = {-3.0, -5.0};
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, 1.0, 2.0, SimpleBounds.UNBOUNDED, SimpleBounds.UNBOUNDED));
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(f);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            RealPointValuePair solution = simplexSolver.optimize(new LinearlyConstrainedMinimizationProblem(objectiveFunction, constraints, goalType));
            assertEquals("Invalid solution for testSimplexSolver1DObjectiveFunction.", 1.0, solution.getPoint()[0], 0.0);
            assertEquals("Invalid objective function value for testSimplexSolver1DObjectiveFunction.", -3.0, solution.getValue(), 0.0);
        } catch (Exception e) {
            fail("Exception was thrown during testSimplexSolver1DObjectiveFunction: " + e.getMessage());
        }
    }

    /**
     * Test for SimplexSolver with no constraints.
     */
    @Test
    public void testSimplexSolverNoConstraints() {
        double[][] f = {{-3.0, 2.0}, {-5.0, 4.0}};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(f);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            RealPointValuePair solution = simplexSolver.optimize(new LinearlyConstrainedMinimizationProblem(objectiveFunction, null, goalType));
            assertEquals("Invalid solution for testSimplexSolverNoConstraints.", new PointValuePair(2.0, 1.0), solution.getPoint());
            assertEquals("Invalid objective function value for testSimplexSolverNoConstraints.", -3.0, solution.getValue(), 0.0);
        } catch (Exception e) {
            fail("Exception was thrown during testSimplexSolverNoConstraints: " + e.getMessage());
        }
    }

    /**
     * Test for SimplexSolver with unbounded constraints.
     */
    @Test
    public void testSimplexSolverUnboundedConstraints() {
        double[][] f = {{-3.0, 2.0}, {-5.0, 4.0}};
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, 1.0, Double.POSITIVE_INFINITY, SimpleBounds.UNBOUNDED, SimpleBounds.UNBOUNDED));
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(f);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver simplexSolver = new SimplexSolver();
        try {
            RealPointValuePair solution = simplexSolver.optimize(new LinearlyConstrainedMinimizationProblem(objectiveFunction, constraints, goalType));
            fail("Exception was expected during testSimplexSolverUnboundedConstraints.");
        } catch (NoFeasibleSolutionException e) {
            // Expected result
        } catch (Exception e) {
            fail("Unexpected exception during testSimplexSolverUnboundedConstraints: " + e.getMessage());
        }
    }
}
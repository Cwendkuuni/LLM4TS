package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.MaxCountExceededException;
import org.apache.commons.math3.optimization.UnboundedSolutionException;
import org.apache.commons.math3.optimization.goaltype.Maximize;
import org.apache.commons.math3.optimization.goaltype.Minimize;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.junit.Test;

/**
 * Test the SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test solver with an empty constraint set and an objective function.
     */
    @Test
    public void testEmptyConstraintSet() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 1.0);
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(obj);
            assertEquals(Double.NEGATIVE_INFINITY, solution.getValue(), 1e-6);
            assertNull(solution.getPoint());
        } catch (NoFeasibleSolutionException e) {
            // expected
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solver with a constraint set and an objective function that is unbounded.
     */
    @Test
    public void testUnboundedObjectiveFunction() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, Double.POSITIVE_INFINITY);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(new LinearConstraint(1.0, 1.0, 2.0, Relation.LEQUAL));
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(obj, constraints, new Maximize(), null);
            assertThrows(UnboundedSolutionException.class, () -> solver.optimize(obj, constraints, new Maximize(), null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solver with a constraint set and a bounded objective function.
     */
    @Test
    public void testBoundedObjectiveFunction() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(new LinearConstraint(1.0, 1.0, 2.0, Relation.LEQUAL));
        constraints.add(new LinearConstraint(1.0, 1.0, 3.0, Relation.GREATER_EQUAL));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution;
        try {
            solution = solver.optimize(obj, constraints, new Maximize(), null);
            assertEquals(3.0, solution.getValue(), 1e-6);
            assertEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solver with a constraint set and a bounded objective function, but with a non-zero initial epsilon.
     */
    @Test
    public void testBoundedObjectiveFunctionWithInitialEpsilon() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(new LinearConstraint(1.0, 1.0, 2.0, Relation.LEQUAL));
        constraints.add(new LinearConstraint(1.0, 1.0, 3.0, Relation.GREATER_EQUAL));
        SimplexSolver solver = new SimplexSolver(1e-3, 10);
        PointValuePair solution;
        try {
            solution = solver.optimize(obj, constraints, new Maximize(), null);
            assertEquals(3.0, solution.getValue(), 1e-3);
            assertEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 1e-3);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solver with a constraint set and a non-linear objective function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearObjectiveFunction() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, Math::pow);
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(obj, constraints, new Maximize(), null);
    }

    /**
     * Test solver with a constraint set and a non-linear objective function with initial epsilon.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonLinearObjectiveFunctionWithInitialEpsilon() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, Math::pow);
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver(1e-3, 10);
        solver.optimize(obj, constraints, new Maximize(), null);
    }

    /**
     * Test solver with a constraint set and a goal type other than MAXIMIZE or MINIMIZE.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGoalTypeOtherThanMaximizeOrMinimize() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(obj, constraints, new GoalType[0][0], null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solver with a constraint set and a large number of constraints.
     */
    @Test
    public void testLargeNumberOfConstraints() throws MaxCountExceededException {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        for (int i = 0; i < 10000; i++) {
            constraints.add(new LinearConstraint(1.0, i, 2.0, Relation.LEQUAL));
        }
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution;
        try {
            solution = solver.optimize(obj, constraints, new Maximize(), null);
            assertEquals(3.0, solution.getValue(), 1e-6);
            assertEquals(new double[]{1.0 / (10001.0), 1.0 / 10001.0}, solution.getPoint(), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solver with a constraint set and a large number of constraints with initial epsilon.
     */
    @Test
    public void testLargeNumberOfConstraintsWithInitialEpsilon() throws MaxCountExceededException {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        for (int i = 0; i < 10000; i++) {
            constraints.add(new LinearConstraint(1.0, i, 2.0, Relation.LEQUAL));
        }
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution;
        try {
            solution = solver.optimize(obj, constraints, new Maximize(), null);
            assertEquals(3.0, solution.getValue(), 1e-6);
            assertEquals(new double[]{1.0 / (10001.0), 1.0 / 10001.0}, solution.getPoint(), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solver with a constraint set and a large number of variables.
     */
    @Test
    public void testLargeNumberOfVariables() throws MaxCountExceededException {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 0.0}, {0.0, -1.0}}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        for (int i = 0; i < 10000; i++) {
            constraints.add(new LinearConstraint(1.0, i, 0.0, Relation.LEQUAL));
            constraints.add(new LinearConstraint(0.0, i, 1.0, Relation.LEQUAL));
        }
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution;
        try {
            solution = solver.optimize(obj, constraints, new Minimize(), null);
            assertEquals(0.0, solution.getValue(), 1e-6);
            assertEquals(new double[]{1.0, 0.0}, solution.getPoint(), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solver with a constraint set and a large number of variables with initial epsilon.
     */
    @Test
    public void testLargeNumberOfVariablesWithInitialEpsilon() throws MaxCountExceededException {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, 0.0}, {0.0, -1.0}}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        for (int i = 0; i < 10000; i++) {
            constraints.add(new LinearConstraint(1.0, i, 0.0, Relation.LEQUAL));
            constraints.add(new LinearConstraint(0.0, i, 1.0, Relation.LEQUAL));
        }
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution;
        try {
            solution = solver.optimize(obj, constraints, new Minimize(), null);
            assertEquals(0.0, solution.getValue(), 1e-6);
            assertEquals(new double[]{1.0, 0.0}, solution.getPoint(), 1e-6);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
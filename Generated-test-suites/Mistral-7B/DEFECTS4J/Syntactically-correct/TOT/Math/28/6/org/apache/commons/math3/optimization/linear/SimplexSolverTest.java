package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test optimization of a linear problem with a single objective function and a single linear equality constraint.
     */
    @Test
    public void testSimpleOptimization() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0}, new double[]{-1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, 1.0, Relation.EQUALS);
        constraints.addConstraint(constraint);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        assertEquals(new double[]{1.0}, solution.getPoint(), 1e-6);
        assertEquals(-1.0, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization of a linear problem with a single objective function and a single linear inequality constraint.
     */
    @Test
    public void testInequalityConstraintOptimization() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0}, new double[]{1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, 1.0, Relation.LESS_THAN);
        constraints.addConstraint(constraint);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objective, constraints);
        assertTrue(solution.getPoint()[0] < 1.0);
        assertEquals(1.0 * solution.getPoint()[0], solution.getValue(), 1e-6);
    }

    /**
     * Test optimization of a linear problem with multiple objective functions and multiple linear equality and inequality constraints.
     */
    @Test
    public void testMultipleObjectivesAndConstraintsOptimization() {
        LinearObjectiveFunction obj1 = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-2.0, 3.0});
        LinearObjectiveFunction obj2 = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, -2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, 1.0, Relation.EQUALS);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 2.0}, 2.0, Relation.LESS_THAN);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new LinearObjectiveFunction[] {obj1, obj2}, constraints);
        double[] point = solution.getPoint();
        assertEquals(1.0, point[0], 1e-6);
        assertEquals(1.0, point[1], 1e-6);
        assertEquals(-2.0 * point[0] + 3.0 * point[1], solution.getValue(), 1e-6);
        assertEquals(3.0 * point[0] - 2.0 * point[1], solution.getValue2(), 1e-6);
    }

    /**
     * Test optimization of a linear problem with unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0}, new double[]{1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objective, constraints);
    }

    /**
     * Test optimization of a linear problem with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0}, new double[]{1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, 0.0, Relation.LESS_THAN);
        constraints.addConstraint(constraint);

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objective, constraints);
    }

    /**
     * Test optimization of a linear problem with a maximal iteration count exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationCountExceeded() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{1.0}, new double[]{-1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, 1.0, Relation.EQUALS);
        constraints.addConstraint(constraint);

        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(objective, constraints);
    }
}
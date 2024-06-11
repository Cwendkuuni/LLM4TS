package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValueMap;
import org.apache.commons.math3.optim.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    /**
     * Test for the SimplexSolver with a simple linear problem.
     * @throws Exception
     */
    @Test
    public void testSimpleLinearProblem() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[]{-3.0, -2.0, 1.0}, new double[]{-6.0, -3.0, 6.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0}, Relation.EQ, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQ, 2.0));
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        PointValuePair solution = solver.optimize(objective, constraints, goalType);
        assertEquals(0.0, solution.getPoint()[0], 1.0e-6);
        assertEquals(0.0, solution.getPoint()[1], 1.0e-6);
        assertEquals(-6.0, solution.getValue(), 1.0e-6);
    }

    /**
     * Test for the SimplexSolver with an unbounded linear problem.
     * @throws Exception
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedLinearProblem() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[]{0.0, 0.0}, new double[]{0.0, 0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.optimize(objective, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test for the SimplexSolver with a linearly dependent linear problem.
     * @throws Exception
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testLinearlyDependentLinearProblem() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{0.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.EQ, 2.0));
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.optimize(objective, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test for the SimplexSolver with a linearly independent linear problem with no solution.
     * @throws Exception
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleLinearProblem() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-1.0, -1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQ, 1.0));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQ, 1.0));
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        solver.optimize(objective, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test for the SimplexSolver with a linearly independent linear problem with multiple solutions.
     * @throws Exception
     */
    @Test
    public void testMultipleLinearProblem() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{0.0, 0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQ, 1.0));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQ, 2.0));
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10);
        PointValuePair solution1 = solver.optimize(objective, constraints, GoalType.MINIMIZE);
        assertEquals(0.0, solution1.getPoint()[0], 1.0e-6);
        assertEquals(0.0, solution1.getPoint()[1], 1.0e-6);
        assertEquals(0.0, solution1.getValue(), 1.0e-6);

        PointValuePair solution2 = solver.optimize(objective, constraints, GoalType.MINIMIZE);
        assertEquals(1.0, solution2.getPoint()[0], 1.0e-6);
        assertEquals(1.0, solution2.getPoint()[1], 1.0e-6);
        assertEquals(0.0, solution2.getValue(), 1.0e-6);
    }

    /**
     * Test for the SimplexSolver with a linearly independent linear problem
     * with a large number of iterations.
     * @throws Exception
     */
    @Test
    public void testLargeNumberOfIterations() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{0.0, 0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQ, 1000.0));
        SimplexSolver solver = new SimplexSolver(1.0e-6, 10000);
        PointValuePair solution = solver.optimize(objective, constraints, GoalType.MINIMIZE);
        assertEquals(0.0, solution.getPoint()[0], 1.0e-6);
        assertEquals(0.0, solution.getPoint()[1], 1.0e-6);
        assertEquals(-1000000.0, solution.getValue(), 1.0e-6);
    }

    /**
     * Test for the SimplexSolver with a large number of variables.
     * @throws Exception
     */
    @Test
    public void testLargeNumberOfVariables() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[100], new double[100]);
        LinearConstraintSet constraints = new LinearConstraintSet();
        for (int i = 0; i < 100; i++) {
            constraints.addConstraint(new LinearConstraint(new double[]{1.0}, Relation.LEQ, 1.0));
        }
        SimplexSolver solver = new SimplexSolver(1.0e-6, 1000);
        PointValuePair solution = solver.optimize(objective, constraints, GoalType.MINIMIZE);
        // this test is only to check if the method does not throw an exception
        // the actual solution does not matter in this test
        assertEquals(null, solution);
    }

    /**
     * Test for the SimplexSolver with a large number of constraints.
     * @throws Exception
     */
    @Test
    public void testLargeNumberOfConstraints() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        for (int i = 0; i < 100000; i++) {
            constraints.addConstraint(new LinearConstraint(new double[]{1.0}, Relation.LEQ, 1.0));
        }
        SimplexSolver solver = new SimplexSolver(1.0e-6, 1000);
        PointValuePair solution = solver.optimize(objective, constraints, GoalType.MINIMIZE);
        // this test is only to check if the method does not throw an exception
        // the actual solution does not matter in this test
        assertEquals(null, solution);
    }

    /**
     * Test for the SimplexSolver with a large number of iterations and constraints.
     * @throws Exception
     */
    @Test
    public void testLargeNumberOfIterationsAndConstraints() throws Exception {
        LinearObjectiveFunction objective = new SimpleLinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        for (int i = 0; i < 100000; i++) {
            constraints.addConstraint(new LinearConstraint(new double[]{1.0}, Relation.LEQ, 1.0));
        }
        SimplexSolver solver = new SimplexSolver(1.0e-6, 100000);
        PointValuePair solution = solver.optimize(objective, constraints, GoalType.MINIMIZE);
        // this test is only to check if the method does not throw an exception
        // the actual solution does not matter in this test
        assertEquals(null, solution);
    }
}
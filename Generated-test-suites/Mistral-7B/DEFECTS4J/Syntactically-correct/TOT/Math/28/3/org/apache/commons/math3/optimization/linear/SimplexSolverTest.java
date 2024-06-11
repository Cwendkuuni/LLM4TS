package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /** Test problem with a single objective function and a single constraint. */
    @Test
    public void testLinearProblemSingleObjectiveFunctionAndConstraint() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 3d } }, 1d);
        LinearConstraint constraint = new LinearConstraint(new double[][] { { 1d, 1d }, { 1d } }, 3d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = null;
        try {
            solution = solver.optimize(func, constraint, goalType);
        } catch (NoFeasibleSolutionException e) {
            Assert.fail("Problem should be feasible.");
        } catch (MaxCountExceededException e) {
            Assert.fail("Problem should be solvable within the given maximum number of iterations.");
        } catch (UnboundedSolutionException e) {
            Assert.fail("Problem should have a bounded solution.");
        }
        Assert.assertNotNull(solution);
        Assert.assertEquals(0d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { 1d / 3d, 1d / 3d }, solution.getPoint(), 1e-6);
    }

    /** Test problem with a single objective function and an infeasible constraint. */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testLinearProblemSingleObjectiveFunctionAndInfeasibleConstraint() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 3d } }, 1d);
        LinearConstraint constraint = new LinearConstraint(new double[][] { { 1d, 1d }, { 1d } }, 2d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        solver.optimize(func, constraint, goalType);
    }

    /** Test problem with multiple objective functions and multiple constraints. */
    @Test
    public void testLinearProblemMultipleObjectiveFunctionsAndConstraints() {
        LinearObjectiveFunction func1 = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 2d } }, 1d);
        LinearObjectiveFunction func2 = new LinearObjectiveFunction(new double[][] { { -1d, 1d }, { 2d } }, 1d);
        LinearConstraint constraint1 = new LinearConstraint(new double[][] { { 1d, 1d }, { 1d } }, 3d);
        LinearConstraint constraint2 = new LinearConstraint(new double[][] { { -1d, 1d }, { 1d } }, 2d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = null;
        try {
            solution = solver.optimize(new LinearObjectiveFunction[] { func1, func2 },
                                       new LinearConstraint[] { constraint1, constraint2 },
                                       goalType);
        } catch (NoFeasibleSolutionException e) {
            Assert.fail("Problem should be feasible.");
        } catch (MaxCountExceededException e) {
            Assert.fail("Problem should be solvable within the given maximum number of iterations.");
        } catch (UnboundedSolutionException e) {
            Assert.fail("Problem should have a bounded solution.");
        }
        Assert.assertNotNull(solution);
        Assert.assertEquals(0d, solution.getValue1(), 1e-6);
        Assert.assertEquals(0d, solution.getValue2(), 1e-6);
        Assert.assertArrayEquals(new double[] { 1d / 2d, 1d / 2d }, solution.getPoint(), 1e-6);
    }

    /** Test problem with a single objective function and no constraints. */
    @Test
    public void testLinearProblemSingleObjectiveFunctionAndNoConstraints() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = null;
        try {
            solution = solver.optimize(func, null, goalType);
        } catch (NoFeasibleSolutionException e) {
            Assert.fail("Problem should be feasible.");
        } catch (MaxCountExceededException e) {
            Assert.fail("Problem should be solvable within the given maximum number of iterations.");
        } catch (UnboundedSolutionException e) {
            Assert.fail("Problem should have a bounded solution.");
        }
        Assert.assertNotNull(solution);
        Assert.assertEquals(0d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { 0d, 0d }, solution.getPoint(), 1e-6);
    }

    /** Test problem with a single objective function and an empty constraint list. */
    @Test
    public void testLinearProblemSingleObjectiveFunctionAndEmptyConstraints() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = null;
        try {
            solution = solver.optimize(func, new LinearConstraint[0], goalType);
        } catch (NoFeasibleSolutionException e) {
            Assert.fail("Problem should be feasible.");
        } catch (MaxCountExceededException e) {
            Assert.fail("Problem should be solvable within the given maximum number of iterations.");
        } catch (UnboundedSolutionException e) {
            Assert.fail("Problem should have a bounded solution.");
        }
        Assert.assertNotNull(solution);
        Assert.assertEquals(0d, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { 0d, 0d }, solution.getPoint(), 1e-6);
    }

    /** Test problem with multiple objective functions and empty constraint list. */
    @Test
    public void testLinearProblemMultipleObjectiveFunctionsAndEmptyConstraints() {
        LinearObjectiveFunction func1 = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        LinearObjectiveFunction func2 = new LinearObjectiveFunction(new double[][] { { -1d, 1d }, { 0d } }, 0d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        PointValuePair solution = null;
        try {
            solution = solver.optimize(new LinearObjectiveFunction[] { func1, func2 },
                                       new LinearConstraint[0], goalType);
        } catch (NoFeasibleSolutionException e) {
            Assert.fail("Problem should be feasible.");
        } catch (MaxCountExceededException e) {
            Assert.fail("Problem should be solvable within the given maximum number of iterations.");
        } catch (UnboundedSolutionException e) {
            Assert.fail("Problem should have a bounded solution.");
        }
        Assert.assertNotNull(solution);
        Assert.assertEquals(0d, solution.getValue1(), 1e-6);
        Assert.assertEquals(0d, solution.getValue2(), 1e-6);
        Assert.assertArrayEquals(new double[] { 0d, 0d }, solution.getPoint(), 1e-6);
    }

    /** Test problem with a single objective function and a constraint with a large right hand side. */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testLinearProblemSingleObjectiveFunctionAndLargeConstraintRightHandSide() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        LinearConstraint constraint = new LinearConstraint(new double[][] { { 1d, 1d }, { 1e6 } }, 0d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        solver.optimize(func, constraint, goalType);
    }

    /** Test problem with multiple objective functions and a constraint with a large right hand side. */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testLinearProblemMultipleObjectiveFunctionsAndLargeConstraintRightHandSide() {
        LinearObjectiveFunction func1 = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        LinearObjectiveFunction func2 = new LinearObjectiveFunction(new double[][] { { -1d, 1d }, { 0d } }, 0d);
        LinearConstraint constraint = new LinearConstraint(new double[][] { { 1d, 1d }, { 1e6 } }, 0d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        solver.optimize(new LinearObjectiveFunction[] { func1, func2 }, constraint, goalType);
    }

    /** Test problem with multiple objective functions and a constraint with a large coefficient. */
    @Test(expected = UnboundedSolutionException.class)
    public void testLinearProblemMultipleObjectiveFunctionsAndLargeConstraintCoefficient() {
        LinearObjectiveFunction func1 = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        LinearObjectiveFunction func2 = new LinearObjectiveFunction(new double[][] { { -1d, 1d }, { 1e6 } }, 0d);
        LinearConstraint constraint = new LinearConstraint(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        solver.optimize(new LinearObjectiveFunction[] { func1, func2 }, constraint, goalType);
    }

    /** Test problem with multiple objective functions and a constraint with a large constant term. */
    @Test(expected = UnboundedSolutionException.class)
    public void testLinearProblemMultipleObjectiveFunctionsAndLargeConstraintConstantTerm() {
        LinearObjectiveFunction func1 = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        LinearObjectiveFunction func2 = new LinearObjectiveFunction(new double[][] { { -1d, 1d }, { 0d } }, 0d);
        LinearConstraint constraint = new LinearConstraint(new double[][] { { 1d, 1d }, { 1e6 } }, 1e6);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        solver.optimize(new LinearObjectiveFunction[] { func1, func2 }, constraint, goalType);
    }

    /** Test problem with multiple objective functions and a constraint with large coefficients and a large constant term. */
    @Test(expected = UnboundedSolutionException.class)
    public void testLinearProblemMultipleObjectiveFunctionsAndLargeConstraints() {
        LinearObjectiveFunction func1 = new LinearObjectiveFunction(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        LinearObjectiveFunction func2 = new LinearObjectiveFunction(new double[][] { { -1d, 1d }, { 1e6 } }, 0d);
        LinearConstraint constraint1 = new LinearConstraint(new double[][] { { 1d, 1d }, { 0d } }, 0d);
        LinearConstraint constraint2 = new LinearConstraint(new double[][] { { 1d, 1d }, { 1e6 } }, 1e6);
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver(1e-6, 10);
        solver.optimize(new LinearObjectiveFunction[] { func1, func2 },
                        new LinearConstraint[] { constraint1, constraint2 }, goalType);
    }
}
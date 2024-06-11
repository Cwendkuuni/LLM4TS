package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.GoalType;
import org.apache.commons.math3.optim.nonlinear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSetImpl;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunctionImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link SimplexSolver} class.
 */
public class SimplexSolverTest {

    /**
     * Test for solving a simple linear optimization problem with a single constraint.
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testSimpleOptimization()
        throws Exception {
        // Arrange
        LinearObjectiveFunction objective = new LinearObjectiveFunctionImpl(new double[]{-1.0, 2.0}, 5.0);
        LinearConstraint constraint = new LinearConstraintImpl(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSetImpl();
        constraints.addConstraint(constraint);
        SimplexSolver solver = new SimplexSolver();

        // Act
        PointValuePair solution = solver.optimize(objective, constraints, GoalType.MINIMIZE, null, null);

        // Assert
        Assert.assertEquals(0d, solution.getPoint()[0], 1e-6);
        Assert.assertEquals(5d, solution.getValue(), 1e-6);
    }

    /**
     * Test for solving a linear optimization problem with multiple constraints and multiple variables.
     * @throws Exception if an error occurs during testing
     */
    @Test
    public void testMultipleConstraints()
        throws Exception {
        // Arrange
        LinearObjectiveFunction objective = new LinearObjectiveFunctionImpl(new double[]{-1.0, 2.0, 3.0}, 10.0);
        LinearConstraint constraint1 = new LinearConstraintImpl(new double[]{-1.0, 1.0, 2.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraintImpl(new double[]{-1.0, 3.0, 2.0}, 5.0);
        LinearConstraintSet constraints = new LinearConstraintSetImpl();
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);
        SimplexSolver solver = new SimplexSolver();

        // Act
        PointValuePair solution = solver.optimize(objective, constraints, GoalType.MINIMIZE, null, null);

        // Assert
        Assert.assertEquals(1d / 6, solution.getPoint()[0], 1e-6);
        Assert.assertEquals(1 / 2, solution.getPoint()[1], 1e-6);
        Assert.assertEquals(5d / 2, solution.getPoint()[2], 1e-6);
        Assert.assertEquals(10d, solution.getValue(), 1e-6);
    }

    /**
     * Test for solving a linear optimization problem with a single constraint and unbounded solution.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution()
        throws Exception {
        // Arrange
        LinearObjectiveFunction objective = new LinearObjectiveFunctionImpl(new double[]{0.0, 2.0}, 5.0);
        LinearConstraint constraint = new LinearConstraintImpl(new double[]{0.0, 1.0}, 2.0);
        LinearConstraintSet constraints = new LinearConstraintSetImpl();
        constraints.addConstraint(constraint);
        SimplexSolver solver = new SimplexSolver();

        // Act and Assert
        solver.optimize(objective, constraints, GoalType.MINIMIZE, null, null);
    }

    /**
     * Test for solving a linear optimization problem with a single constraint and no feasible solution.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution()
        throws Exception {
        // Arrange
        LinearObjectiveFunction objective = new LinearObjectiveFunctionImpl(new double[]{0.0, 2.0}, 0.0);
        LinearConstraint constraint = new LinearConstraintImpl(new double[]{0.0, 1.0}, 1.0);
        LinearConstraintSet constraints = new LinearConstraintSetImpl();
        constraints.addConstraint(constraint);
        SimplexSolver solver = new SimplexSolver();

        // Act and Assert
        solver.optimize(objective, constraints, GoalType.MINIMIZE, null, null);
    }

    /**
     * Test for solving a linear optimization problem with a maximal iteration count.
     * @throws Exception if an error occurs during testing
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations()
        throws Exception {
        // Arrange
        LinearObjectiveFunction objective = new LinearObjectiveFunctionImpl(new double[]{0.0, 2.0}, 5.0);
        LinearConstraint constraint = new LinearConstraintImpl(new double[]{0.0, 1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSetImpl();
        constraints.addConstraint(constraint);
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(1);

        // Act and Assert
        solver.optimize(objective, constraints, GoalType.MINIMIZE, null, null);
    }

}
package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.linear.GoalType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet.Relation;
import org.apache.commons.math3.optim.nonlinear.linear.LinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.linear.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for SimplexSolver.
 */
public class SimplexSolverTest {

    /**
     * Test that SimplexSolver correctly solves a simple linear optimization problem with a single objective function.
     */
    @Test
    public void testSimpleLinearOptimization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1., 4.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 0),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 0) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver();

        PointValuePair result = optimizer.optimize(objF, linConstraints, k);
        assertEquals("Incorrect optimal value", 0d, result.getValue(), 1e-6);
        assertArrayEquals("Incorrect optimal point", new double[]{1d, 1d}, result.getPoint(), 1e-6);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with a single objective function and a bound constraint.
     */
    @Test
    public void testLinearOptimizationWithBoundConstraint() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1., 4.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 2),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 0) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver();

        PointValuePair result = optimizer.optimize(objF, linConstraints, k);
        assertEquals("Incorrect optimal value", -2d, result.getValue(), 1e-6);
        assertArrayEquals("Incorrect optimal point", new double[]{1d, 1d}, result.getPoint(), 1e-6);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with multiple objective functions.
     */
    @Test
    public void testMultipleObjectiveFunctions() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1., 2.}, 0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1., 1.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 0),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 0) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f1, f2);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver();

        PointValuePair result = optimizer.optimize(objF, linConstraints, k);
        assertEquals("Incorrect number of objectives", 2, result.getNumberOfObjectives(), 1e-6);
        assertEquals("Incorrect optimal value for the first objective", 0d, result.getObjectiveValue(0), 1e-6);
        assertEquals("Incorrect optimal value for the second objective", 0d, result.getObjectiveValue(1), 1e-6);
        assertArrayEquals("Incorrect optimal point", new double[]{0d, 0d}, result.getPoint(), 1e-6);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with multiple objective functions and multiple constraints.
     */
    @Test
    public void testMultipleObjectiveFunctionsAndConstraints() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1., 2.}, 0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1., -1.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 0),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 0),
                                                         new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 1),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 1) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f1, f2);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver();

        PointValuePair result = optimizer.optimize(objF, linConstraints, k);
        assertEquals("Incorrect number of objectives", 2, result.getNumberOfObjectives(), 1e-6);
        assertEquals("Incorrect optimal value for the first objective", 1d, result.getObjectiveValue(0), 1e-6);
        assertEquals("Incorrect optimal value for the second objective", 1d, result.getObjectiveValue(1), 1e-6);
        assertArrayEquals("Incorrect optimal point", new double[]{1d / 2d, 1d / 2d}, result.getPoint(), 1e-6);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with a single objective function and unbounded constraints.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1., 4.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 0),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 0),
                                                         new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, Double.NEGATIVE_INFINITY),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, Double.POSITIVE_INFINITY) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver();

        optimizer.optimize(objF, linConstraints, k);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with multiple objective functions and unbounded constraints.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testMultipleObjectiveFunctionsAndUnboundedConstraints() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1., 2.}, 0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1., 1.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 0),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 0),
                                                         new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, Double.NEGATIVE_INFINITY),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, Double.POSITIVE_INFINITY) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f1, f2);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver();

        optimizer.optimize(objF, linConstraints, k);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with a single objective function and infeasible constraints.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testInfeasibleConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1., 4.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 2),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 3) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver();

        optimizer.optimize(objF, linConstraints, k);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with multiple objective functions and infeasible constraints.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testMultipleObjectiveFunctionsAndInfeasibleConstraints() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1., 2.}, 0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1., 1.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 2),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 3) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f1, f2);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver();

        optimizer.optimize(objF, linConstraints, k);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with a single objective function and a maximum iteration limit.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsLimit() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1., 4.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 0),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 0) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver(1);

        optimizer.optimize(objF, linConstraints, k);
    }

    /**
     * Test that SimplexSolver correctly handles a linear optimization problem with multiple objective functions and a maximum iteration limit.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMultipleObjectiveFunctionsAndMaxIterationsLimit() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-1., 2.}, 0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1., 1.}, 0);
        LinearConstraint[] g = new LinearConstraint[] { new LinearConstraint(new double[]{-1., 1.}, Relation.LEQ, 0),
                                                         new LinearConstraint(new double[]{1., 1.}, Relation.GEQ, 0) };
        LinearConstraintSet h = new LinearConstraintSet(g);
        GoalType k = SimpleGoalType.MINIMIZE;
        LinearObjectiveFunctionSet objF = new LinearObjectiveFunctionSet(f1, f2);
        LinearConstraintSet linConstraints = new LinearConstraintSet(g);
        LinearOptimizer optimizer = new SimplexSolver(1);

        optimizer.optimize(objF, linConstraints, k);
    }
}
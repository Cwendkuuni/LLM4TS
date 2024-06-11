package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.MathUtils;
 import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test solving a simple linear optimization problem.
     */
    @Test
    public void testSimpleProblem() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 4.0}, {2.0, 3.0}}, new double[]{-3.0, -1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
               new LinearConstraint(new double[][]{{1.0, 1.0}}, 5.0),
               new LinearConstraint(new double[][]{{2.0, 1.0}}, 3.0),
               new LinearConstraint(new double[][]{{1.0, 2.0}}, 4.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, constraints, goalType);
        PointValuePair expectedSolution = new PointValuePair(new double[]{-3.0 / 13.0, -1.0 / 13.0}, -19.0 / 13.0);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 1e-6);
        assertArrayEquals(expectedSolution.getPoint(), solution.getPoint(), 1e-6);
    }

    /**
     * Test that an exception is thrown if the objective function matrix is empty.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testEmptyObjectiveFunctionMatrix() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{}, new double[]{});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test that an exception is thrown if the objective function has no variables.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNoVariablesInObjectiveFunction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{0.0}}, new double[]{-3.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test that an exception is thrown if the optimization problem has no constraints.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNoConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 4.0}, {2.0, 3.0}}, new double[]{-3.0, -1.0});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, null, GoalType.MINIMIZE);
    }

    /**
     * Test that an exception is thrown if the optimization problem has no objective function.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNoObjectiveFunction() {
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(null, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test that an exception is thrown if the optimization problem has no solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {1.0, -1.0}}, new double[]{0.0, 0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, GoalType.MINIMIZE);
    }

    /**
     * Test that an exception is thrown if the optimization problem is unbounded.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedProblem() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {0.0, 0.0}}, new double[]{0.0, Double.POSITIVE_INFINITY});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints, GoalType.MINIMIZE);
    }
}
package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction objectiveFunction;
    private List<LinearConstraint> constraints;
    private GoalType goalType;

    @Parameterized.Parameters(name = "{index}: test solver with constraints {0} and goal type {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Arrays.asList(
                        new LinearConstraint(1.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
                        new LinearConstraint(1.0, 2.0, 0.0, Double.POSITIVE_INFINITY)),
                        GoalType.MINIMIZE},
                {Arrays.asList(
                        new LinearConstraint(1.0, 1.0, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
                        new LinearConstraint(1.0, 2.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(1.0, 3.0, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)),
                        GoalType.MAXIMIZE},
                {Arrays.asList(
                        new LinearConstraint(1.0, 1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(1.0, 2.0, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)),
                        GoalType.MINIMIZE}
        });
    }

    @Parameterized.Constructor
    public SimplexSolverTest(List<LinearConstraint> constraints, GoalType goalType) {
        this.constraints = constraints;
        this.goalType = goalType;
        this.objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        this.solver = new SimplexSolver();
    }

    @Before
    public void setUp() {
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(objectiveFunction, constraints, goalType);
        solver.setRestrictToNonNegative(true);
        try {
            PointValuePair solution = solver.optimize(problem);
            System.out.println("Solution: " + solution);
        } catch (UnboundedSolutionException | NoFeasibleSolutionException | MaxEvalExceededException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Test
    public void testSolution() {
        PointValuePair solution = solver.getLastSolution();
        Assert.assertNotNull(solution);
        double[] expectedSolution = new double[]{0.0, 0.0};
        if (goalType == GoalType.MINIMIZE) {
            Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 0.001);
            Assert.assertTrue(solver.isOptimal(solver.getTableau()));
        } else {
            Assert.assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, solution.getPoint(), 0.001);
            Assert.assertFalse(solver.isOptimal(solver.getTableau()));
        }
    }

    @Test
    public void testObjectiveFunctionValue() {
        PointValuePair solution = solver.getLastSolution();
        double expectedValue = 0.0;
        if (goalType == GoalType.MAXIMIZE) {
            expectedValue = Double.POSITIVE_INFINITY;
        }
        double objectiveValue = objectiveFunction.value(solution.getPoint());
        Assert.assertEquals(expectedValue, objectiveValue, 0.001);
    }
}
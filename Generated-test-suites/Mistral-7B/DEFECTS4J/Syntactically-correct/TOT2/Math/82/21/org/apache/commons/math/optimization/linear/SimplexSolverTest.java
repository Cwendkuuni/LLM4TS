import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValueMap;
import org.apache.commons.math3.optim.SimpleLinearConstraint;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import org.junit.Assert;

import static org.apache.commons.math3.optim.MaxEval.UNBOUNDED;
import static org.apache.commons.math3.optim.MaxEval.RELATIVE;
import static org.apache.commons.math3.optim.MaxEval.ABSOLUTE;

import org.apache.commons.math.optimization.linear.SimplexSolver;

public class SimplexSolverTest {

    @Test
    public void testSimpleCase() {
        // Create a simple test problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), new double[]{-1, 1});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new SimpleLinearConstraint(new double[]{-3, -2}, 2.0);
        constraints[1] = new SimpleLinearConstraint(new double[]{1, 1}, 3.0);
        SimplexSolver solver = new SimplexSolver();

        // Solve the problem and check the solution
        PointValuePair solution = solver.optimize(new SimpleOptimizationProblem(f, constraints));
        Assert.assertEquals(new double[]{1.0/7.0, 2.0/7.0}, solution.getPoint(), 0.001);
        Assert.assertEquals(-2.0/7.0, solution.getValue(), 0.001);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        // Create a problem with an unbounded solution
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), new double[]{-1, 1});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new SimpleLinearConstraint(new double[]{-3, -2}, 2.0);
        constraints[1] = new SimpleLinearConstraint(new double[]{1, 1}, 0.0);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(new SimpleOptimizationProblem(f, constraints));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        // Create a problem with no feasible solution
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), new double[]{-1, 1});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new SimpleLinearConstraint(new double[]{-3, -2}, 2.0);
        constraints[1] = new SimpleLinearConstraint(new double[]{1, 1}, 4.0);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(new SimpleOptimizationProblem(f, constraints));
    }

    @Test
    public void testRestrictToNonNegative() {
        // Create a problem with non-negative constraints
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), new double[]{-1, 1});
        LinearConstraint[] constraints = new LinearConstraint[3];
        constraints[0] = new SimpleLinearConstraint(new double[]{-3, -2}, 2.0);
        constraints[1] = new SimpleLinearConstraint(new double[]{1, 1}, 3.0);
        constraints[2] = new SimpleLinearConstraint(new double[]{0, 0}, Double.POSITIVE_INFINITY);
        SimplexSolver solver = new SimplexSolver(true);
        PointValuePair solution = solver.optimize(new SimpleOptimizationProblem(f, constraints));
        Assert.assertEquals(new double[]{3.0/7.0, 0.0}, solution.getPoint(), 0.001);
        Assert.assertEquals(-2.0/7.0, solution.getValue(), 0.001);
    }

    @Test
    public void testGoalType() {
        // Test different goal types
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), new double[]{-1, 1});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new SimpleLinearConstraint(new double[]{-3, -2}, 2.0);
        constraints[1] = new SimpleLinearConstraint(new double[]{1, 1}, 3.0);
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solutionMin = solver.optimize(new SimpleOptimizationProblem(f, constraints, GoalType.MINIMIZE));
        Assert.assertEquals(new double[]{1.0/7.0, 2.0/7.0}, solutionMin.getPoint(), 0.001);
        Assert.assertEquals(-2.0/7.0, solutionMin.getValue(), 0.001);

        PointValuePair solutionMax = solver.optimize(new SimpleOptimizationProblem(f, constraints, GoalType.MAXIMIZE));
        Assert.assertEquals(new double[]{1.0/5.0, 1.0/5.0}, solutionMax.getPoint(), 0.001);
        Assert.assertEquals(2.0/5.0, solutionMax.getValue(), 0.001);

        PointValuePair solutionAbs = solver.optimize(new SimpleOptimizationProblem(f, constraints, GoalType.ABSOLUTE));
        Assert.assertEquals(new double[]{1.0/7.0, 2.0/7.0}, solutionAbs.getPoint(), 0.001);
        Assert.assertEquals(Math.abs(-2.0/7.0), solutionAbs.getValue(), 0.001);

        PointValuePair solutionRel = solver.optimize(new SimpleOptimizationProblem(f, constraints, GoalType.RELATIVE));
        Assert.assertEquals(new double[]{1.0/7.0, 2.0/7.0}, solutionRel.getPoint(), 0.001);
        Assert.assertEquals(-2.0/7.0 / (Math.abs(-2.0/7.0) + 1e-9), solutionRel.getValue(), 0.001);
    }

    @Test
    public void testMaxIterations() {
        // Test maximal number of iterations
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), new double[]{-1, 1});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new SimpleLinearConstraint(new double[]{-3, -2}, 2.0);
        constraints[1] = new SimpleLinearConstraint(new double[]{1, 1}, 3.0);
        SimplexSolver solver = new SimplexSolver(10);
        try {
            solver.optimize(new SimpleOptimizationProblem(f, constraints));
            Assert.fail();
        } catch (OptimizationException e) {
            // expected
        }
    }
}
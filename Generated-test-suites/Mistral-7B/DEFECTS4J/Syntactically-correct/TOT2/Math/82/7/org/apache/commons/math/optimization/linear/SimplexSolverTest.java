import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testEmptyProblem() {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        Assert.assertNull(solution);
    }

    @Test
    public void testSimpleProblem() {
        SimplexSolver solver = new SimplexSolver();
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(
                new LinearObjectiveFunction(new double[]{0.5, 1.5}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relation.LEQUAL, 1.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, Relation.LEQUAL, 1.0)
                }, null, GoalType.MINIMIZE, null, 1e-6);
        PointValuePair solution = solver.doOptimize(problem);
        Assert.assertEquals(new RealPointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, 2.0 / 3.0), solution, 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedProblem() {
        SimplexSolver solver = new SimplexSolver();
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(
                new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relation.LEQUAL, 1.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, Relation.LEQUAL, 1.0)
                }, null, GoalType.MINIMIZE, null, 1e-6);
        solver.doOptimize(problem);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionProblem() {
        SimplexSolver solver = new SimplexSolver();
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(
                new LinearObjectiveFunction(new double[]{0.5, 1.5}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relation.LEQUAL, 0.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, Relation.LEQUAL, 0.0)
                }, null, GoalType.MINIMIZE, null, 1e-6);
        solver.doOptimize(problem);
    }

    @Test
    public void testGoalTypeMaximize() {
        SimplexSolver solver = new SimplexSolver();
        SimpleOptimizationProblem problem = new SimpleOptimizationProblem(
                new LinearObjectiveFunction(new double[]{0.5, 1.5}, new double[]{1.0, 1.0}),
                new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relation.LEQUAL, 1.0),
                        new LinearConstraint(new double[]{1.0, -1.0}, Relation.LEQUAL, 1.0)
                }, null, GoalType.MAXIMIZE, null, 1e-6);
        PointValuePair solution = solver.doOptimize(problem);
        Assert.assertEquals(new RealPointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, -2.0 / 3.0), solution, 1e-6);
    }
}
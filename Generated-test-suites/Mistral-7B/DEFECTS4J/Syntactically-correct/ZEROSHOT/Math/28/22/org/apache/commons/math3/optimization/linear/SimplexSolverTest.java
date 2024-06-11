import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearOptimizationProblem;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.test.AbstractTestCase;
import org.junit.Test;

public class SimplexSolverTest extends AbstractTestCase {

    @Test
    public void testSolveExampleProblem() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{1.0}, {2.0}}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(1.0, new double[][]{{-1.0, 2.0}, {-3.0, 1.0}}, null, Relationship.LEQ, 1.0),
                new LinearConstraint(1.0, new double[][]{{2.0, -1.0}, {1.0, -1.0}}, null, Relationship.LEQ, 1.0)
        };

        SimplexSolver solver = new SimplexSolver();
        LinearOptimizationProblem problem = new LinearOptimizationProblem(objective, constraints, null, null, null);

        PointValuePair solution = solver.optimize(problem);

        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(3.0, solution.getValue(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{1.0}, {2.0}}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[0];

        SimplexSolver solver = new SimplexSolver();
        LinearOptimizationProblem problem = new LinearOptimizationProblem(objective, constraints, null, null, null);

        solver.optimize(problem);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{1.0}, {2.0}}, new double[]{3.0, 4.0});
        LinearConstraint[] constraints = new LinearConstraint[0];

        SimplexSolver solver = new SimplexSolver(1e-3, 1);
        LinearOptimizationProblem problem = new LinearOptimizationProblem(objective, constraints, null, null, null);

        solver.setMaxIterations(1);
        solver.optimize(problem);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[][]{{1.0}, {-1.0}}, new double[]{3.0, 0.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(1.0, new double[][]{{-1.0, 1.0}, {-2.0, 2.0}}, null, Relationship.LEQ, 1.0),
                new LinearConstraint(1.0, new double[][]{{1.0, 1.0}, {1.0, 2.0}}, null, Relationship.LEQ, 1.0)
        };

        SimplexSolver solver = new SimplexSolver();
        LinearOptimizationProblem problem = new LinearOptimizationProblem(objective, constraints, null, null, null);

        solver.optimize(problem);
    }
}
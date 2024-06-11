import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_SimpleLinearProblem() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{3.0, 2.0}, 3.0),
                new LinearConstraint(new double[]{1.0, 2.0}, 1.0)
        };

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();

        assertEquals(0.0, solution.getValue(), 1e-5);
        assertEquals(new double[]{3.0 / 2.0, -1.0 / 2.0}, solution.getPoint(), 1e-5);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_MaxIterationsReached() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[0];

        SimplexSolver solver = new SimplexSolver(1, 0);
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, 1.0}, Double.POSITIVE_INFINITY),
                new LinearConstraint(new double[]{3.0, 2.0}, 3.0),
                new LinearConstraint(new double[]{1.0, 2.0}, 1.0)
        };

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() throws Exception {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 2.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{3.0, 2.0}, Double.NEGATIVE_INFINITY),
                new LinearConstraint(new double[]{1.0, 2.0}, 1.0)
        };

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }
}
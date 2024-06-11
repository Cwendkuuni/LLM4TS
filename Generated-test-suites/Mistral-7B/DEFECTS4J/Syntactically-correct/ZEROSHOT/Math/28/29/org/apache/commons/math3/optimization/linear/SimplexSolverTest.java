import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimpleSolver() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[][]{{-1.0, 1.0}, {0.0, 1.0}}, Relationship.LEQ, 0.0);
        constraints[1] = new LinearConstraint(new double[][]{{-1.0, -1.0}, {0.0, 1.0}}, Relationship.LEQ, 0.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints);
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[][]{{-1.0, 1.0}, {0.0, 1.0}}, Relationship.LEQ, Double.NEGATIVE_INFINITY);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[][]{{-1.0, 1.0}, {0.0, 1.0}}, Relationship.LEQ, 0.0);
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(f, constraints);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[][]{{-1.0, 1.0}, {0.0, 1.0}}, Relationship.LEQ, 0.0);
        constraints[1] = new LinearConstraint(new double[][]{{-1.0, -1.0}, {0.0, 1.0}}, Relationship.LEQ, 0.0);
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints);
    }

}
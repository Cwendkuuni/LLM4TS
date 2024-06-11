import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimpleCase() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] g = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();

        PointValuePair result = solver.optimize(f, g);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0});
        LinearConstraint[] g = new LinearConstraint[]{new LinearConstraint(new double[]{1.0}, Double.NEGATIVE_INFINITY)};
        SimplexSolver solver = new SimplexSolver();

        solver.optimize(f, g);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] g = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();

        solver.optimize(f, g);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] g = new LinearConstraint[1000];
        for (int i = 0; i < 1000; i++) {
            g[i] = new LinearConstraint(new double[]{-1.0, 1.0}, i + 1.0);
        }
        SimplexSolver solver = new SimplexSolver();

        solver.optimize(f, g);
    }
}
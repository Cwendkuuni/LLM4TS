import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionGradient;
import org.apache.commons.math3.optimization.linear.SimplexConstraint;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.AbstractUnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testOptimize() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0}, {3.0}}, new double[]{5.0});
        LinearConstraint[] g = new LinearConstraint[]{{new SimplexConstraint(2.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(2.0, 2.0, 0.0, Double.POSITIVE_INFINITY)},
            {new SimplexConstraint(3.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(3.0, 2.0, 0.0, Double.POSITIVE_INFINITY)},
            {new SimplexConstraint(4.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(4.0, 2.0, 0.0, Double.POSITIVE_INFINITY)}};
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.optimize(f, g);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0, 1.5}, result.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizeWithGradient() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0}, {3.0}}, new double[]{5.0});
        LinearConstraint[] g = new LinearConstraint[]{{new SimplexConstraint(2.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(2.0, 2.0, 0.0, Double.POSITIVE_INFINITY)},
            {new SimplexConstraint(3.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(3.0, 2.0, 0.0, Double.POSITIVE_INFINITY)},
            {new SimplexConstraint(4.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(4.0, 2.0, 0.0, Double.POSITIVE_INFINITY)}};
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunctionGradient gf = new LinearObjectiveFunctionGradient(f);
        PointValuePair result = solver.optimize(f, g, gf);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0, 1.5}, result.getPoint(), 1e-6);
        assertArrayEquals(new double[]{1.0, 0.5}, gf.value(new double[]{1.0, 1.5}), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0}, {3.0}}, new double[]{5.0});
        LinearConstraint[] g = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver(1e-3, 10);
        solver.optimize(f, g);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0}, {3.0}}, new double[]{5.0});
        LinearConstraint[] g = new LinearConstraint[]{{new SimplexConstraint(2.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(2.0, 2.0, 0.0, Double.POSITIVE_INFINITY)},
            {new SimplexConstraint(3.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(3.0, 2.0, 0.0, Double.POSITIVE_INFINITY)}};
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, g);
        g[0].setUpperBound(Double.NEGATIVE_INFINITY);
        try {
            solver.optimize(f, g);
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0}, {3.0}}, new double[]{5.0});
        LinearConstraint[] g = new LinearConstraint[]{{new SimplexConstraint(2.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(2.0, 2.0, 0.0, Double.POSITIVE_INFINITY)},
            {new SimplexConstraint(3.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(3.0, 2.0, 0.0, Double.POSITIVE_INFINITY)},
            {new SimplexConstraint(4.0, 1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
            new SimplexConstraint(4.0, 2.0, 1.0, Double.POSITIVE_INFINITY)}};
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(f, g);
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }
}
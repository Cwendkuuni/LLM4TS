import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimplexSolverTest extends SimpleOptimizationTest {

    @Test
    public void testSimplexSolver() {
        // Test with a simple linear program
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, ConstraintQuality.INEQUALITY));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 0.0, null, ConstraintQuality.EQUALITY));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints);

        assertNotNull(solution);
        assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 1e-6);
        assertEquals(-1.0 / 3.0, solution.getValue(), 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 0.0, null, ConstraintQuality.INEQUALITY));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 0.0, null, ConstraintQuality.EQUALITY));

        SimplexSolver solver = new SimplexSolver(1e-12, 10);
        solver.setMaxIterations(100);
        solver.optimize(f, constraints);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, Double.NEGATIVE_INFINITY, null, ConstraintQuality.INEQUALITY));

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 1.0, null, ConstraintQuality.INEQUALITY));

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(f, constraints);
    }
}
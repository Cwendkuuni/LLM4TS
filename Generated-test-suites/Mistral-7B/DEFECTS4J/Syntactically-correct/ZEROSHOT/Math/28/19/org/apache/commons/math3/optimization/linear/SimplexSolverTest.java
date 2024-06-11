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

    private static LinearObjectiveFunction function;
    private static LinearConstraint[] constraints;

    static {
        // Initialize the linear optimization problem
        double[][] coefficients = new double[][] {{-1.0, 1.0, 1.0}, {-1.0, 1.0, -1.0}, {3.0, -1.0, 2.0}};
        double[] objectiveCoefficients = {1.0, 2.0, 3.0};
        function = new LinearObjectiveFunction(coefficients, objectiveCoefficients);

        LinearConstraint constraint1 = new LinearConstraint(new double[] {-1.0, 1.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[] {-1.0, 1.0}, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[] {3.0, -1.0}, 2.0);
        constraints = new LinearConstraint[] {constraint1, constraint2, constraint3};
    }

    @Test
    public void testSimplexSolverWithDefaultSettings() {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = null;
        try {
            solution = solver.optimize(function, constraints);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            Assert.fail("Solver should find a solution: " + e.getMessage());
        }

        Assert.assertNotNull(solution);
        Assert.assertEquals(3.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{1.0, 0.5, 1.5}, solution.getPoint(), 0.0);
    }

    @Test
    public void testSimplexSolverWithCustomSettings() {
        double epsilon = 1.0e-5;
        int maxUlps = 15;
        SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        PointValuePair solution = null;
        try {
            solution = solver.optimize(function, constraints);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            Assert.fail("Solver should find a solution: " + e.getMessage());
        }

        Assert.assertNotNull(solution);
        Assert.assertEquals(3.0, solution.getValue(), epsilon);
        Assert.assertArrayEquals(new double[]{1.0, 0.5, 1.5}, solution.getPoint(), 0.0);

        SimplexTableau tableau = solver.getLastTableau();
        Assert.assertEquals(epsilon, tableau.getEpsilon(), 0.0);
        Assert.assertEquals(maxUlps, tableau.getMaxUlps(), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() {
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(function, constraints);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(1);
        solver.optimize(function, constraints);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() {
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[] {1.0}, 0.0);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(new LinearObjectiveFunction(new double[][]{}, new double[0]), constraints);
        } catch (UnboundedSolutionException | MaxCountExceededException e) {
            Assert.fail("Solver should throw NoFeasibleSolutionException: " + e.getMessage());
        }
    }
}
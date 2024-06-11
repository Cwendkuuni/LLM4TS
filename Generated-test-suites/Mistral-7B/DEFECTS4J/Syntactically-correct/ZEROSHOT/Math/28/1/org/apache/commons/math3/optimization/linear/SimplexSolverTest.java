import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraintSet constraints;
    private LinearConstraintSet constraintsWithRedundant;
    private LinearConstraintSet unboundedConstraints;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{0.0, 1.0});
        constraints = new LinearConstraintSet();
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0);
        constraints.addConstraint(constraint1);
        constraints.addConstraint(constraint2);

        constraintsWithRedundant = new LinearConstraintSet();
        constraintsWithRedundant.addConstraint(constraint1);
        constraintsWithRedundant.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 0.0));

        unboundedConstraints = new LinearConstraintSet();
        LinearConstraint constraint3 = new LinearConstraint(new double[]{Double.POSITIVE_INFINITY, 0.0}, Double.NEGATIVE_INFINITY);
        unboundedConstraints.addConstraint(constraint3);
    }

    @Test
    public void testSimplexSolverWithSimpleConstraints() throws Exception {
        PointValuePair solution = solver.optimize(objectiveFunction, constraints);
        Assert.assertEquals(0.0, solution.getValue(), 0.0);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSimplexSolverWithUnboundedConstraints() throws Exception {
        PointValuePair solution = solver.optimize(objectiveFunction, unboundedConstraints);
        Assert.fail();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSimplexSolverWithMaxIterations() throws Exception {
        int maxIterations = 1;
        SimplexSolver solverWithMaxIterations = new SimplexSolver(epsilon, maxIterations);
        PointValuePair solution = solverWithMaxIterations.optimize(objectiveFunction, constraints);
        Assert.fail();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSimplexSolverWithInfeasibleConstraints() throws Exception {
        PointValuePair solution = solver.optimize(objectiveFunction, constraintsWithRedundant);
        Assert.fail();
    }
}
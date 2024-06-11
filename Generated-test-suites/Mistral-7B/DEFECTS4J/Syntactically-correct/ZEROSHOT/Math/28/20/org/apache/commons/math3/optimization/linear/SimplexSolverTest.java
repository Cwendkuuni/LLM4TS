import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.nonlinear.scalar.UnboundedSolutionException;
import org.apache.commons.math3.optim.nonlinear.scalar.maxcondition.MaxCountExceededException;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunctionSet;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-3.0, -5.0, 2.0}, new double[]{0.0, 0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[]{-1.0, -1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{1.0, 1.0, 1.0}, 2.0)
        };
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        SimplexSolver solver = new SimplexSolver();

        try {
            PointValuePair solution = solver.optimize(constraintSet, f);
            Assert.assertEquals(0.0, solution.getPoint()[0], 1e-8);
            Assert.assertEquals(2.0, solution.getPoint()[1], 1e-8);
            Assert.assertEquals(5.0, solution.getValue(), 1e-8);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            Assert.fail("Unanticipated exception: " + e.getMessage());
        }
    }

    @Test
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-3.0, -5.0, 2.0}, new double[]{0.0, 0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Double.NEGATIVE_INFINITY)
        };
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(constraintSet, f);
            Assert.fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-3.0, -5.0, 2.0}, new double[]{0.0, 0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[]{-1.0, -1.0, 1.0}, Double.POSITIVE_INFINITY),
                new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Double.POSITIVE_INFINITY)
        };
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(constraintSet, f);
            Assert.fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testMaxIterations() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionSet(new double[]{-3.0, -5.0, 2.0}, new double[]{0.0, 0.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[]{-1.0, -1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{1.0, 1.0, 1.0}, 2.0)
        };
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        SimplexSolver solver = new SimplexSolver(1, 1);

        try {
            solver.optimize(constraintSet, f);
            Assert.fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

}
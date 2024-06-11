import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testOptimize() throws Exception {
        // Test with a simple example: min x + y, subject to x + y <= 1, x >= 0, y >= 0
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1, -1}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1, 1}, Relationship.LEQUAL, 1));
        constraints.addConstraint(new LinearConstraint(new double[]{1, 0}, Relationship.GREATER_EQUAL, 0));
        constraints.addConstraint(new LinearConstraint(new double[]{0, 1}, Relationship.GREATER_EQUAL, 0));

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunction, constraints);

        // Check the solution
        double[] expectedSolution = new double[]{0.5, 0.5};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 0.001);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 0.001);
        assertEquals(-0.5, solution.getValue(), 0.001);

        // Test with an unbounded problem: min x, subject to x <= -1
        LinearObjectiveFunction unboundedObjFunction = new LinearObjectiveFunction(new double[]{-1}, new double[]{0});
        LinearConstraint unboundedConstraint = new LinearConstraint(new double[]{1}, Relationship.LEQUAL, 0);
        LinearConstraintSet unboundedConstraints = new LinearConstraintSet();
        unboundedConstraints.addConstraint(unboundedConstraint);

        try {
            solver.optimize(unboundedObjFunction, unboundedConstraints);
            fail("An UnboundedSolutionException should have been thrown.");
        } catch (UnboundedSolutionException e) {
            // Expected
        }

        // Test with a no feasible solution problem: min x, subject to x <= -1 and x >= 0
        LinearObjectiveFunction noFeasibleObjFunction = new LinearObjectiveFunction(new double[]{-1}, new double[]{0});
        LinearConstraint noFeasibleConstraint1 = new LinearConstraint(new double[]{1}, Relationship.LEQUAL, 0);
        LinearConstraint noFeasibleConstraint2 = new LinearConstraint(new double[]{1}, Relationship.GREATER_EQUAL, 0);
        LinearConstraintSet noFeasibleConstraints = new LinearConstraintSet();
        noFeasibleConstraints.addConstraint(noFeasibleConstraint1);
        noFeasibleConstraints.addConstraint(noFeasibleConstraint2);

        try {
            solver.optimize(noFeasibleObjFunction, noFeasibleConstraints);
            fail("A NoFeasibleSolutionException should have been thrown.");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }

        // Test with a max count exceeded problem: min x, subject to x <= 1000000 (1 million constraints)
        LinearObjectiveFunction maxCountObjFunction = new LinearObjectiveFunction(new double[]{-1}, new double[]{0});
        for (int i = 0; i < 1000000; i++) {
            LinearConstraint maxCountConstraint = new LinearConstraint(new double[]{1}, Relationship.LEQUAL, 1);
            LinearConstraintSet maxCountConstraints = new LinearConstraintSet();
            maxCountConstraints.addConstraint(maxCountConstraint);
            try {
                solver.optimize(maxCountObjFunction, maxCountConstraints);
                fail("A MaxCountExceededException should have been thrown.");
            } catch (MaxCountExceededException e) {
                // Expected
            }
        }
    }
}
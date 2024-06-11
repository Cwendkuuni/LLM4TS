import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.simplex.SimplexSolver;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptySolver() {
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSolverWithFunctionOnly() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 1.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = null;
        try {
            solution = solver.doOptimize(objFunc);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(0, solution.getPoint().length);
        assertEquals(-1.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolverWithFunctionAndConstraint() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 1.0});
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, new double[]{1.0}, Relationship.LEQ);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = null;
        try {
            solution = solver.doOptimize(objFunc, constraint);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(1, solution.getPoint().length);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(-1.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSolverWithMultipleConstraints() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 1.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, Relationship.LEQ);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, new double[]{1.0, 1.0}, Relationship.LEQ);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = null;
        try {
            solution = solver.doOptimize(objFunc, constraint1, constraint2);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(1, solution.getPoint().length);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(-1.0, solution.getValue(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{0.0}, new double[]{-1.0});
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(objFunc);
        } catch (MaxCountExceededException | NoFeasibleSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, Relationship.LEQ);
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.doOptimize(objFunc, constraint);
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 1.0});
        SimplexSolver solver = new SimplexSolver(1, 1);
        try {
            solver.doOptimize(objFunc);
        } catch (NoFeasibleSolutionException | UnboundedSolutionException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}
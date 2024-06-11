import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.simple.objective.AbstractRealObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.simple.objective.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.simple.objective.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.simple.objective.LinearConstraintSet;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{0.0});
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testOneConstraint() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{0.0});
        LinearConstraint c = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQUAL, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(c);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{0.5, 0.5}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testTwoConstraints() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -2.0, -3.0}, new double[]{0.0, 0.0});
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQUAL, 0.0);
        LinearConstraint c2 = new LinearConstraint(new double[]{1.0, -1.0, 1.0}, Relationship.LEQUAL, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(c1);
        constraints.addConstraint(c2);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, constraints);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertEquals(new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, -1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, new LinearConstraintSet(constraints));
        fail("Unexpected solution");
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[100000];
        for (int i = 0; i < constraints.length; i++) {
            constraints[i] = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQUAL, i);
        }
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(1);
        PointValuePair solution = solver.optimize(f, new LinearConstraintSet(constraints));
        fail("Maximum number of iterations exceeded");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, -1.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new LinearConstraint(new double[]{1.0, -1.0}, Relationship.LEQUAL, 2.0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, new LinearConstraintSet(constraints));
        fail("No feasible solution found");
    }

}
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction.MinimizationType;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.SimplexTableau.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws Exception {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 0.0);
        LinearObjectiveFunction objFunc2 = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, 3.0);
        LinearConstraint[] constraints = {new LinearConstraint(new double[]{1.0, 1.0}, 0.0),
                                           new LinearConstraint(new double[]{1.0, -1.0}, 0.0),
                                           new LinearConstraint(new double[]{-1.0, -1.0}, 0.0),
                                           new LinearConstraint(new double[]{-1.0, 1.0}, 0.0)};
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.optimize(objFunc, constraints, goalType);
        assertEquals(new double[]{0.0, 1.0}, solution.getPoint(), 0.0);
        assertEquals(-1.0, solution.getValue(), 0.0);

        try {
            solution = solver.optimize(objFunc2, constraints, goalType);
            fail("Exception expected");
        } catch (UnboundedSolutionException e) {
            // expected
        }

        LinearObjectiveFunction objFunc3 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0, MinimizationType.MAXIMIZE);
        solution = solver.optimize(objFunc3, constraints, goalType);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, solution.getValue(), 0.0);

        LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, 2.0);
        constraints = new LinearConstraint[] {constraint};
        LinearObjectiveFunction objFunc4 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);

        try {
            solution = solver.optimize(objFunc4, constraints, goalType);
            fail("NoFeasibleSolutionException expected");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }

        LinearObjectiveFunction objFunc5 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, -1.0}, Double.NEGATIVE_INFINITY);
        constraints = new LinearConstraint[]{constraint2};
        solution = solver.optimize(objFunc5, constraints, goalType);
        assertEquals(new double[]{1.0, 0.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc6 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                             new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                             new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                             new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        solution = solver.optimize(objFunc6, constraints, goalType);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc7 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints7 = new LinearConstraint[] {new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                                                   new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                                                   new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                                                   new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
                                                                   new LinearConstraint(new double[]{0.0, 1.0}, 0.0)};
        solution = solver.optimize(objFunc7, constraints7, goalType);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc8 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints8 = new LinearConstraint[] {new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                                                   new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                                                   new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                                                   new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        LinearObjectiveFunction objFunc9 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{1.0}, 1.0);
        constraints8 = new LinearConstraint[] {constraint3};
        solution = solver.optimize(objFunc8, constraints8, goalType);
        assertEquals(new double[]{1.0, 0.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);

        solution = solver.optimize(objFunc9, constraints8, goalType);
        assertEquals(new double[]{0.0, 1.0}, solution.getPoint(), 0.0);
        assertEquals(-1.0, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc10 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints10 = new LinearConstraint[] {new LinearConstraint(new double[]{2.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        solution = solver.optimize(objFunc10, constraints10, goalType);
        assertEquals(new double[]{0.5, 1.0}, solution.getPoint(), 0.0);
        assertEquals(-0.5, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc11 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints11 = new LinearConstraint[] {new LinearConstraint(new double[]{2.0, 1.0}, 2.0),
                                                                      new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        solution = solver.optimize(objFunc11, constraints11, goalType);
        assertEquals(new double[]{1.0, 0.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc12 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints12 = new LinearConstraint[] {new LinearConstraint(new double[]{2.0, 1.0}, 2.0),
                                                                      new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        LinearObjectiveFunction objFunc13 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint4 = new LinearConstraint(new double[]{1.0}, 2.0);
        constraints12 = new LinearConstraint[] {constraint4};
        solution = solver.optimize(objFunc12, constraints12, goalType);
        assertEquals(new double[]{0.5, 1.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);

        solution = solver.optimize(objFunc13, constraints12, goalType);
        assertEquals(new double[]{0.0, 1.0}, solution.getPoint(), 0.0);
        assertEquals(-1.0, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc14 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints14 = new LinearConstraint[] {new LinearConstraint(new double[]{2.0, 1.0}, 2.0),
                                                                      new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)};
        LinearObjectiveFunction objFunc15 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint5 = new LinearConstraint(new double[]{1.0}, 2.0);
        constraints14 = new LinearConstraint[] {constraint5};
        solution = solver.optimize(objFunc14, constraints14, goalType);
        assertEquals(new double[]{0.5, 1.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);

        solution = solver.optimize(objFunc15, constraints14, goalType);
        assertEquals(new double[]{1.0, 0.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc16 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints16 = new LinearConstraint[] {new LinearConstraint(new double[]{2.0, 1.0}, 2.0),
                                                                      new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{0.0, 1.0}, 1.0)};
        solution = solver.optimize(objFunc16, constraints16, goalType);
        assertEquals(new double[]{1.0/3.0, 1.0}, solution.getPoint(), 0.0);
        assertEquals(-1.0/3.0, solution.getValue(), 0.0);

        LinearObjectiveFunction objFunc17 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints17 = new LinearConstraint[] {new LinearConstraint(new double[]{2.0, 1.0}, 2.0),
                                                                      new LinearConstraint(new double[]{1.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, -1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
                                                                      new LinearConstraint(new double[]{0.0, 1.0}, 1.0)};
        LinearObjectiveFunction objFunc18 = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint constraint6 = new LinearConstraint(new double[]{1.0}, 3.0);
        constraints17 = new LinearConstraint[] {constraint6};
        solution = solver.optimize(objFunc17, constraints17, goalType);
        assertEquals(new double[]{2.0/3.0, 1.0}, solution.getPoint(), 0.0);
        assertEquals(1.0/3.0, solution.getValue(), 0.0);

        solution = solver.optimize(objFunc18, constraints17, goalType);
        assertEquals(new double[]{1.0/3.0, 1.0}, solution.getPoint(), 0.0);
        assertEquals(-1.0/3.0, solution.getValue(), 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 0.0);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(objFunc, constraints, GoalType.MINIMIZE);
    }
}
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

 private static final double EPSILON = 1e-6;

 @Test
 public void testEmptyConstraints() throws OptimizationException {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
 SimplexSolver solver = new SimplexSolver(EPSILON);
 PointValuePair solution = solver.optimize(objFunc);
 assertEquals(0.0, solution.getPoint()[0], EPSILON);
 }

 @Test
 public void testSimpleLinearEqualityConstraints() throws OptimizationException {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
 LinearConstraintSet constraints = new LinearConstraintSet();
 constraints.addConstraint(new LinearConstraint(2.0, new double[]{1.0, 1.0}, 0.0));
 SimplexSolver solver = new SimplexSolver(EPSILON);
 PointValuePair solution = solver.optimize(objFunc, constraints);
 assertEquals(new double[]{0.5, 1.0}, solution.getPoint(), EPSILON);
 }

 @Test
 public void testSimpleLinearInequalityConstraints() throws OptimizationException {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
 LinearConstraintSet constraints = new LinearConstraintSet();
 constraints.addConstraint(new LinearConstraint(2.0, new double[]{1.0, 1.0}, 3.0));
 SimplexSolver solver = new SimplexSolver(EPSILON);
 PointValuePair solution = solver.optimize(objFunc, constraints);
 assertEquals(new double[]{1.5, 1.0}, solution.getPoint(), EPSILON);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNonLinearConstraints() throws OptimizationException {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
 LinearConstraintSet constraints = new LinearConstraintSet();
 constraints.addConstraint(new org.apache.commons.math3.optim.nonlinear.NonlinearConstraint(x -> Math.pow(x, 2.0) - 4.0));
 SimplexSolver solver = new SimplexSolver(EPSILON);
 solver.optimize(objFunc, constraints);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeEpsilon() throws OptimizationException {
 SimplexSolver solver = new SimplexSolver(-EPSILON);
 }

 @Test
 public void testGoalType() throws OptimizationException {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
 SimplexSolver solver = new SimplexSolver(EPSILON);
 solver.setGoalType(GoalType.MINIMIZE);
 PointValuePair solution = solver.optimize(objFunc);
 assertEquals(new double[]{3.0 / 7.0, 4.0 / 7.0}, solution.getPoint(), EPSILON);
 }

 @Test
 public void testRestrictToNonNegative() throws OptimizationException {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
 SimplexSolver solver = new SimplexSolver(EPSILON);
 solver.setRestrictToNonNegative(true);
 PointValuePair solution = solver.optimize(objFunc);
 assertEquals(new double[]{Math.max(0.0, 3.0 / 7.0), Math.max(0.0, 4.0 / 7.0)}, solution.getPoint(), EPSILON);
 }
}
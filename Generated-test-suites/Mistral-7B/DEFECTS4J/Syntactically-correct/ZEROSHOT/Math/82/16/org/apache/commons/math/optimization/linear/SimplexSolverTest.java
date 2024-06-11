import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class SimplexSolverTest {

 private SimplexSolver solver;

 @Test
 public void testEmptyConstructor() {
 solver = new SimplexSolver();
 }

 @Test
 public void testCustomEpsilonConstructor() {
 solver = new SimplexSolver(0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetNumObjectiveFunctionsWithNullObjectiveFunction() {
 solver = new SimplexSolver();
 try {
 solver.getNumObjectiveFunctions(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetNumObjectiveFunctionsWithZeroObjectiveFunction() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[0]);
 solver = new SimplexSolver();
 int numObjs = solver.getNumObjectiveFunctions(objFunc);
 assertEquals(0, numObjs);
 }

 @Test
 public void testGetNumObjectiveFunctionsWithSingleObjectiveFunction() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 solver = new SimplexSolver();
 int numObjs = solver.getNumObjectiveFunctions(objFunc);
 assertEquals(1, numObjs);
 }

 @Test
 public void testGetNumObjectiveFunctionsWithMultipleObjectiveFunctions() {
 LinearObjectiveFunction objFunc1 = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 LinearObjectiveFunction objFunc2 = new LinearObjectiveFunction(new double[]{3.0, 4.0});
 solver = new SimplexSolver();
 int numObjs = solver.getNumObjectiveFunctions(new LinearObjectiveFunction[]{objFunc1, objFunc2});
 assertEquals(2, numObjs);
 }

 @Test
 public void testGetWidthWithNullConstraints() {
 solver = new SimplexSolver();
 try {
 solver.getWidth(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetWidthWithEmptyConstraints() {
 LinearConstraint[] constraints = {};
 solver = new SimplexSolver();
 int width = solver.getWidth(constraints);
 assertEquals(0, width);
 }

 @Test
 public void testGetWidthWithSingleConstraint() {
 LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, Relationship.LEQ, 2.0);
 solver = new SimplexSolver();
 int width = solver.getWidth(new LinearConstraint[]{constraint});
 assertEquals(2, width);
 }

 @Test
 public void testGetWidthWithMultipleConstraints() {
 LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 2.0);
 LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 3.0}, Relationship.LEQ, 4.0);
 solver = new SimplexSolver();
 int width = solver.getWidth(new LinearConstraint[]{constraint1, constraint2});
 assertEquals(3, width);
 }

 @Test
 public void testGetHeightWithNullObjectiveFunction() {
 solver = new SimplexSolver();
 try {
 solver.getHeight(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetHeightWithZeroObjectiveFunction() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[0]);
 solver = new SimplexSolver();
 int height = solver.getHeight(objFunc);
 assertEquals(0, height);
 }

 @Test
 public void testGetHeightWithSingleObjectiveFunction() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 solver = new SimplexSolver();
 int height = solver.getHeight(objFunc);
 assertEquals(2, height);
 }

 @Test
 public void testGetHeightWithMultipleObjectiveFunctions() {
 LinearObjectiveFunction objFunc1 = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 LinearObjectiveFunction objFunc2 = new LinearObjectiveFunction(new double[]{3.0, 4.0});
 solver = new SimplexSolver();
 int height = solver.getHeight(new LinearObjectiveFunction[]{objFunc1, objFunc2});
 assertEquals(4, height);
 }

 @Test
 public void testGetNumConstraintsWithNullConstraints() {
 solver = new SimplexSolver();
 try {
 solver.getNumConstraints(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetNumConstraintsWithEmptyConstraints() {
 LinearConstraint[] constraints = {};
 solver = new SimplexSolver();
 int numConstraints = solver.getNumConstraints(constraints);
 assertEquals(0, numConstraints);
 }

 @Test
 public void testGetNumConstraintsWithSingleConstraint() {
 LinearConstraint constraint = new LinearConstraint(new double[]{1.0}, Relationship.LEQ, 2.0);
 solver = new SimplexSolver();
 int numConstraints = solver.getNumConstraints(new LinearConstraint[]{constraint});
 assertEquals(1, numConstraints);
 }

 @Test
 public void testGetNumConstraintsWithMultipleConstraints() {
 LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 2.0);
 LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 3.0}, Relationship.LEQ, 4.0);
 solver = new SimplexSolver();
 int numConstraints = solver.getNumConstraints(new LinearConstraint[]{constraint1, constraint2});
 assertEquals(2, numConstraints);
 }

 @Test
 public void testDoOptimizeWithEmptyConstraints() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 LinearConstraint[] constraints = {};
 solver = new SimplexSolver();
 try {
 RealPointValuePair solution = solver.doOptimize(objFunc, constraints);
 } catch (OptimizationException e) {
 // expected
 }
 assertNull(solution);
 }

 @Test
 public void testDoOptimizeWithSingleConstraint() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 2.0);
 solver = new SimplexSolver();
 try {
 RealPointValuePair solution = solver.doOptimize(objFunc, constraint);
 } catch (OptimizationException e) {
 // expected
 }
 assertNull(solution);
 }

 @Test
 public void testDoOptimizeWithInvalidSolution() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 2.0);
 LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 3.0}, Relationship.LEQ, 4.0);
 solver = new SimplexSolver();
 try {
 RealPointValuePair solution = solver.doOptimize(objFunc, new LinearConstraint[]{constraint1, constraint2});
 } catch (NoFeasibleSolutionException e) {
 // expected
 }
 assertNull(solution);
 }

 @Test
 public void testDoOptimizeWithValidSolution() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 2.0);
 LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 3.0}, Relationship.LEQ, 4.0);
 solver = new SimplexSolver();
 try {
 RealPointValuePair solution = solver.doOptimize(objFunc, new LinearConstraint[]{constraint1, constraint2});
 assertEquals(new double[]{2.0, 1.0}, solution.getPoint(), 0.001);
 assertEquals(-3.0, solution.getValue(), 0.001);
 } catch (OptimizationException e) {
 // not reached
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimizeWithNullObjectiveFunction() {
 LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(new double[]{1.0}, Relationship.GEQ, 2.0)};
 solver = new SimplexSolver();
 try {
 solver.doOptimize(null, constraints);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoOptimizeWithNullConstraints() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 solver = new SimplexSolver();
 try {
 solver.doOptimize(objFunc, null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testDoOptimizeWithUnboundedSolution() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0});
 LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 2.0);
 LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 3.0}, Relationship.LEQ, 4.0);
 LinearConstraint constraint3 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, Double.POSITIVE_INFINITY);
 solver = new SimplexSolver();
 try {
 RealPointValuePair solution = solver.doOptimize(objFunc, new LinearConstraint[]{constraint1, constraint2, constraint3});
 } catch (NoFeasibleSolutionException e) {
 // expected
 }
 assertNull(solution);
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testDoOptimizeWithUnboundedSolutionInPhase1() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 1.0});
 LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 2.0);
 LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 3.0}, Relationship.LEQ, 4.0);
 solver = new SimplexSolver();
 try {
 solver.doOptimize(objFunc, new LinearConstraint[]{constraint1, constraint2});
 } catch (UnboundedSolutionException e) {
 // expected
 }
 assertNull(solver.getSolution());
 }

 @Test(expected = OptimizationException.class)
 public void testDoOptimizeWithMaxIterationsExceeded() {
 LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new double[]{1.0, 1.0});
 LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 2.0);
 LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 3.0}, Relationship.LEQ, 4.0);
 solver = new SimplexSolver();
 solver.setMaxIterations(0);
 try {
 solver.doOptimize(objFunc, new LinearConstraint[]{constraint1, constraint2});
 } catch (OptimizationException e) {
 // expected
 }
 assertNull(solver.getSolution());
 }
}
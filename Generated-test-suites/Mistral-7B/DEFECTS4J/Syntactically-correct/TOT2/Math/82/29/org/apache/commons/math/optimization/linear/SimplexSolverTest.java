import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleMaxEval;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

 private LinearObjectiveFunction f;
 private LinearConstraint[] constraints;
 private LinearOptimizer solver;

 @Test
 public void testEmptyConstructor() {
 SimplexSolver simplexSolver = new SimplexSolver();
 assertNotNull(simplexSolver);
 }

 @Test
 public void testConstructorWithEpsilon() {
 SimplexSolver simplexSolver = new SimplexSolver(0.001);
 assertNotNull(simplexSolver);
 }

 @Test
 public void testGetPivotColumn() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
 new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, 3.0)},
 LinearGoalType.MINIMIZE, true, 0.001);
 SimplexSolver simplexSolver = new SimplexSolver();
 int pivotColumn = simplexSolver.getPivotColumn(tableau);
 assertEquals(0, pivotColumn);
 }

 @Test
 public void testGetPivotRow() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
 new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, 3.0)},
 LinearGoalType.MINIMIZE, true, 0.001);
 SimplexSolver simplexSolver = new SimplexSolver();
 int pivotRow = simplexSolver.getPivotRow(0, tableau);
 assertEquals(1, pivotRow);
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testThrowUnboundedSolutionException() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
 new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, Double.NEGATIVE_INFINITY)},
 LinearGoalType.MINIMIZE, true, 0.001);
 SimplexSolver simplexSolver = new SimplexSolver();
 simplexSolver.getPivotRow(0, tableau);
 fail("Should have thrown UnboundedSolutionException");
 }

 @Test
 public void testDoIteration() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
 new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, 3.0)},
 LinearGoalType.MINIMIZE, true, 0.001);
 SimplexSolver simplexSolver = new SimplexSolver();
 simplexSolver.doIteration(tableau);
 assertFalse(tableau.isOptimal());
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testThrowNoFeasibleSolutionException() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 0.0}),
 new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, 3.0)},
 LinearGoalType.MINIMIZE, true, 0.001);
 SimplexSolver simplexSolver = new SimplexSolver();
 simplexSolver.solvePhase1(tableau);
 fail("Should have thrown NoFeasibleSolutionException");
 }

 @Test
 public void testIsPhase1Solved() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
 new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, 3.0)},
 LinearGoalType.MINIMIZE, true, 0.001);
 SimplexSolver simplexSolver = new SimplexSolver();
 assertFalse(simplexSolver.isPhase1Solved(tableau));
 }

 @Test
 public void testIsOptimal() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
 new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, 3.0)},
 LinearGoalType.MINIMIZE, true, 0.001);
 SimplexSolver simplexSolver = new SimplexSolver();
 assertFalse(simplexSolver.isOptimal(tableau));
 }

 @Test
 public void testSolvePhase1() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
 new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, 3.0)},
 LinearGoalType.MINIMIZE, true, 0.001);
 SimplexSolver simplexSolver = new SimplexSolver();
 simplexSolver.solvePhase1(tableau);
 assertTrue(simplexSolver.isPhase1Solved(tableau));
 }

 @Test
 public void testDoOptimize() throws OptimizationException {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
 LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5, 1.0}, 2.5), new LinearConstraint(2, new double[]{1.0, 2.0}, 3.0)};
 SimplexSolver simplexSolver = new SimplexSolver();
 PointValuePair solution = simplexSolver.doOptimize();
 assertEquals(new double[]{1.5, 1.0}, solution.getPoint(), 0.0);
 assertEquals(-5.5, solution.getValue(), 0.0);
 }
}
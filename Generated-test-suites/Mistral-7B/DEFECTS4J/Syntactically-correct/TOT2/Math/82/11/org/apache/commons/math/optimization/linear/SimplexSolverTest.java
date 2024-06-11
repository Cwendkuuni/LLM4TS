import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.SimpleGOptimizer;
import org.apache.commons.math3.optim.nonlinear.simple.GoalType;
import org.junit.Before;
import org.junit.Test;
import org.junit. TigersFor;
import static org.junit.Assert.*;

@TigersFor(SimplexSolver.class)
public class SimplexSolverTest {

 private SimplexSolver solver;
 private LinearObjectiveFunction objFunc;
 private LinearConstraint[] constraints;

 @Before
 public void setUp() {
 solver = new SimplexSolver();
 objFunc = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
 constraints = new LinearConstraint[2];
 constraints[0] = new LinearConstraint(new double[]{-1.0, -1.0}, 0.0);
 constraints[1] = new LinearConstraint(new double[]{1.0, 2.0}, 3.0);
 }

 @Test
 public void testDefaultConstructor() {
 solver = new SimplexSolver();
 assertEquals(DEFAULT_EPSILON, solver.epsilon, 0.0);
 }

 @Test
 public void testCustomConstructor() {
 solver = new SimplexSolver(0.001);
 assertEquals(0.001, solver.epsilon, 0.0);
 }

 @Test
 public void testGetPivotColumn() {
 SimplexTableau tableau = new SimplexTableau(
 new double[][]{{1.0, 2.0, 0.0, Integer.MIN_VALUE},
 {0.0, 1.0, 3.0, 1.0},
 {0.0, 1.0, -1.0, 2.0}},
 new LinearObjectiveFunction[]{objFunc},
 GoalType.MINIMIZE,
 null,
 DEFAULT_EPSILON);
 Integer pivotCol = solver.getPivotColumn(tableau);
 assertEquals(1, pivotCol.intValue());
 }

 @Test
 public void testGetPivotRow() {
 SimplexTableau tableau = new SimplexTableau(
 new double[][]{{1.0, 2.0, 0.0, Integer.MIN_VALUE},
 {0.0, 1.0, 3.0, 1.0},
 {0.0, 1.0, -1.0, 2.0}},
 new LinearObjectiveFunction[]{objFunc},
 GoalType.MINIMIZE,
 null,
 DEFAULT_EPSILON);
 Integer pivotRow = solver.getPivotRow(0, tableau);
 assertEquals(1, pivotRow.intValue());
 }

 @Test
 public void testDoIteration() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(
 new double[][]{{1.0, 2.0, 0.0, Integer.MIN_VALUE},
 {0.0, 1.0, 3.0, 1.0},
 {0.0, 1.0, -1.0, 2.0}},
 new LinearObjectiveFunction[]{objFunc},
 GoalType.MINIMIZE,
 null,
 DEFAULT_EPSILON);
 solver.doIteration(tableau);
 double pivotVal = tableau.getEntry(1, 0);
 assertEquals(1.0 / pivotVal, tableau.getEntry(0, 0), 0.01);
 assertEquals(0.0, tableau.getEntry(1, 1), 0.01);
 }

 @Test
 public void testIsPhase1Solved() {
 SimplexTableau tableau = new SimplexTableau(
 new double[][]{{1.0, 2.0, 0.0, Integer.MIN_VALUE},
 {0.0, 1.0, 3.0, 1.0},
 {0.0, 1.0, -1.0, 2.0}},
 new LinearObjectiveFunction[]{objFunc},
 GoalType.MINIMIZE,
 null,
 DEFAULT_EPSILON);
 assertFalse(solver.isPhase1Solved(tableau));
 tableau.setEntry(0, 1, 1.0);
 tableau.setEntry(1, 2, -1.0);
 assertTrue(solver.isPhase1Solved(tableau));
 }

 @Test
 public void testIsOptimal() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(
 new double[][]{{1.0, 2.0, 0.0, Integer.MIN_VALUE},
 {0.0, 1.0, 3.0, 1.0},
 {0.0, 1.0, -1.0, 2.0}},
 new LinearObjectiveFunction[]{objFunc},
 GoalType.MINIMIZE,
 null,
 DEFAULT_EPSILON);
 solver.solvePhase1(tableau);
 tableau.discardArtificialVariables();
 assertFalse(solver.isOptimal(tableau));
 solver.doIteration(tableau);
 assertTrue(solver.isOptimal(tableau));
 }

 @Test
 public void testSolvePhase1() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(
 new double[][]{{1.0, 2.0, 0.0, Integer.MIN_VALUE},
 {0.0, 1.0, 3.0, 1.0},
 {0.0, 1.0, -1.0, 2.0}},
 new LinearObjectiveFunction[]{objFunc},
 GoalType.MINIMIZE,
 null,
 DEFAULT_EPSILON);
 solver.solvePhase1(tableau);
 assertEquals(2, tableau.getNumArtificialVariables());
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testUnboundedSolutionException() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(
 new double[][]{{1.0, 2.0, 0.0, Integer.MIN_VALUE},
 {0.0, 1.0, 3.0, Integer.MAX_VALUE},
 {0.0, 1.0, -1.0, 2.0}},
 new LinearObjectiveFunction[]{objFunc},
 GoalType.MINIMIZE,
 null,
 DEFAULT_EPSILON);
 solver.solvePhase1(tableau);
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testNoFeasibleSolutionException() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(
 new double[][]{{1.0, 2.0, 0.0, Integer.MIN_VALUE},
 {0.0, 1.0, 3.0, 1.0},
 {0.0, 1.0, -1.0, 2.0}},
 new LinearObjectiveFunction[]{objFunc},
 GoalType.MINIMIZE,
 null,
 DEFAULT_EPSILON);
 solver.solvePhase1(tableau);
 tableau.setEntry(0, 2, -1.0);
 solver.solvePhase1(tableau);
 }

 @Test
 public void testOptimize() throws OptimizationException {
 PointValuePair solution = solver.doOptimize();
 assertEquals(2.0, solution.getPoint()[0], 0.01);
 assertEquals(3.0, solution.getValue(), 0.01);
 }

}
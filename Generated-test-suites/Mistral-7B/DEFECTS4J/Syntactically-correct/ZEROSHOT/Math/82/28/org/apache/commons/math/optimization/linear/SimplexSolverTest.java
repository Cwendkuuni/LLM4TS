import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SimplexSolverTest {

 private SimplexSolver simplexSolver;

 @Test
 public void testSimplexSolverConstruction() {
 simplexSolver = new SimplexSolver();
 }

 @Test
 public void testSimplexSolverWithCustomEpsilon() {
 simplexSolver = new SimplexSolver(1.0e-10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimplexSolverNegativeEpsilon() {
 simplexSolver = new SimplexSolver(-1.0);
 }

 @Test
 public void testGetPivotColumn() {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);

 // When
 int pivotColumn = simplexSolver.getPivotColumn(tableau);

 // Then
 assertEquals(1, pivotColumn.intValue());
 }

 @Test
 public void testGetPivotColumnWhenAllNegative() {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);

 // When
 int pivotColumn = simplexSolver.getPivotColumn(tableau);

 // Then
 assertEquals(0, pivotColumn.intValue());
 }

 @Test
 public void testGetPivotRow() {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);

 // When
 int pivotRow = simplexSolver.getPivotRow(1, tableau);

 // Then
 assertNull(pivotRow);
 }

 @Test
 public void testGetPivotRowWithMinimumRatio() {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}, new double[]{0.0, 5.0, 0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);

 // When
 int pivotRow = simplexSolver.getPivotRow(1, tableau);

 // Then
 assertEquals(1, pivotRow.intValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetPivotRowWithNoNegativeEntry() {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);

 // When, Then
 assertThrows(IllegalStateException.class, () -> simplexSolver.getPivotRow(2, tableau));
 }

 @Test
 public void testDoIteration() throws OptimizationException {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);

 // When
 simplexSolver.doIteration(tableau);

 // Then
 assertEquals(1.0 / (-1.0), tableau.getEntry(1, 0), Precision.SAFE_EQUALITY_EPSILON);
 assertEquals(0.0, tableau.getEntry(1, 1), Precision.SAFE_EQUALITY_EPSILON);
 for (int i = 0; i < tableau.getHeight(); i++) {
 if (i != 1) {
 assertEquals(0.0, tableau.getEntry(i, 0), Precision.SAFE_EQUALITY_EPSILON);
 assertEquals(0.0, tableau.getEntry(i, 1), Precision.SAFE_EQUALITY_EPSILON);
 }
 }
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testDoIterationWithUnboundedSolution() throws OptimizationException {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0, 2.0}, new double[]{0.0, 0.0, 0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);

 // When, Then
 assertThrows(UnboundedSolutionException.class, () -> simplexSolver.doIteration(tableau));
 }

 @Test
 public void testIsPhase1Solved() throws OptimizationException {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);
 tableau.setNumArtificialVariables(2);

 // When
 boolean isPhase1Solved = simplexSolver.isPhase1Solved(tableau);

 // Then
 assertFalse(isPhase1Solved);
 }

 @Test
 public void testIsPhase1SolvedWhenPhase1IsSolved() throws OptimizationException {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);
 tableau.setNumArtificialVariables(0);

 // When
 boolean isPhase1Solved = simplexSolver.isPhase1Solved(tableau);

 // Then
 assertTrue(isPhase1Solved);
 }

 @Test
 public void testSolvePhase1() throws OptimizationException {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);
 tableau.setNumArtificialVariables(2);

 // When
 simplexSolver.solvePhase1(tableau);

 // Then
 assertEquals(0.0, tableau.getEntry(0, 1), Precision.SAFE_EQUALITY_EPSILON);
 assertEquals(0.0, tableau.getEntry(1, 1), Precision.SAFE_EQUALITY_EPSILON);
 for (int i = 2; i < tableau.getHeight(); i++) {
 assertEquals(0.0, tableau.getEntry(i, 1), Precision.SAFE_EQUALITY_EPSILON);
 }
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);
 tableau.setNumArtificialVariables(2);
 tableau.setEntry(0, tableau.getWidth() - 1, -1.0);

 // When, Then
 assertThrows(NoFeasibleSolutionException.class, () -> simplexSolver.solvePhase1(tableau));
 }

 @Test
 public void testIsOptimal() throws OptimizationException {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);
 tableau.setNumArtificialVariables(0);

 // When
 boolean isOptimal = simplexSolver.isOptimal(tableau);

 // Then
 assertTrue(isOptimal);
 }

 @Test
 public void testIsOptimalWhenNotOptimal() throws OptimizationException {
 // Given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
 new LinearConstraint[]{}, GoalType.MINIMIZE, true, 1.0e-6);
 tableau.setNumArtificialVariables(0);
 tableau.setEntry(0, tableau.getWidth() - 1, 1.0);

 // When
 boolean isOptimal = simplexSolver.isOptimal(tableau);

 // Then
 assertFalse(isOptimal);
 }

 @Test
 public void testOptimize() throws OptimizationException {
 // Given
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
 LinearConstraint[] constraints = new LinearConstraint[0];
 GoalType goalType = GoalType.MINIMIZE;
 boolean restrictToNonNegative = true;
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, 1.0e-6);

 // When
 RealPointValuePair result = simplexSolver.doOptimize();

 // Then
 assertEquals(-1.0, result.getPoint()[0], Precision.SAFE_EQUALITY_EPSILON);
 assertEquals(-1.0, result.getValue(), Precision.SAFE_EQUALITY_EPSILON);
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testOptimizeWithUnboundedSolution() throws OptimizationException {
 // Given
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
 LinearConstraint[] constraints = new LinearConstraint[0];
 GoalType goalType = GoalType.MINIMIZE;
 boolean restrictToNonNegative = true;
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, 1.0e-6);
 tableau.setEntry(0, tableau.getWidth() - 1, Double.POSITIVE_INFINITY);

 // When, Then
 assertThrows(UnboundedSolutionException.class, () -> simplexSolver.doOptimize());
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testOptimizeWithNoFeasibleSolution() throws OptimizationException {
 // Given
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
 LinearConstraint[] constraints = new LinearConstraint[0];
 GoalType goalType = GoalType.MINIMIZE;
 boolean restrictToNonNegative = true;
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, 1.0e-6);
 tableau.setEntry(0, tableau.getWidth() - 1, Double.NEGATIVE_INFINITY);

 // When, Then
 assertThrows(NoFeasibleSolutionException.class, () -> simplexSolver.doOptimize());
 }

 @Test
 public void testOptimizeWithFeasibleSolutionButNotOptimal() throws OptimizationException {
 // Given
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
 LinearConstraint[] constraints = new LinearConstraint[0];
 GoalType goalType = GoalType.MINIMIZE;
 boolean restrictToNonNegative = true;
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, 1.0e-6);
 tableau.setEntry(0, tableau.getWidth() - 1, 1.0);
 tableau.setEntry(1, tableau.getWidth() - 1, -1.0);

 // When
 RealPointValuePair result = simplexSolver.doOptimize();

 // Then
 assertNotNull(result);
 assertEquals(-1.0, result.getPoint()[0], Precision.SAFE_EQUALITY_EPSILON);
 assertNotEquals(-1.0, result.getValue(), Precision.SAFE_EQUALITY_EPSILON);
 }

 @Test
 public void testOptimizeWithOptimalSolution() throws OptimizationException {
 // Given
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
 LinearConstraint[] constraints = new LinearConstraint[0];
 GoalType goalType = GoalType.MINIMIZE;
 boolean restrictToNonNegative = true;
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, 1.0e-6);

 // When
 RealPointValuePair result = simplexSolver.doOptimize();

 // Then
 assertNotNull(result);
 assertEquals(-1.0, result.getPoint()[0], Precision.SAFE_EQUALITY_EPSILON);
 assertEquals(-1.0, result.getValue(), Precision.SAFE_EQUALITY_EPSILON);
 }

}
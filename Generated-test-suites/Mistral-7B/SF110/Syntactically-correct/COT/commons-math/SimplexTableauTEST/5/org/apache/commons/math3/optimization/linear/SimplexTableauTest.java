import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 private SimplexTableau tableau;

 @Test
 public void testSimplexTableauConstructor() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimplexTableauConstructorWithNullObjectiveFunction() {
 LinearObjectiveFunction objectiveFunction = null;
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimplexTableauConstructorWithNullConstraints() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = null;
 GoalType goalType = GoalType.MAXIMIZe;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 }

 @Test
 public void testInitializeColumnLabels() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 List<String> expectedColumnLabels = Arrays.asList("W", "Z", "x0", "x1", "x2", "x-");
 List<String> actualColumnLabels = tableau.columnLabels;
 assertEquals(expectedColumnLabels, actualColumnLabels);
 }

 @Test
 public void testCreateTableau() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][] {
 {-1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 2.0},
 {1.0, -1.0, 1.0, 1.0, 1.0, -1.0, 0.0},
 {1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 2.0}
 });
 RealMatrix actualTableau = tableau.tableau;
 assertArrayEquals(expectedTableau.getData(), actualTableau.getData(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateTableauWithInvalidNumberOfConstraints() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 List<LinearConstraint> additionalConstraint = new ArrayList<LinearConstraint>();
 additionalConstraint.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 tableau.constraints.addAll(additionalConstraint);
 }

 @Test
 public void testDropPhase1Objective() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 tableau.dropPhase1Objective();
 RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][] {
 {1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 2.0},
 {1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 2.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}
 });
 RealMatrix actualTableau = tableau.tableau;
 assertArrayEquals(expectedTableau.getData(), actualTableau.getData(), 0.0);
 }

 @Test
 public void testIsOptimal() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 tableau.dropPhase1Objective();
 assertTrue(tableau.isOptimal());
 }

 @Test
 public void testIsOptimalWithNonOptimalTableau() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 tableau.setEntry(1, 1, 1.1);
 assertFalse(tableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 tableau.dropPhase1Objective();
 PointValuePair expectedSolution = new PointValuePair(new double[] {-3.0 / 3.0, 2.0 / 3.0}, -1.0);
 PointValuePair actualSolution = tableau.getSolution();
 assertEquals(expectedSolution.getPoint(), actualSolution.getPoint(), 0.0);
 assertEquals(expectedSolution.getValue(), actualSolution.getValue(), 0.0);
 }

 @Test
 public void testGetSolutionWithNonOptimalTableau() {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 0.0);
 List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
 constraints.add(new LinearConstraint(new RealVector(new double[] {1.0, 1.0, -1.0}), Relationship.LEQ, 2.0));
 GoalType goalType = GoalType.MAXIMIZE;
 boolean restrictToNonNegative = false;
 double epsilon = 1.0E-6;
 int maxUlps = 10;
 tableau = new SimplexTableau(objectiveFunction, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 tableau.setEntry(1, 1, 1.1);
 try {
 PointValuePair solution = tableau.getSolution();
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }
 }

}
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 private SimplexTableau createTableau(LinearObjectiveFunction f, Collection<LinearConstraint> constraints, GoalType goalType, boolean restrictToNonNegative, double epsilon, int maxUlps) {
 return new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
 }

 @Test
 public void testConstruction() {
 LinearObjectiveFunction f = new LinearQuadraticObjectiveFunction(new RealVector(new double[]{0.0, 2.0, 3.0}), new RealVector(new double[]{1.0, 2.0, 3.0}));
 Collection<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 1.0}), Relationship.LEQ, 4.0));
 LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.GEQ, 1.0);
 constraints.add(constraint2);
 SimplexTableau tableau = createTableau(f, constraints, GoalType.MINIMIZE, true, 1e-6, 10);

 assertTrue(tableau.getNumObjectiveFunctions() == 1);
 assertTrue(tableau.getNumDecisionVariables() == 3);
 assertTrue(tableau.getNumSlackVariables() == 2);
 assertTrue(tableau.getNumArtificialVariables() == 0);
 assertTrue(tableau.epsilon == 1e-6);
 assertTrue(tableau.maxUlps == 10);

 // Test column labels
 assertEquals(4, tableau.columnLabels.size());
 assertEquals("W", tableau.columnLabels.get(0));
 assertEquals("Z", tableau.columnLabels.get(1));
 assertEquals("x0", tableau.columnLabels.get(2));
 assertEquals("x1", tableau.columnLabels.get(3));
 }

 @Test
 public void testCreateTableau() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealPolynomialFunction(new RealVariable[]{"x1", "x2"}, new double[]{-3.0, -2.0}, new double[]{-2.0, -1.0}));
 Collection<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0));
 constraints.add(new LinearConstraint(new RealVector(new double[]{2.0, -1.0}), Relationship.GEQ, 2.0));
 SimplexTableau tableau = createTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 RealMatrix tableauMatrix = tableau.getData();

 // Check W column
 assertEquals(tableauMatrix.getEntry(0, 0), -1.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(1, 0), 1.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(2, 0), 1.0, 1e-6);

 // Check Z column
 assertEquals(tableauMatrix.getEntry(0, 1), 0.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(1, 1), 2.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(2, 1), 3.0, 1e-6);

 // Check x0, x1 columns
 assertEquals(tableauMatrix.getEntry(0, 2), -3.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(0, 3), -2.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(1, 2), -2.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(1, 3), -1.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(2, 2), 0.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(2, 3), 0.0, 1e-6);

 // Check s0, s1 columns
 assertEquals(tableauMatrix.getEntry(1, 4), 1.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(2, 4), 1.0, 1e-6);

 // Check RHS
 assertEquals(tableauMatrix.getEntry(1, 5), 3.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(2, 5), 2.0, 1e-6);
 }

 @Test
 public void testNormalizeConstraints() {
 LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.LEQ, 3.0);
 LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
 Collection<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(constraint1);
 constraints.add(constraint2);
 SimplexTableau tableau = createTableau(null, constraints, GoalType.MINIMIZE, true, 1e-6, 10);
 List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);

 assertEquals(normalizedConstraints.size(), 2);

 LinearConstraint normalizedConstraint1 = normalizedConstraints.get(0);
 assertEquals(normalizedConstraint1.getCoefficients().getDimension(), 3);
 assertEquals(normalizedConstraint1.getValue(), 3.0, 1e-6);
 assertEquals(normalizedConstraint1.getRelationship(), Relationship.GEQ);

 LinearConstraint normalizedConstraint2 = normalizedConstraints.get(1);
 assertEquals(normalizedConstraint2.getCoefficients().getDimension(), 3);
 assertEquals(normalizedConstraint2.getValue(), 2.0, 1e-6);
 assertEquals(normalizedConstraint2.getRelationship(), Relationship.LEQ);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeValueConstraint() {
 LinearConstraint constraint = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.LEQ, -3.0);
 Collection<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(constraint);
 SimplexTableau tableau = createTableau(null, constraints, GoalType.MINIMIZE, true, 1e-6, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMismatchedConstraintSizes() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{0.0, 2.0, 3.0}));
 LinearConstraint constraint = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 1.0}), Relationship.LEQ, 3.0);
 Collection<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(constraint);
 SimplexTableau tableau = createTableau(f, new ArrayList<>(), GoalType.MINIMIZE, true, 1e-6, 10);
 }

 @Test
 public void testDropPhase1Objective() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealPolynomialFunction(new RealVariable[]{"x1", "x2"}, new double[]{-3.0, -2.0}, new double[]{-2.0, -1.0})), new ArrayList<>(), GoalType.MINIMIZE, false, 1e-6, 10);
 tableau.dropPhase1Objective();
 RealMatrix tableauMatrix = tableau.getData();

 assertEquals(tableauMatrix.getEntry(0, 0), 0.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(0, 1), 2.0, 1e-6);
 assertEquals(tableauMatrix.getEntry(0, 2), 3.0, 1e-6);
 }

 @Test
 public void testIsOptimal() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealPolynomialFunction(new RealVariable[]{"x1", "x2"}, new double[]{-3.0, -2.0}, new double[]{-2.0, -1.0})), new ArrayList<>(), GoalType.MINIMIZE, false, 1e-6, 10);
 tableau.setEntry(0, 0, 0.0);
 tableau.setEntry(0, 1, 1.5);
 tableau.setEntry(0, 2, 2.5);

 assertFalse(tableau.isOptimal());
 }

 @Test
 public void testIsOptimalOptimized() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealPolynomialFunction(new RealVariable[]{"x1", "x2"}, new double[]{-3.0, -2.0}, new double[]{-2.0, -1.0})), new ArrayList<>(), GoalType.MINIMIZE, false, 1e-6, 10);
 tableau.setEntry(0, 0, -5.0);
 tableau.setEntry(0, 1, -3.0);
 tableau.setEntry(0, 2, -3.5);

 assertTrue(tableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealPolynomialFunction(new RealVariable[]{"x1", "x2"}, new double[]{-3.0, -2.0}, new double[]{-2.0, -1.0})), new ArrayList<>(), GoalType.MINIMIZE, false, 1e-6, 10);
 tableau.dropPhase1Objective();
 PointValuePair solution = tableau.getSolution();
 double[] x = solution.getPoint();

 assertEquals(x.length, 3);
 assertEquals(x[0], 2.5, 1e-6);
 assertEquals(x[1], 1.5, 1e-6);
 assertEquals(x[2], 0.0, 1e-6);
 }
}
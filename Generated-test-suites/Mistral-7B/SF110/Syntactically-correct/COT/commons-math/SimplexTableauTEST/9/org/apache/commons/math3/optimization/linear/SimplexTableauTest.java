package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class SimplexTableauTest {

 private static final double EPSILON = 1.0E-12;
 private static final int DEFAULT_ULPS = 10;

 @Test
 public void testSimplexTableauConstruction() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 1.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[] {1.0, 2.0}), Relationship.LEQ, 3.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[] {-1.0, 1.0}), Relationship.GEQ, 1.0);
 LinearConstraint constraint3 = new LinearConstraint(new ArrayRealVector(new double[] {-1.0, 1.0}), Relationship.EQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 constraints.add(constraint3);
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, EPSILON);

 // Check constructor properties
 assertEquals(1.0, tableau.getNumObjectiveFunctions(), 0.0);
 assertEquals(3, tableau.getOriginalNumDecisionVariables());
 assertEquals(3, tableau.getNumDecisionVariables());
 assertEquals(0, tableau.getNumSlackVariables());
 assertEquals(0, tableau.getNumArtificialVariables());
 assertEquals(EPSILON, tableau.epsilon, 0.0);
 assertEquals(DEFAULT_ULPS, tableau.maxUlps);
 assertEquals(f, tableau.f);

 // Check column labels
 List<String> columnLabels = tableau.columnLabels;
 assertEquals(10, columnLabels.size());
 assertEquals("W", columnLabels.get(0));
 assertEquals("Z", columnLabels.get(1));
 for (int i = 0; i < 3; ++i) {
 assertEquals("x" + i, columnLabels.get(i + 2));
 }
 if (!tableau.restrictToNonNegative) {
 assertEquals("x-", columnLabels.get(columnLabels.size() - 2));
 }
 for (int i = 0; i < 3; ++i) {
 assertEquals("s" + i, columnLabels.get(columnLabels.size() - 3 + i));
 }
 for (int i = 0; i < 3; ++i) {
 assertEquals("a" + i, columnLabels.get(columnLabels.size() - 2 + i));
 }
 assertEquals("RHS", columnLabels.get(columnLabels.size() - 1));
 }

 @Test
 public void testCreateTableau() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 1.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[] {1.0, 2.0}), Relationship.LEQ, 3.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[] {-1.0, 1.0}), Relationship.GEQ, 1.0);
 LinearConstraint constraint3 = new LinearConstraint(new ArrayRealVector(new double[] {-1.0, 1.0}), Relationship.EQ, 0.0);
 constraints.add(constraint1);
 constraints.add(constraint2);
 constraints.add(constraint3);
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, EPSILON);

 // Test tableau creation
 RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][] {
 {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 3.0, 3.0, 3.0, 1.0, -1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0},
 {1.0, -1.0, -1.0, -1.0, 1.0, 1.0, -3.0, -3.0, -3.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0}
 });
 RealMatrix tableau = tableau.getData();
 assertArrayEquals(expectedTableau.getData(), tableau.getData(), 0.0);
 }

 @Test
 public void testNormalizeConstraints() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 1.0);
 List<LinearConstraint> originalConstraints = new ArrayList<>();
 LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[] {1.0, 2.0}), Relationship.LEQ, 3.0);
 LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[] {-1.0, 1.0}), Relationship.GEQ, 1.0);
 LinearConstraint constraint3 = new LinearConstraint(new ArrayRealVector(new double[] {-1.0, 1.0}), Relationship.EQ, 0.0);
 originalConstraints.add(constraint1);
 originalConstraints.add(constraint2);
 originalConstraints.add(constraint3);
 SimplexTableau tableau = new SimplexTableau(f, originalConstraints, GoalType.MAXIMIZE, false, EPSILON);

 List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);
 assertEquals(originalConstraints.size(), normalizedConstraints.size());
 for (int i = 0; i < normalizedConstraints.size(); ++i) {
 LinearConstraint originalConstraint = originalConstraints.get(i);
 LinearConstraint normalizedConstraint = normalizedConstraints.get(i);
 assertEquals(originalConstraint.getCoefficients(), normalizedConstraint.getCoefficients());
 assertEquals(originalConstraint.getRelationship(), normalizedConstraint.getRelationship());
 assertEquals(originalConstraint.getValue(), normalizedConstraint.getValue(), 0.0);
 }
 }

 @Test
 public void testGetInvertedCoefficientSum() {
 RealVector coefficients = new ArrayRealVector(new double[] {1.0, 2.0, 3.0});
 double result = SimplexTableau.getInvertedCoefficientSum(coefficients);
 assertEquals(-6.0, result, 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvertedCoefficientSumNullVector() {
 SimplexTableau.getInvertedCoefficientSum(null);
 }

 @Test
 public void testGetBasicRow() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 1.0), new ArrayList<>(), GoalType.MAXIMIZE, false, EPSILON);

 // Test getting basic row for decision variable x0
 int colIndexX0 = tableau.columnLabels.indexOf("x0");
 Integer rowX0 = tableau.getBasicRow(colIndexX0);
 assertEquals(0, rowX0.intValue());

 // Test getting basic row for decision variable x1
 int colIndexX1 = tableau.columnLabels.indexOf("x1");
 Integer rowX1 = tableau.getBasicRow(colIndexX1);
 assertNull(rowX1);

 // Test getting basic row for slack variable s0
 int colIndexS0 = tableau.columnLabels.indexOf("s0");
 Integer rowS0 = tableau.getBasicRow(colIndexS0);
 assertNull(rowS0);

 // Test getting basic row for artificial variable a0
 int colIndexA0 = tableau.columnLabels.indexOf("a0");
 Integer rowA0 = tableau.getBasicRow(colIndexA0);
 assertNull(rowA0);
 }

 @Test
 public void testIsOptimal() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 1.0), new ArrayList<>(), GoalType.MAXIMIZE, false, EPSILON);

 // Test optimal tableau
 tableau.dropPhase1Objective();
 assertTrue(tableau.isOptimal());

 // Test non-optimal tableau
 LinearObjectiveFunction nonOptimalF = new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0, 4.0}), 1.0);
 SimplexTableau nonOptimalTableau = new SimplexTableau(nonOptimalF, new ArrayList<>(), GoalType.MAXIMIZE, false, EPSILON);
 nonOptimalTableau.dropPhase1Objective();
 double mostNegativeEntry = nonOptimalTableau.getEntry(0, nonOptimalTableau.columnLabels.size() - 1);
 assertFalse(Precision.compareTo(mostNegativeEntry, 0.0, EPSILON) > 0.0);
 }

 @Test
 public void testGetSolution() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 1.0), new ArrayList<>(), GoalType.MAXIMIZE, false, EPSILON);
 tableau.dropPhase1Objective();

 PointValuePair solution = tableau.getSolution();
 double[] coefficients = solution.getPoint();
 assertEquals(3, coefficients.length);
 assertTrue(Arrays.stream(coefficients).allMatch(d -> d > EPSILON || d < -EPSILON));
 assertEquals(1.0, tableau.f.getValue(coefficients), 0.0);
 }

 @Test
 public void testDivideRow() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 1.0), new ArrayList<>(), GoalType.MAXIMIZE, false, EPSILON);
 int dividendRow = 0;
 double divisor = 2.0;
 tableau.divideRow(dividendRow, divisor);

 double[][] expectedData = new double[][] {
 {0.5, 1.0, 1.5, 1.5, 1.5, 1.5, 3.0, 3.0, 3.0, 0.5, -0.5, -0.5, -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0},
 {1.0, -0.5, -1.5, -1.5, 1.0, 1.0, -3.0, -3.0, -3.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0}
 };
 RealMatrix expectedTableau = new Array2DRowRealMatrix(expectedData);
 RealMatrix tableauData = tableau.getData();
 assertArrayEquals(expectedTableau.getData(), tableauData.getData(), 0.0);
 }

 @Test
 public void testSubtractRow() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealVector(new double[] {1.0, 2.0, 3.0}), 1.0), new ArrayList<>(), GoalType.MAXIMIZE, false, EPSILON);
 int minuendRow = 0;
 int subtrahendRow = 1;
 double multiple = 2.0;
 tableau.subtractRow(minuendRow, subtrahendRow, multiple);

 double[][] expectedData = new double[][] {
 {-1.5, 1.5, 0.5, 1.5, 1.5, 1.5, 3.0, 3.0, 3.0, 0.5, -0.5, -0.5, -0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
 {-3.0, -1.5, -1.5, -3.5, 0.5, -0.5, 3.0, 3.0, 3.0, 1.5, 1.5, 1.5, 1.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
 {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0}
 };
 RealMatrix expectedTableau = new Array2DRowRealMatrix(expectedData);
 RealMatrix tableauData = tableau.getData();
 assertArrayEquals(expectedTableau.getData(), tableauData.getData(), 0.0);
 }

}
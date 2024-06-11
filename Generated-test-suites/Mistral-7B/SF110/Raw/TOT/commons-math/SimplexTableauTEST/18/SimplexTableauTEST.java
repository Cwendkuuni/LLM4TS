package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 private SimplexTableau tableau;

 @Before
 public void setUp() {
 LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-3.0, 2.0}, {-1.0, 1.0}}), 2.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0));
 tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-6, 20);
 }

 @Test
 public void testCreateTableau() {
 assertNotNull(tableau.getTableau());
 assertEquals(4, tableau.getHeight());
 assertEquals(7, tableau.getWidth());
 }

 @Test
 public void testInitializeColumnLabels() {
 assertEquals(7, tableau.getNumColumns());
 List<String> columnLabels = tableau.getColumnLabels();
 assertEquals("Z", columnLabels.get(0));
 assertEquals("W", columnLabels.get(1));
 for (int i = 0; i < 2; ++i) {
 assertEquals("x" + i, columnLabels.get(i + 2));
 }
 assertEquals("x-", columnLabels.get(4));
 for (int i = 0; i < 2; ++i) {
 assertEquals("s" + i, columnLabels.get(i + 5));
 }
 assertEquals("a" + 0, columnLabels.get(6));
 assertEquals("a" + 1, columnLabels.get(7));
 assertEquals("RHS", columnLabels.get(8));
 }

 @Test
 public void testInitializeTableauWithMaximizeObjective() {
 RealMatrix tableau = this.tableau.getTableau();
 assertEquals(1.0, tableau.getEntry(0, 0), 1e-6);
 assertEquals(-3.0, tableau.getEntry(0, 1), 1e-6);
 assertEquals(-2.0, tableau.getEntry(0, 2), 1e-6);
 assertEquals(-1.0, tableau.getEntry(0, 3), 1e-6);
 assertEquals(2.0, tableau.getEntry(0, 4), 1e-6);
 assertEquals(-1.0, tableau.getEntry(0, 5), 1e-6);
 assertEquals(-1.0, tableau.getEntry(0, 6), 1e-6);
 assertEquals(2.0, tableau.getEntry(0, 7), 1e-6);
 assertEquals(0.0, tableau.getEntry(0, 8), 1e-6);
 for (int i = 1; i < tableau.getHeight(); ++i) {
 assertEquals(0.0, tableau.getEntry(i, 0), 1e-6);
 }
 }

 @Test
 public void testInitializeTableauWithMinimizeObjective() {
 LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-3.0, 2.0}, {-1.0, 1.0}}), -2.0);
 tableau = new SimplexTableau(function, constraints, GoalType.MINIMIZE, false, 1e-6, 20);
 RealMatrix tableau = this.tableau.getTableau();
 assertEquals(-1.0, tableau.getEntry(0, 0), 1e-6);
 assertEquals(-3.0, tableau.getEntry(0, 1), 1e-6);
 assertEquals(-2.0, tableau.getEntry(0, 2), 1e-6);
 assertEquals(-1.0, tableau.getEntry(0, 3), 1e-6);
 assertEquals(2.0, tableau.getEntry(0, 4), 1e-6);
 assertEquals(-1.0, tableau.getEntry(0, 5), 1e-6);
 assertEquals(-1.0, tableau.getEntry(0, 6), 1e-6);
 assertEquals(2.0, tableau.getEntry(0, 7), 1e-6);
 assertEquals(0.0, tableau.getEntry(0, 8), 1e-6);
 for (int i = 1; i < tableau.getHeight(); ++i) {
 assertEquals(0.0, tableau.getEntry(i, 0), 1e-6);
 }
 }

 @Test
 public void testNormalizeConstraints() {
 LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0);
 LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
 assertArrayEquals(new double[][]{{-1.0, -1.0}, {1.0, 1.0}}, tableau.normalize(constraint1).getCoefficients().getDataRef());
 assertArrayEquals(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}, tableau.normalize(constraint2).getCoefficients().getDataRef());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeConstraintWithInvalidRelationship() {
 LinearConstraint constraint = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}}), Relationship.EQ, 0.0);
 tableau.normalize(constraint);
 fail("Exception not thrown");
 }

 @Test
 public void testGetNumObjectiveFunctions() {
 assertEquals(1, tableau.getNumObjectiveFunctions());
 LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-3.0, 2.0}, {-1.0, 1.0}}), 3.0);
 tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-6, 20);
 assertEquals(2, tableau.getNumObjectiveFunctions());
 }

 @Test
 public void testDropPhase1Objective() {
 tableau.dropPhase1Objective();
 assertEquals(1, tableau.getNumObjectiveFunctions());
 }

 @Test
 public void testIsOptimal() {
 assertTrue(tableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 PointValuePair solution = tableau.getSolution();
 assertEquals(new double[]{2.0 / 5.0, -1.0 / 5.0}, solution.getPoint(), 1e-6);
 assertEquals(5.0, solution.getValue(), 1e-6);
 }

 @Test
 public void testGetSolutionWithNonOptimalTableau() {
 LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-3.0, 2.0}, {-1.0, 1.0}}), 1.5);
 tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-6, 20);
 assertNull(tableau.getSolution());
 }

 @Test
 public void testDivideRow() {
 tableau.divideRow(0, 2);
 assertEquals(1.5, tableau.getEntry(0, 0), 1e-6);
 }

 @Test
 public void testSubtractRow() {
 LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-3.0, 2.0}, {-1.0, 1.0}}), 2.0);
 tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-6, 20);
 tableau.subtractRow(1, 0, 1);
 assertEquals(-1.0, tableau.getEntry(1, 1), 1e-6);
 }
}
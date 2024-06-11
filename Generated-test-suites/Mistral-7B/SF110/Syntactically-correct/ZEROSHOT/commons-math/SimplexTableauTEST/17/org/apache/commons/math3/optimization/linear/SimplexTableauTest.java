import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

 @Test
 public void testSimplexTableauConstruction() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 4.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MINIMIZE, false, 0.01);

 Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
 Assert.assertEquals(3, tableau.getNumDecisionVariables());
 Assert.assertEquals(2, tableau.getNumSlackVariables());
 Assert.assertEquals(1, tableau.getNumArtificialVariables());
 Assert.assertNotNull(tableau.getTableau());
 }

 @Test
 public void testInitializeColumnLabels() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 4.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MINIMIZE, false, 0.01);

 List<String> labels = tableau.getColumnLabels();

 Assert.assertEquals(11, labels.size());
 Assert.assertEquals("W", labels.get(0));
 Assert.assertEquals("Z", labels.get(1));
 Assert.assertEquals("x0", labels.get(2));
 Assert.assertEquals("x1", labels.get(3));
 Assert.assertEquals("x-", labels.get(4));
 Assert.assertEquals("s0", labels.get(5));
 Assert.assertEquals("s1", labels.get(6));
 Assert.assertEquals("s2", labels.get(7));
 Assert.assertEquals("a0", labels.get(8));
 Assert.assertEquals("a1", labels.get(9));
 Assert.assertEquals("RHS", labels.get(10));
 }

 @Test
 public void testCreateTableau() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 4.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MINIMIZE, false, 0.01);

 RealMatrix tableauMatrix = tableau.getTableau();

 Assert.assertEquals(5, tableauMatrix.getRowDimension());
 Assert.assertEquals(11, tableauMatrix.getColumnDimension());

 // Check W and Z columns
 Assert.assertEquals(-1.0, tableauMatrix.getEntry(0, 0), 0.0);
 Assert.assertEquals(-1.0, tableauMatrix.getEntry(1, 1), 0.0);

 // Check objective coefficients
 Assert.assertEquals(-2.0, tableauMatrix.getEntry(1, 2), 0.0);
 Assert.assertEquals(-1.0, tableauMatrix.getEntry(1, 3), 0.0);

 // Check decision variables coefficients
 Assert.assertEquals(1.0, tableauMatrix.getEntry(1, 4), 0.0);
 Assert.assertEquals(1.0, tableauMatrix.getEntry(1, 5), 0.0);

 // Check slack variables
 Assert.assertEquals(1.0, tableauMatrix.getEntry(1, 5), 0.0);
 Assert.assertEquals(-1.0, tableauMatrix.getEntry(2, 5), 0.0);
 Assert.assertEquals(-1.0, tableauMatrix.getEntry(3, 5), 0.0);

 // Check artificial variables
 Assert.assertEquals(1.0, tableauMatrix.getEntry(1, 8), 0.0);
 Assert.assertEquals(1.0, tableauMatrix.getEntry(2, 8), 0.0);
 Assert.assertEquals(1.0, tableauMatrix.getEntry(3, 8), 0.0);
 Assert.assertEquals(1.0, tableauMatrix.getEntry(4, 8), 0.0);

 // Check RHS values
 Assert.assertEquals(4.0, tableauMatrix.getEntry(2, 11), 0.0);
 Assert.assertEquals(2.0, tableauMatrix.getEntry(3, 11), 0.0);
 }

 @Test
 public void testIsOptimal() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 4.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MINIMIZE, false, 0.01);

 Assert.assertFalse(tableau.isOptimal());

 tableau.dropPhase1Objective();
 Assert.assertTrue(tableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 4.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MINIMIZE, false, 0.01);

 tableau.dropPhase1Objective();

 PointValuePair solution = tableau.getSolution();

 Assert.assertNotNull(solution.getPoint());
 Assert.assertTrue(solution.getValue() > 0.0);
 }

 @Test
 public void testEquals() {
 SimplexTableau tableau1 = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 4.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MINIMIZE, false, 0.01);

 SimplexTableau tableau2 = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 4.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MINIMIZE, false, 0.01);

 SimplexTableau tableau3 = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 5.0), // different RHS
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MINIMIZE, false, 0.01);

 SimplexTableau tableau4 = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {2.0, 1.0}}), 0.0), Arrays.asList(
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 1.0}}), Relationship.LEQ, 4.0),
 new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.GEQ, 2.0)
 ), GoalType.MAXIMIZE, false, 0.01); // different goal type

 Assert.assertTrue(tableau1.equals(tableau2));
 Assert.assertFalse(tableau1.equals(tableau3));
 Assert.assertFalse(tableau1.equals(tableau4));
 }
}
package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 private SimplexTableau simplexTableau;
 private LinearObjectiveFunction objectiveFunction;
 private LinearConstraint constraint1;
 private LinearConstraint constraint2;
 private List<LinearConstraint> constraints;

 @Before
 public void setUp() {
 objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
 constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 3.0}}), Relationship.LEQ, 4.0);
 constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, 2.0}}), Relationship.GEQ, 2.0);
 constraints = new ArrayList<>();
 constraints.add(constraint1);
 constraints.add(constraint2);
 simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
 }

 @Test
 public void testCreateTableau() {
 // Testing the tableau creation with the given input
 double[][] expectedTableau = {
 {'-1.0', '1.0', '2.0', '1.0', '1.0', '3.0', '4.0', '5.0', '1.0', '1.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0'},
 {'1.0', '-1.0', '3.0', '1.0', '-1.0', '1.0', '0.0', '0.0', '0.0', '0.0', '0.0', '1.0', '1.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0'},
 {'0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '1.0', '-1.0', '3.0', '1.0', '-1.0', '1.0', '1.0', '0.0', '0.0', '0.0', '0.0', '0.0'},
 {'0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '1.0', '-1.0', '3.0', '1.0', '-1.0', '1.0', '1.0', '0.0', '0.0', '0.0'},
 {'1.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0'},
 {'0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0'}
 };
 RealMatrix actualTableau = simplexTableau.getTableau();
 Assert.assertArrayEquals("Tableau not created as expected", expectedTableau.flatMapToDouble(Arrays::stream).mapToDouble(d -> d).toArray(), actualTableau.getDataRef()[0]);
 }

 @Test
 public void testInitializeColumnLabels() {
 // Testing the initialization of column labels
 List<String> expectedColumnLabels = Arrays.asList("Z", "x1", "x2", "x-", "s1", "s2");
 List<String> actualColumnLabels = simplexTableau.getColumnLabels();
 Assert.assertEquals("Column labels not initialized as expected", expectedColumnLabels, actualColumnLabels);
 }

 @Test
 public void testNormalizeConstraints() {
 // Testing the normalization of constraints
 LinearConstraint normalizedConstraint1 = simplexTableau.normalize(constraint1);
 LinearConstraint normalizedConstraint2 = simplexTableau.normalize(constraint2);

 List<LinearConstraint> expectedNormalizedConstraints = Arrays.asList(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}}), Relationship.GEQ, 0.0), new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {1.0, 2.0}}), Relationship.LEQ, 0.0));
 List<LinearConstraint> actualNormalizedConstraints = simplexTableau.getConstraints();
 Assert.assertEquals("Constraints not normalized as expected", expectedNormalizedConstraints, actualNormalizedConstraints);
 }

 @Test
 public void testGetNumObjectiveFunctions() {
 // Testing the number of objective functions
 int actualNumObjectiveFunctions = simplexTableau.getNumObjectiveFunctions();
 assertEquals("Incorrect number of objective functions", 1, actualNumObjectiveFunctions);
 }

 @Test
 public void testDropPhase1Objective() {
 // Testing the dropping of the phase 1 objective function
 simplexTableau.dropPhase1Objective();
 double[][] expectedTableauAfterDropping = {
 {'0.0', '1.0', '2.0', '1.0', '1.0', '3.0', '4.0', '5.0', '1.0', '1.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0'},
 {'1.0', '-1.0', '3.0', '1.0', '-1.0', '1.0', '0.0', '0.0', '0.0', '0.0', '0.0', '1.0', '1.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0'},
 {'0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '1.0', '-1.0', '3.0', '1.0', '-1.0', '1.0', '1.0', '0.0', '0.0', '0.0', '0.0', '0.0'},
 {'0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '1.0', '-1.0', '3.0', '1.0', '-1.0', '1.0', '1.0', '0.0', '0.0', '0.0'},
 {'0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0'},
 {'0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0', '0.0'}
 };
 RealMatrix actualTableauAfterDropping = simplexTableau.getTableau();
 Assert.assertArrayEquals("Tableau not dropped as expected", expectedTableauAfterDropping.flatMapToDouble(Arrays::stream).mapToDouble(d -> d).toArray(), actualTableauAfterDropping.getDataRef()[0]);
 }

 @Test
 public void testIsOptimal() {
 // Testing the optimization status
 assertTrue("Tableau is not optimal", simplexTableau.isOptimal());
 }

 @Test
 public void testGetSolution() {
 // Testing the optimization solution
 PointValuePair expectedSolution = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, 33.0 / 3.0);
 PointValuePair actualSolution = simplexTableau.getSolution();
 assertEquals("Solution not found as expected", expectedSolution, actualSolution);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSolutionWithoutOptimization() {
 // Testing the solution retrieval without optimization
 simplexTableau = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0), Collections.emptyList(), GoalType.MAXIMIZE, false, 1e-6, 10);
 simplexTableau.getSolution();
 }
}
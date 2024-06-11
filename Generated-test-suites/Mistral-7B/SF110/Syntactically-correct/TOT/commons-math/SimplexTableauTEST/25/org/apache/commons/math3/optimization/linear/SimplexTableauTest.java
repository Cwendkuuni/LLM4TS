package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 @BeforeClass
 public static void setUpClass() {
 // Set up any static resources or initialize any class-level fixtures here
 }

 @AfterClass
 public static void tearDownClass() {
 // Perform any cleanup necessary to prepare for the next test run
 }

 @Before
 public void setUp() {
 // Set up any instance variables or prepare any instance-level fixtures here
 }

 @After
 public void tearDown() {
 // Perform any cleanup necessary to prepare for the next test method
 }

 @Test
 public void testSimplexTableauConstruction() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[] { 2.0, -1.0, 3.0 }), 2.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { -1.0, 1.0, 1.0 }), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { 1.0, 1.0, -1.0 }), Relationship.GEQ, 0.0));
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
 assertEquals(3, tableau.getOriginalNumDecisionVariables());
 assertEquals(2, tableau.getNumDecisionVariables());
 assertEquals(2, tableau.getNumSlackVariables());
 assertEquals(1, tableau.getNumArtificialVariables());
 assertEquals(2, tableau.getNumObjectiveFunctions());
 assertTrue(Arrays.equals(new double[] { 2.0, -1.0, 3.0 }, tableau.f.getCoefficients().toArray()));
 assertEquals(2, tableau.constraints.size());
 assertTrue(Arrays.equals(new double[] { -1.0, 1.0, 1.0 }, tableau.constraints.get(0).getCoefficients().toArray()));
 assertTrue(Arrays.equals(new double[] { 1.0, 1.0, -1.0 }, tableau.constraints.get(1).getCoefficients().toarray()));
 }

 @Test
 public void testSimplexTableauNormalizeConstraints() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[] { 2.0, -1.0, 3.0 }), 2.0);
 List<LinearConstraint> originalConstraints = new ArrayList<>();
 originalConstraints.add(new LinearConstraint(new ArrayRealVector(new double[] { -1.0, 1.0, 1.0 }), Relationship.LEQ, 0.0));
 originalConstraints.add(new LinearConstraint(new ArrayRealVector(new double[] { 1.0, 1.0, -1.0 }), Relationship.GEQ, 0.0));
 SimplexTableau tableau = new SimplexTableau(f, originalConstraints, GoalType.MAXIMIZE, false, 1e-12);
 List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);
 assertEquals(2, normalizedConstraints.size());
 assertTrue(Arrays.equals(new LinearConstraint(new ArrayRealVector(new double[] { 1.0, -1.0, -1.0 }), Relationship.LEQ, 0.0).toString(), normalizedConstraints.get(0).toString()));
 assertTrue(Arrays.equals(new LinearConstraint(new ArrayRealVector(new double[] { -1.0, 1.0, 1.0 }), Relationship.GEQ, 0.0).toString(), normalizedConstraints.get(1).toString()));
 }

 @Test
 public void testSimplexTableauCreateTableau() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[] { 2.0, -1.0, 3.0 }), 2.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { -1.0, 1.0, 1.0 }), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { 1.0, 1.0, -1.0 }), Relationship.GEQ, 0.0));
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
 RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][] {
 {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0},
 {-1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0},
 { 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
 { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
 });
 RealMatrix tableauMatrix = tableau.tableau;
 assertArrayEquals(expectedTableau.getData(), tableauMatrix.getData(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimplexTableauWithInvalidConstraint() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[] { 2.0, -1.0, 3.0 }), 2.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { -1.0, 1.0, 1.0 }), Relationship.LEQ, 1.0));
 SimplexTableau tableau = null;
 try {
 tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
 } catch (IllegalArgumentException e) {
 tableau = e.getSuppressed(IllegalArgumentException.class)[0];
 }
 assertNotNull(tableau);
 assertEquals("Number of constraints must be positive", tableau.getMessage());
 }

 @Test
 public void testSimplexTableauIsOptimal() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[] { 2.0, -1.0, 3.0 }), 2.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { -1.0, 1.0, 1.0 }), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { 1.0, 1.0, -1.0 }), Relationship.GEQ, 0.0));
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
 assertTrue(tableau.isOptimal());
 }

 @Test
 public void testSimplexTableauGetSolution() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[] { 2.0, -1.0, 3.0 }), 2.0);
 List<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { -1.0, 1.0, 1.0 }), Relationship.LEQ, 0.0));
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[] { 1.0, 1.0, -1.0 }), Relationship.GEQ, 0.0));
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
 PointValuePair solution = tableau.getSolution();
 double[] coefficients = solution.getPoint();
 assertEquals(3, coefficients.length);
 assertTrue(Arrays.equals(new double[] { 1.0 / 2.0, 1.0 / 2.0, 0.0 }, coefficients));
 assertEquals(6.0, tableau.f.getValue(coefficients), 0.0);
 }
}
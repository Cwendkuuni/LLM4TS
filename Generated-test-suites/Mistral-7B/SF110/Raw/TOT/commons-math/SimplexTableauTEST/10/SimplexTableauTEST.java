package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

 @Test
 public void testSimplexTableauConstruction() {
 LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}}, new double[]{-4.0, 5.0, 6.0});
 Collection<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.GEQ, 0.0));
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

 // Test the number of columns and rows in the tableau
 assertEquals(11, tableau.getWidth());
 assertEquals(3, tableau.getHeight());

 // Test the objective function and constraints
 assertEquals(f.getCoefficients(), tableau.getData()[0]);
 assertEquals(constraints.get(0).getCoefficients(), tableau.getData()[1]);
 assertEquals(constraints.get(0).getRelationship(), tableau.getData()[1].getEntry(tableau.getWidth() - 1) > 0 ? Relationship.LEQ : Relationship.GEQ);
 }

 @Test
 public void testSimplexTableauNormalizeConstraints() {
 LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}}, new double[]{-4.0, 5.0, 6.0});
 Collection<LinearConstraint> originalConstraints = new ArrayList<>();
 originalConstraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.GEQ, 0.0));
 originalConstraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0));
 SimplexTableau tableau = new SimplexTableau(f, originalConstraints, GoalType.MINIMIZE, false, 1e-6, 10);

 // Test the normalized constraints
 LinearConstraint normalizedConstraint1 = tableau.normalizeConstraints().get(0);
 LinearConstraint normalizedConstraint2 = tableau.normalizeConstraints().get(1);

 assertEquals(-1.0 * new ArrayRealVector(new double[]{1.0, -1.0}), normalizedConstraint1.getCoefficients());
 assertEquals(Relationship.LEQ, normalizedConstraint1.getRelationship());
 assertEquals(0.0, normalizedConstraint1.getValue());

 assertEquals(new ArrayRealVector(new double[]{1.0, 1.0}), normalizedConstraint2.getCoefficients());
 assertEquals(Relationship.LEQ, normalizedConstraint2.getRelationship());
 assertEquals(3.0, normalizedConstraint2.getValue());
 }

 @Test
 public void testSimplexTableauIsOptimal() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}), new ArrayRealVector(new double[]{-4.0, 5.0}));
 Collection<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.GEQ, 0.0));
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0));
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

 // Test that the tableau is not optimal yet
 assertFalse(tableau.isOptimal());

 // Test that the solution is optimal
 tableau.dropPhase1Objective();
 PointValuePair solution = tableau.getSolution();
 double[] coefficients = solution.getPoint();
 double value = solution.getValue();

 assertEquals(Arrays.asList(0.0, 3.0), Arrays.asList(coefficients[0], coefficients[1]));
 assertEquals(-11.0, value, 1e-6);

 // Test that the tableau is now optimal
 assertTrue(tableau.isOptimal());
 }

 @Test
 public void testSimplexTableauGetSolution() {
 LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}}, new double[]{-4.0, 5.0, 6.0});
 Collection<LinearConstraint> constraints = new ArrayList<>();
 constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.GEQ, 0.0));
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

 PointValuePair solution = tableau.getSolution();
 double[] coefficients = solution.getPoint();
 double value = solution.getValue();

 assertEquals(Arrays.asList(2.0 / 3.0, 1.0 - 2.0 / 3.0), Arrays.asList(coefficients[0], coefficients[1]));
 assertEquals(-11.0 / 3.0, value, 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimplexTableauInvalidArguments() {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{}), new ArrayRealVector(new double[]{}));
 Collection<LinearConstraint> constraints = new ArrayList<>();
 SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
 }
}
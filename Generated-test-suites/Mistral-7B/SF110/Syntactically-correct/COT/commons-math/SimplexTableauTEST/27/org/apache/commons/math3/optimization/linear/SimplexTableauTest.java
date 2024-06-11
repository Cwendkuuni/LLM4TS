package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

import java.util.*;
import java.io.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, -3.0}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;
        int maxUlps = 12;

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);

        // Basic checks
        assertEquals(goalType, tableau.getGoalType());
        assertEquals(restrictToNonNegative, tableau.isRestrictToNonNegative());
        assertEquals(epsilon, tableau.getEpsilon(), maxUlps);
        assertEquals(maxUlps, tableau.getMaxUlps());
        assertEquals(f, tableau.getObjectiveFunction());
        assertEquals(constraints, tableau.getConstraints());

        // Check tableau dimensions and data
        int expectedWidth = 13;
        int expectedHeight = 3;
        RealMatrix tableau = tableau.getData();
        assertEquals(expectedWidth, tableau.getColumnDimension());
        assertEquals(expectedHeight, tableau.getRowDimension());

        // Check objective function coefficients and constant term
        double[] objCoefficients = tableau.getRowVector(0);
        assertEquals(expectedWidth - 1, objCoefficients.length);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, objCoefficients, maxUlps);
        assertEquals(-1.0 * f.getConstantTerm(), tableau.getEntry(0, expectedWidth - 1), maxUlps);

        // Check constraint coefficients and constants
        for (int i = 0; i < constraints.size(); ++i) {
            LinearConstraint constraint = constraints.get(i);
            int row = i + expectedHeight;
            double[] constraintCoefficients = tableau.getRowVector(row);
            assertEquals(constraint.getCoefficients().getDimension(), constraintCoefficients.length);
            assertArrayEquals(constraint.getCoefficients().toArray(), constraintCoefficients, maxUlps);
            assertEquals(constraint.getValue(), tableau.getEntry(row, expectedWidth - 1), maxUlps);
        }
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, -3.0}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;
        int maxUlps = 12;

        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint1);
        originalConstraints.add(constraint2);

        List<LinearConstraint> normalizedConstraints = new SimplexTableau(f, originalConstraints, goalType, restrictToNonNegative, epsilon, maxUlps).normalizeConstraints(originalConstraints);

        // Check that the number of constraints is unchanged
        assertEquals(originalConstraints.size(), normalizedConstraints.size());

        // Check that each constraint has been normalized correctly
        for (int i = 0; i < originalConstraints.size(); ++i) {
            LinearConstraint originalConstraint = originalConstraints.get(i);
            LinearConstraint normalizedConstraint = normalizedConstraints.get(i);

            assertEquals(originalConstraint.getCoefficients().getDimension(), normalizedConstraint.getCoefficients().getDimension());
            assertArrayEquals(originalConstraint.getCoefficients().toArray(), normalizedConstraint.getCoefficients().toArray(), maxUlps);

            if (originalConstraint.getValue() < 0.0) {
                assertEquals(Relationship.LEQ, normalizedConstraint.getRelationship());
                assertEquals(-1.0 * originalConstraint.getValue(), normalizedConstraint.getValue(), maxUlps);
            } else {
                assertEquals(Relationship.GEQ, normalizedConstraint.getRelationship());
                assertEquals(originalConstraint.getValue(), normalizedConstraint.getValue(), maxUlps);
            }
        }
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, -3.0}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;
        int maxUlps = 12;

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
        RealMatrix tableauMatrix = tableau.getData();

        // Check the tableau for the given inputs (the results may vary as the pivot selection is random)
        int expectedWidth = 13;
        int expectedHeight = 3;
        int numDecisionVariables = f.getCoefficients().getDimension();
        int numSlackVariables = 2;
        int numArtificialVariables = 2;

        // Check the first row (objective function)
        double[] objCoefficients = tableauMatrix.getRowVector(0);
        assertEquals(expectedWidth - 1, objCoefficients.length);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, objCoefficients, maxUlps);
        assertEquals(-1.0 * f.getConstantTerm(), tableauMatrix.getEntry(0, expectedWidth - 1), maxUlps);

        // Check the objective function coefficients for the non-negative case
        if (!restrictToNonNegative) {
            double[] objCoefficientsNegative = tableauMatrix.getRowVector(expectedHeight + numDecisionVariables);
            assertEquals(expectedWidth - 1, objCoefficientsNegative.length);
            assertEquals(-1.0, tableauMatrix.getEntry(expectedHeight + numDecisionVariables, expectedWidth - 1), maxUlps);
            assertArrayEquals(new Array2DRowRealMatrix(new double[][]{{-1.0}}).getColumnVector(0).toArray(), objCoefficientsNegative, maxUlps);
        }

        // Check the remaining rows (constraints)
        for (int i = 0; i < constraints.size(); ++i) {
            LinearConstraint constraint = constraints.get(i);
            int row = i + expectedHeight;
            double[] constraintCoefficients = tableauMatrix.getRowVector(row);
            assertEquals(constraint.getCoefficients().getDimension(), constraintCoefficients.length);
            assertArrayEquals(constraint.getCoefficients().toArray(), constraintCoefficients, maxUlps);

            int relationship = (constraint.getRelationship() == Relationship.LEQ) ? 1 : -1;
            double rhs = constraint.getValue();

            // Check the entries for the given constraint
            for (int j = 0; j < numDecisionVariables + numSlackVariables + numArtificialVariables; ++j) {
                if (j == expectedWidth - 1) {
                    assertEquals(rhs, tableauMatrix.getEntry(row, j), maxUlps);
                } else {
                    double expectedEntry;
                    if (i < numSlackVariables && constraint.getRelationship() == Relationship.LEQ) {
                        expectedEntry = 1.0;
                    } else if (i >= numSlackVariables && constraint.getRelationship() == Relationship.GEQ) {
                        expectedEntry = -1.0;
                    } else {
                        expectedEntry = constraint.getCoefficients().getEntry(j);
                    }
                    assertEquals(expectedEntry, tableauMatrix.getEntry(row, j), maxUlps);
                }
            }
        }
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, -3.0}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;
        int maxUlps = 12;

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);

        // Test with an optimal tableau
        tableau.setEntry(1, 12, 0.0);
        tableau.setEntry(1, 13, 0.0);
        tableau.setEntry(2, 12, 0.0);
        tableau.setEntry(2, 13, 0.0);
        tableau.setEntry(2, 14, 0.0);

        assertTrue(tableau.isOptimal());

        // Test with a non-optimal tableau
        tableau.setEntry(1, 12, 1.0);
        tableau.setEntry(1, 13, 1.0);
        tableau.setEntry(2, 12, -1.0);
        tableau.setEntry(2, 13, -1.0);
        tableau.setEntry(2, 14, 1.0);

        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, -3.0}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;
        int maxUlps = 12;

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);

        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();
        double value = solution.getValue();

        // Test with a simple solution
        tableau.setEntry(1, 12, 0.0);
        tableau.setEntry(1, 13, 0.0);
        tableau.setEntry(2, 12, 1.0);
        tableau.setEntry(2, 13, 1.0);
        tableau.setEntry(2, 14, 0.0);

        assertEquals(new double[]{0.0, 1.0, 0.0}, coefficients, maxUlps);
        assertEquals(3.0, value, maxUlps);

        // Test with a more complex solution
        tableau.setEntry(1, 12, 2.0);
        tableau.setEntry(1, 13, -1.0);
        tableau.setEntry(2, 12, -1.0);
        tableau.setEntry(2, 13, -2.0);
        tableau.setEntry(2, 14, 2.0);

        assertEquals(new double[]{0.5, 1.5, -0.5}, coefficients, maxUlps);
        assertEquals(4.5, value, maxUlps);

        // Test with a degenerate solution
        tableau.setEntry(1, 12, 0.0);
        tableau.setEntry(1, 13, 0.0);
        tableau.setEntry(2, 12, 0.0);
        tableau.setEntry(2, 13, 0.0);
        tableau.setEntry(2, 14, 0.0);
        tableau.setEntry(3, 13, 1.0);

        assertEquals(new double[]{0.0, 0.0, 0.0}, coefficients, maxUlps);
        assertEquals(0.0, value, maxUlps);
    }
}
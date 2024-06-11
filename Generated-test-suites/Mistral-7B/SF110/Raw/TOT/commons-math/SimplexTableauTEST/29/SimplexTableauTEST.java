package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -2.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 14;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);

        Assert.assertEquals(goalType, tableau.getGoalType());
        Assert.assertEquals(restrictToNonNegative, tableau.isRestrictToNonNegative());
        Assert.assertEquals(epsilon, tableau.getEpsilon(), maxUlps);
        Assert.assertEquals(maxUlps, tableau.getMaxUlps());
        Assert.assertEquals(f, tableau.getObjectiveFunction());
        List<LinearConstraint> constraints = tableau.getConstraints();
        Assert.assertEquals(2, constraints.size());
        Assert.assertEquals(constraint1, constraints.get(0));
        Assert.assertEquals(constraint2, constraints.get(1));

        // Check that column labels are initialized correctly
        List<String> columnLabels = tableau.getColumnLabels();
        Assert.assertEquals(15, columnLabels.size());
        Assert.assertEquals("W", columnLabels.get(0));
        Assert.assertEquals("Z", columnLabels.get(1));
        for (int i = 0; i < 3; ++i) {
            Assert.assertEquals("x" + i, columnLabels.get(i + 2));
        }
        if (!restrictToNonNegative) {
            Assert.assertEquals("x-", columnLabels.get(columnLabels.size() - 2));
        }
        for (int i = 0; i < 2; ++i) {
            Assert.assertEquals("s" + i, columnLabels.get(columnLabels.size() - i - 1));
        }
        for (int i = 0; i < 2; ++i) {
            Assert.assertEquals("a" + i, columnLabels.get(columnLabels.size() - 3 - i));
        }
        Assert.assertEquals("RHS", columnLabels.get(columnLabels.size() - 1));
    }

    @Test
    public void testInvalidConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -2.0}), Relationship.GEQ, 2.0);
        LinearConstraint invalidConstraint = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.UNKNOWN, 0.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 14;

        try {
            new SimplexTableau(f, Arrays.asList(constraint1, constraint2, invalidConstraint), goalType, restrictToNonNegative, epsilon, maxUlps);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimplexTableauInitialization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -2.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 14;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);

        // Test tableau creation
        Assert.assertNotNull(tableau.getData());
        Assert.assertEquals(tableau.getHeight(), tableau.getData().length);
        Assert.assertEquals(tableau.getWidth(), tableau.getData()[0].length);

        // Test column labels
        List<String> columnLabels = tableau.getColumnLabels();
        Assert.assertEquals(15, columnLabels.size());

        // Test objective function values
        PointValuePair solution1 = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, solution1.getPoint(), maxUlps);
        double objectiveValue1 = solution1.getValue();
        Assert.assertEquals(Double.NEGATIVE_INFINITY, objectiveValue1, maxUlps);

        // Test constraints
        List<LinearConstraint> constraints = tableau.getConstraints();
        for (LinearConstraint constraint : constraints) {
            PointValuePair solution = tableau.getSolution();
            double rhsValue = constraint.getValue();
            switch (constraint.getRelationship()) {
                case LEQ:
                    Assert.assertTrue(solution.getPoint()[constraint.getIndices().getDimension()] <= rhsValue);
                    break;
                case GEQ:
                    Assert.assertTrue(solution.getPoint()[constraint.getIndices().getDimension()] >= rhsValue);
                    break;
                case EQ:
                    Assert.assertEquals(rhsValue, solution.getPoint()[constraint.getIndices().getDimension()], maxUlps);
                    break;
            }
        }

        // Test dropping phase 1 objective
        tableau.dropPhase1Objective();
        Assert.assertEquals(1, tableau.getNumObjectiveFunctions());
        Assert.assertArrayEquals(new double[]{0.0}, tableau.getEntry(0, 0), maxUlps);
        Assert.assertEquals(0.0, tableau.getEntry(0, 1), maxUlps);
        Assert.assertEquals(0.0, tableau.getEntry(0, 2), maxUlps);
        List<String> newColumnLabels = tableau.getColumnLabels();
        Assert.assertEquals(14, newColumnLabels.size());
        Assert.assertFalse(newColumnLabels.contains("W"));
        Assert.assertFalse(newColumnLabels.contains("Z"));
        Assert.assertEquals("x-", newColumnLabels.get(newColumnLabels.size() - 2));

        // Test is optimal
        Assert.assertTrue(tableau.isOptimal());
    }

    @Test
    public void testSimplexTableauInvalidConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -2.0}), Relationship.GEQ, 2.0);
        LinearConstraint invalidConstraint = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.EQ, 0.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 14;

        try {
            SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2, invalidConstraint), goalType, restrictToNonNegative, epsilon, maxUlps);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimplexTableauInvalidGoalType() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -2.0}), Relationship.GEQ, 2.0);
        GoalType invalidGoalType = null;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 14;

        try {
            SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), invalidGoalType, restrictToNonNegative, epsilon, maxUlps);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimplexTableauInvalidMaxUlps() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -2.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int invalidMaxUlps = -1;

        try {
            SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, invalidMaxUlps);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimplexTableauNegativeEpsilon() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -2.0}), Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double negativeEpsilon = -1.0E-6;
        int maxUlps = 14;

        try {
            SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, negativeEpsilon, maxUlps);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimplexTableauNullConstraintCollection() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 14;

        try {
            SimplexTableau tableau = new SimplexTableau(f, null, goalType, restrictToNonNegative, epsilon, maxUlps);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimplexTableauEmptyConstraintCollection() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 14;

        List<LinearConstraint> emptyConstraints = Collections.emptyList();
        try {
            SimplexTableau tableau = new SimplexTableau(f, emptyConstraints, goalType, restrictToNonNegative, epsilon, maxUlps);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
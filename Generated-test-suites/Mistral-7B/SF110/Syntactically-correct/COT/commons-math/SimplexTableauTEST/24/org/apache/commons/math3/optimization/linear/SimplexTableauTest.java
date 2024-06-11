package org.junit.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.LEQ, 5.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{2.0, 3.0, 1.0}), Relationship.GEQ, 4.0);
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(1.0E-6, tableau.getEpsilon(), 1.0E-8);
        assertEquals(10, tableau.getMaxUlps());
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.LEQ, 5.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{2.0, 3.0, 1.0}), Relationship.GEQ, 4.0);
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint1);
        originalConstraints.add(constraint2);

        List<LinearConstraint> normalizedConstraints = new SimplexTableau(objectiveFunction, originalConstraints, GoalType.MAXIMIZE, false, 1.0E-6, 10).normalizeConstraints(originalConstraints);

        assertEquals(2, normalizedConstraints.size());
        assertEquals(constraint1.getValue(), normalizedConstraints.get(0).getValue(), 1.0E-8);
        assertEquals(constraint2.getValue(), normalizedConstraints.get(1).getValue(), 1.0E-8);
        assertEquals(constraint1.getRelationship(), normalizedConstraints.get(0).getRelationship());
        assertEquals(constraint2.getRelationship(), normalizedConstraints.get(1).getRelationship());
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0), new ArrayList<>(), GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(1, tableau.getNumObjectiveFunctions());

        tableau = new SimplexTableau(new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0), new ArrayList<>(), GoalType.MAXIMIZE, false, 1.0E-6, 10, 2);
        assertEquals(2, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testGetConstraintTypeCounts() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraintLEQ = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.LEQ, 5.0);
        LinearConstraint constraintGEQ = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.GEQ, 5.0);
        LinearConstraint constraintEQ = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.EQ, 5.0);
        constraints.add(constraintLEQ);
        constraints.add(constraintGEQ);
        constraints.add(constraintEQ);

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(2, tableau.getConstraintTypeCounts(Relationship.LEQ));
        assertEquals(1, tableau.getConstraintTypeCounts(Relationship.GEQ));
        assertEquals(1, tableau.getConstraintTypeCounts(Relationship.EQ));
    }

    @Test
    public void testGetInvertedCoefficientSum() {
        RealVector coefficients = new ArrayRealVector(new double[]{-1.0, 2.0, -1.5, 3.0, 2.5});
        double result = SimplexTableau.getInvertedCoefficientSum(coefficients);
        assertEquals(-1.0, result, 1.0E-8);
    }

    @Test(expected = AssertionError.class)
    public void testGetBasicRowNullEntry() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 1.0E-6, 10, matrix);
        Integer nullEntryRow = tableau.getBasicRow(2);
        assertNull(nullEntryRow);
    }

    @Test(expected = AssertionError.class)
    public void testGetBasicRowNonZeroEntry() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 1.0E-6, 10, matrix);
        Integer nonZeroEntryRow = tableau.getBasicRow(1);
        assertNull(nonZeroEntryRow);
        double nonZeroEntry = tableau.getEntry(1, 2);
        assertEquals(5.0, nonZeroEntry, 1.0E-8);
    }

    @Test
    public void testIsOptimal() {
        RealMatrix tableauMatrix = new Array2DRowRealMatrix(new double[][]{{0.0, 5.0, 3.0, 2.0, 1.0}, {-1.0, 2.0, 4.0, 0.0, 0.0}, {0.0, 0.0, 0.0, -1.0, 0.0}});
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 1.0E-6, 10, tableauMatrix);
        assertTrue(tableau.isOptimal());

        tableauMatrix = new Array2DRowRealMatrix(new double[][]{{0.0, 5.0, 3.0, 2.0, 1.0}, {-1.0, 2.0, 4.0, 0.0, 0.0}, {0.0, 0.0, 0.0, -1.0, 1.0}});
        tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 1.0E-6, 10, tableauMatrix);
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        RealMatrix tableauMatrix = new Array2DRowRealMatrix(new double[][]{{0.0, 5.0, 3.0, 2.0, 1.0, 4.0}, {-1.0, 2.0, 4.0, 0.0, 0.0, 2.0}, {0.0, 0.0, 0.0, -1.0, 1.0, 3.0}, {0.0, 0.0, 0.0, -1.0, 0.0, 1.0}});
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 1.0E-6, 10, tableauMatrix);
        PointValuePair solution = tableau.getSolution();

        double[] coefficients = solution.getPoint();
        assertEquals(6.0, coefficients[0], 1.0E-8);
        assertEquals(2.0, coefficients[1], 1.0E-8);
        assertEquals(3.0, coefficients[2], 1.0E-8);
        assertEquals(-1.0, coefficients[3], 1.0E-8);
        assertEquals(-1.0, coefficients[4], 1.0E-8);

        assertEquals(12.0, tableau.f.getValue(coefficients), 1.0E-8);
    }

    @Test
    public void testDropPhase1Objective() {
        RealMatrix tableauMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0}, {-1.0, 2.0, 4.0, 0.0, 0.0, 2.0, 0.0, 3.0}, {0.0, -1.0, 0.0, -1.0, 1.0, 0.0, -1.0, 1.0}, {0.0, 0.0, 0.0, -1.0, 0.0, 1.0, -1.0, 1.0}, {0.0, 0.0, 0.0, -1.0, 0.0, 1.0, -1.0, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}});
        SimplexTableau tableau = new SimplexTableau(null, null, GoalType.MAXIMIZE, false, 1.0E-6, 10, tableauMatrix);
        tableau.dropPhase1Objective();

        assertEquals(5, tableau.getNumArtificialVariables());
        assertEquals(5, tableau.getWidth() - tableau.getNumArtificialVariables());
        assertEquals(4, tableau.getHeight());
        assertTrue(Arrays.stream(tableau.columnLabels.toArray()).noneMatch(colLabel -> colLabel.equals("x-")));
    }
}
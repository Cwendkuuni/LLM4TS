package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{1.0, 2.0}));
        SimplexTableau tableau = new SimplexTableau(objFunc, new ArrayList<LinearConstraint>(), GoalType.MAXIMIZE, false, 1.0E-12);
        List<String> labels = tableau.columnLabels;

        assertEquals(13, labels.size());
        assertEquals("W", labels.get(0));
        assertEquals("Z", labels.get(1));
        for (int i = 0; i < 2; i++) {
            assertEquals("x" + i, labels.get(2 + i));
        }
        if (!tableau.restrictToNonNegative) {
            assertEquals("x-", labels.get(labels.size() - 3));
        }
        for (int i = 0; i < 3; i++) {
            assertEquals("s" + i, labels.get(labels.size() - 2 - i));
        }
        for (int i = 0; i < 2; i++) {
            assertEquals("a" + i, labels.get(labels.size() - i));
        }
        assertEquals("RHS", labels.get(labels.size() - 1));
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, -1.5}, {0.5, -1.5}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, new ArrayList<LinearConstraint>(){{add(constraint1); add(constraint2);}}, GoalType.MAXIMIZE, false, 1.0E-12);

        assertEquals(4, tableau.getHeight());
        assertEquals(13, tableau.getWidth());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                assertEquals(0.0, tableau.getEntry(i, j), 1.0E-12);
            }
        }
        for (int i = 0; i < 2; i++) {
            assertEquals(-1.0, tableau.getEntry(0, 2 + i), 1.0E-12);
            assertEquals(1.0, tableau.getEntry(0, 13 - i), 1.0E-12);
        }
        assertEquals(-1.0, tableau.getEntry(1, 2), 1.0E-12);
        assertEquals(-1.0, tableau.getEntry(1, 13 - 1), 1.0E-12);
        assertEquals(-2.0, tableau.getEntry(2, 2), 1.0E-12);
        assertEquals(-2.0, tableau.getEntry(2, 13 - 1), 1.0E-12);
        assertEquals(1.0, tableau.getEntry(3, 12), 1.0E-12);
        assertEquals(1.0, tableau.getEntry(3, 13), 1.0E-12);
        assertEquals(1.0, tableau.getEntry(3, 13 - 2), 1.0E-12);
        assertEquals(1.0, tableau.getEntry(3, 13 - 3), 1.0E-12);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, -1.5}, {0.5, -1.5}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, new ArrayList<LinearConstraint>(){{add(constraint1); add(constraint2);}}, GoalType.MAXIMIZE, false, 1.0E-12);
        LinearConstraint[] normalizedConstraints = tableau.normalizeConstraints(Arrays.asList(constraint1, constraint2)).toArray(new LinearConstraint[0]);

        assertEquals(2, normalizedConstraints.length);
        assertEquals(constraint1.getCoefficients().toString(), normalizedConstraints[0].getCoefficients().toString());
        assertEquals(Relationship.LEQ, normalizedConstraints[0].getRelationship());
        assertEquals(-1.0, normalizedConstraints[0].getValue(), 1.0E-12);

        assertEquals(constraint2.getCoefficients().toString(), normalizedConstraints[1].getCoefficients().toString());
        assertEquals(Relationship.GEQ, normalizedConstraints[1].getRelationship());
        assertEquals(-1.0, normalizedConstraints[1].getValue(), 1.0E-12);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, -1.5}, {0.5, -1.5}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, new ArrayList<LinearConstraint>(){{add(constraint1); add(constraint2);}}, GoalType.MAXIMIZE, false, 1.0E-12);

        assertEquals(0, tableau.getBasicRow(0));
        assertEquals(1, tableau.getBasicRow(3));
        assertEquals(null, tableau.getBasicRow(13));

        tableau.setEntry(1, 3, 1.0);
        tableau.setEntry(1, 13 - 1, 1.0);
        assertEquals(1, tableau.getBasicRow(1));
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, -1.5}, {0.5, -1.5}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, new ArrayList<LinearConstraint>(){{add(constraint1); add(constraint2);}}, GoalType.MAXIMIZE, false, 1.0E-12);

        assertFalse(tableau.isOptimal());

        tableau.setEntry(1, 3, 1.0);
        tableau.setEntry(1, 13 - 1, 1.0);
        tableau.setEntry(2, 12, 1.0);
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, -1.5}, {0.5, -1.5}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, new ArrayList<LinearConstraint>(){{add(constraint1); add(constraint2);}}, GoalType.MAXIMIZE, false, 1.0E-12);

        assertNull(tableau.getSolution());

        tableau.setEntry(1, 3, 1.0);
        tableau.setEntry(1, 13 - 1, 1.0);
        tableau.setEntry(2, 12, 1.0);

        PointValuePair solution = tableau.getSolution();
        assertEquals(new double[]{0.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 1.0}, solution.getPoint(), 1.0E-12);
        assertEquals(3.0, solution.getValue(), 1.0E-12);
    }
}
package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {3.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
        List<String> expectedColumnLabels = Arrays.asList("Z", "x0", "x1", "x-", "s0", "s1", "a0", "a1", "RHS");
        assertEquals(expectedColumnLabels, tableau.columnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {3.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
        int width = tableau.getWidth();
        int height = tableau.getHeight();
        assertEquals(12, width);
        assertEquals(3, height);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                double expectedEntry = 0.0;
                if (i == 0 && j == 0) {
                    expectedEntry = -1.0;
                } else if (i == 0 && (j >= 1 && j <= 3)) {
                    expectedEntry = -f.getCoefficients().getEntry(j - 1);
                } else if (i == 0 && j == 4) {
                    expectedEntry = -5.0;
                } else if (i == 1 && j == 0) {
                    expectedEntry = 1.0;
                } else if (i == 1 && j == 5) {
                    expectedEntry = 3.0;
                } else if (i == 2 && j == 0) {
                    expectedEntry = 1.0;
                } else if (i == 2 && j == 6) {
                    expectedEntry = 1.0;
                } else if (i >= 1 && j == width - 1) {
                    expectedEntry = constraints.get(i - 1).getValue();
                }
                assertEquals(expectedEntry, tableau.getEntry(i, j), 1.0E-12);
            }
        }
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {3.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
        int col = 0;
        Integer expectedRow = 0;
        Integer actualRow = tableau.getBasicRow(col);
        assertEquals(expectedRow, actualRow);

        col = 1;
        expectedRow = null;
        actualRow = tableau.getBasicRow(col);
        assertEquals(expectedRow, actualRow);

        col = 2;
        expectedRow = 1;
        actualRow = tableau.getBasicRow(col);
        assertEquals(expectedRow, actualRow);

        col = 3;
        expectedRow = null;
        actualRow = tableau.getBasicRow(col);
        assertEquals(expectedRow, actualRow);

        col = 4;
        expectedRow = 1;
        actualRow = tableau.getBasicRow(col);
        assertEquals(expectedRow, actualRow);

        col = 5;
        expectedRow = null;
        actualRow = tableau.getBasicRow(col);
        assertEquals(expectedRow, actualRow);

        col = 6;
        expectedRow = 2;
        actualRow = tableau.getBasicRow(col);
        assertEquals(expectedRow, actualRow);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {3.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
        assertFalse(tableau.isOptimal());

        tableau.setEntry(1, 5, 1.0);
        tableau.setEntry(1, 7, -1.0);
        tableau.setEntry(2, 5, 1.0);
        tableau.setEntry(2, 7, -1.0);
        tableau.dropPhase1Objective();
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {3.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();
        assertEquals(0.0, coefficients[0], 1.0E-12);
        assertEquals(-1.0 / 3.0, coefficients[1], 1.0E-12);
        assertEquals(-1.0 / 3.0, coefficients[2], 1.0E-12);
        assertEquals(5.0 / 3.0, coefficients[3], 1.0E-12);
        assertEquals(1.0 / 3.0, coefficients[4], 1.0E-12);
        assertEquals(1.0 / 3.0, coefficients[5], 1.0E-12);
        assertEquals(-1.0 / 3.0, coefficients[6], 1.0E-12);
        assertEquals(-1.0 / 3.0, coefficients[7], 1.0E-12);
        assertEquals(1.0, tableau.f.getValue(coefficients), 1.0E-12);
    }
}
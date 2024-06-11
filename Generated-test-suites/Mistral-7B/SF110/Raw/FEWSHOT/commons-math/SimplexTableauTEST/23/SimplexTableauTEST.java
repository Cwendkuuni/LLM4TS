package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.*;
import java.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;

public class SimplexTableauTest {

    @Test
    public void testNormalizeConstraints() {
        LinearConstraint constraint = new LinearConstraint(new ArrayRealVector(new double[]{-1, 2, 3}), Relationship.LEQ, 5);
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint);

        SimplexTableau tableau = new SimplexTableau(null, originalConstraints, GoalType.MINIMIZE, false, 0.0);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);

        LinearConstraint normalizedConstraint = normalizedConstraints.get(0);
        assertEquals(Relationship.LEQ, normalizedConstraint.getRelationship());
        assertEquals(-1.0, normalizedConstraint.getCoefficients().getEntry(0), 0.0);
        assertEquals(2.0, normalizedConstraint.getCoefficients().getEntry(1), 0.0);
        assertEquals(3.0, normalizedConstraint.getCoefficients().getEntry(2), 0.0);
        assertEquals(5.0, normalizedConstraint.getValue(), 0.0);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), false);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-1, 2}), Relationship.LEQ, 5));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3, -1}), Relationship.GEQ, 2));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 0.0);
        RealMatrix result = tableau.createTableau(false);

        int width = tableau.getWidth();
        int height = tableau.getHeight();

        assertEquals(15, width);
        assertEquals(6, height);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                double expectedEntry = 0.0;
                if (i == 0) {
                    if (j == 0) {
                        expectedEntry = -1.0;
                    } else if (j >= 5 && j <= 8) {
                        expectedEntry = 1.0;
                    } else if (j == 12) {
                        expectedEntry = -5.0;
                    } else if (j == 13) {
                        expectedEntry = 1.0;
                    }
                } else if (i >= 1 && i <= 3 && j == 0) {
                    expectedEntry = tableau.getEntry(0, j);
                } else if (i >= 1 && i <= 3 && j >= 5 && j <= 8) {
                    expectedEntry = -tableau.getEntry(0, j - 5);
                } else if (i == 5 && j == 12) {
                    expectedEntry = 1.0;
                } else if (i == 5 && j >= 13 && j <= 14) {
                    expectedEntry = 1.0;
                }

                assertEquals(expectedEntry, result.getEntry(i, j), 0.0);
            }
        }
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), false);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-1, 2}), Relationship.LEQ, 5));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3, -1}), Relationship.GEQ, 2));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 0.0);

        assertFalse(tableau.isOptimal());

        tableau.dropPhase1Objective();
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}), false);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-1, 2}), Relationship.LEQ, 5));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3, -1}), Relationship.GEQ, 2));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 0.0);

        PointValuePair solution = tableau.getSolution();

        assertEquals(2, solution.getPoint().length);
        assertEquals(-11.0 / 11.0, solution.getPoint()[0], 0.0);
        assertEquals(-1.0 / 11.0, solution.getPoint()[1], 0.0);
        assertEquals(-11.0, solution.getValue(), 0.0);
    }
}
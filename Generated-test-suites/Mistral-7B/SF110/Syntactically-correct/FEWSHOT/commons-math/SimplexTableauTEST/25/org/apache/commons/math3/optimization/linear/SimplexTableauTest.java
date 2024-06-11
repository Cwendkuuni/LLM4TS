package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testNormalizeConstraints() {
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 2.0}), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{2.0, -1.0}), Relationship.GEQ, 2.0);
        LinearConstraint constraint3 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.EQ, 0.0);

        List<LinearConstraint> originalConstraints = Arrays.asList(constraint1, constraint2, constraint3);

        SimplexTableau tableau = new SimplexTableau(new QuadraticObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}, {1.0, 2.0}}), new ArrayRealVector(new double[]{-2.0, 3.0})), originalConstraints, GoalType.MAXIMIZE, false, 1.0E-12);

        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);

        LinearConstraint normalizedConstraint1 = normalizedConstraints.get(0);
        LinearConstraint normalizedConstraint2 = normalizedConstraints.get(1);
        LinearConstraint normalizedConstraint3 = normalizedConstraints.get(2);

        assertEquals(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.GEQ, 3.0), normalizedConstraint1);
        assertEquals(new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 2.0), normalizedConstraint2);
        assertEquals(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.EQ, 0.0), normalizedConstraint3);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {1.0, -3.0}}), new ArrayRealVector(new double[]{4.0, 2.0}));
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 3.0),
                new LinearConstraint(new ArrayRealVector(new double[]{2.0, -1.0}), Relationship.GEQ, 2.0),
                new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.EQ, 0.0)
        );

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        RealMatrix tableauMatrix = tableau.createTableau(true);

        // The tableau matrix should have the following structure:
        // | -1.0  1.0  |
        // | -1.0 -3.0  |
        // |  4.0  2.0  |
        // |  0.0  0.0  |
        // |  3.0  0.0  |
        // |  0.0  0.0  |

        assertEquals(6, tableauMatrix.getRowDimension());
        assertEquals(6, tableauMatrix.getColumnDimension());

        assertEquals(-1.0, tableauMatrix.getEntry(0, 0), 1.0E-12);
        assertEquals(1.0, tableauMatrix.getEntry(0, 1), 1.0E-12);
        assertEquals(-1.0, tableauMatrix.getEntry(1, 0), 1.0E-12);
        assertEquals(-3.0, tableauMatrix.getEntry(1, 1), 1.0E-12);
        assertEquals(4.0, tableauMatrix.getEntry(2, 0), 1.0E-12);
        assertEquals(2.0, tableauMatrix.getEntry(2, 1), 1.0E-12);
        assertEquals(3.0, tableauMatrix.getEntry(3, 3), 1.0E-12);
        assertEquals(0.0, tableauMatrix.getEntry(4, 3), 1.0E-12);
        assertEquals(0.0, tableauMatrix.getEntry(5, 3), 1.0E-12);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {1.0, -3.0}}), new ArrayRealVector(new double[]{4.0, 2.0}));
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 3.0),
                new LinearConstraint(new ArrayRealVector(new double[]{2.0, -1.0}), Relationship.GEQ, 2.0),
                new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.EQ, 0.0)
        );

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        PointValuePair solution = tableau.getSolution();

        RealVector coefficients = solution.getPoint();

        assertEquals(2.0 / 5.0, coefficients.getEntry(0), 1.0E-12);
        assertEquals(-1.0 / 5.0, coefficients.getEntry(1), 1.0E-12);
        assertEquals(-3.0 / 5.0, coefficients.getEntry(2), 1.0E-12);
        assertEquals(1.0 / 5.0, coefficients.getEntry(3), 1.0E-12);

        assertEquals(12.0, objectiveFunction.getValue(coefficients), 1.0E-12);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {1.0, -3.0}}), new ArrayRealVector(new double[]{4.0, 2.0}));
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 3.0),
                new LinearConstraint(new ArrayRealVector(new double[]{2.0, -1.0}), Relationship.GEQ, 2.0),
                new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.EQ, 0.0)
        );

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        assertTrue(tableau.isOptimal());

        tableau.dropPhase1Objective();

        assertFalse(tableau.isOptimal());
    }
}
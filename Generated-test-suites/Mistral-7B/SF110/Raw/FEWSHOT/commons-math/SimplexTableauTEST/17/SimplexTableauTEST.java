package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.Relationship;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RealMatrix;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, 0.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0.5, 1.5}, Relationship.EQ, 2.5);

        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint1);
        originalConstraints.add(constraint2);
        originalConstraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(obj, originalConstraints, GoalType.MAXIMIZE, false, 1e-12);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);

        LinearConstraint normalizedConstraint1 = normalizedConstraints.get(0);
        assertEquals(Relationship.LEQ, normalizedConstraint1.getRelationship());
        assertEquals(-1.0, normalizedConstraint1.getCoefficients()[0], 1e-12);
        assertEquals(-1.0, normalizedConstraint1.getCoefficients()[1], 1e-12);
        assertEquals(2.0, normalizedConstraint1.getValue(), 1e-12);

        LinearConstraint normalizedConstraint2 = normalizedConstraints.get(1);
        assertEquals(Relationship.GEQ, normalizedConstraint2.getRelationship());
        assertEquals(-1.0, normalizedConstraint2.getCoefficients()[0], 1e-12);
        assertEquals(1.0, normalizedConstraint2.getCoefficients()[1], 1e-12);
        assertEquals(1.0, normalizedConstraint2.getValue(), 1e-12);

        LinearConstraint normalizedConstraint3 = normalizedConstraints.get(2);
        assertEquals(Relationship.EQ, normalizedConstraint3.getRelationship());
        assertEquals(0.5, normalizedConstraint3.getCoefficients()[0], 1e-12);
        assertEquals(1.5, normalizedConstraint3.getCoefficients()[1], 1e-12);
        assertEquals(2.5, normalizedConstraint3.getValue(), 1e-12);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0.5, 1.5}, Relationship.EQ, 2.5);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MAXIMIZE, false, 1e-12, 10);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 1.0, 1.0, 1.0, 1.0, 3.0, 2.0, 2.0, 1.0, 1.0, 2.5, 0.0},
                {-1.0, -1.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0},
                {1.0, 1.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0}
        });

        RealMatrix tableauMatrix = tableau.getTableau();
        assertArrayEquals(expectedTableau.getData(), tableauMatrix.getData(), 1e-12);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0.5, 1.5}, Relationship.EQ, 2.5);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MAXIMIZE, false, 1e-12, 10);

        int columnIndex = 3;
        Integer expectedRow = 1;
        Integer basicRow = tableau.getBasicRow(columnIndex);
        assertEquals(expectedRow, basicRow);

        columnIndex = 6;
        expectedRow = 3;
        basicRow = tableau.getBasicRow(columnIndex);
        assertEquals(expectedRow, basicRow);

        columnIndex = 10;
        assertNull(tableau.getBasicRow(columnIndex));
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction obj1 = new LinearObjectiveFunction(new double[]{1.0, 2.0}, 3.0);
        LinearObjectiveFunction obj2 = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, -3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0.5, 1.5}, Relationship.EQ, 2.5);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(obj1, constraints, GoalType.MAXIMIZE, false, 1e-12, 10);
        tableau.dropPhase1Objective();

        SimplexTableau expectedTableau = new SimplexTableau(obj2, constraints, GoalType.MAXIMIZE, false, 1e-12, 10);

        RealMatrix expectedTableauMatrix = expectedTableau.getTableau();
        RealMatrix tableauMatrix = tableau.getTableau();
        assertArrayEquals(expectedTableauMatrix.getData(), tableauMatrix.getData(), 1e-12);
    }

    @Test
    public void testSolution() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0.5, 1.5}, Relationship.EQ, 2.5);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MAXIMIZE, false, 1e-12, 10);
        PointValuePair solution = tableau.getSolution();

        double[] coefficients = solution.getPoint();
        assertEquals(6.0, coefficients[0], 1e-12);
        assertEquals(-1.5, coefficients[1], 1e-12);
        assertEquals(0.5, coefficients[2], 1e-12);
        assertEquals(-2.0, coefficients[3], 1e-12);
        assertEquals(2.0, coefficients[4], 1e-12);
        assertEquals(1.5, coefficients[5], 1e-12);

        assertEquals(6.0, tableau.f.getValue(coefficients), 1e-12);
    }

    @Test
    public void testDivideRow() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0.5, 1.5}, Relationship.EQ, 2.5);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MAXIMIZE, false, 1e-12, 10);

        int dividendRow = 0;
        double divisor = 2.0;
        tableau.divideRow(dividendRow, divisor);

        double[][] expectedData = new double[][]{
                {0.5, 0.5, 0.5, 1.0, 1.0, 3.0 / 2.0, 1.5, 1.5, 1.5, 1.5, 1.25, 1.25},
                {-0.5, -0.5, 0.0, -0.5, 0.0, 0.0, -0.5, -0.5, 0.0, 0.0, 0.0, 0.0},
                {1.0, 1.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0}
        };

        double[][] tableauData = tableau.getData();
        assertArrayEquals(expectedData, tableauData, 1e-12);
    }

    @Test
    public void testSubtractRow() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0.5, 1.5}, Relationship.EQ, 2.5);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MAXIMIZE, false, 1e-12, 10);

        int minuendRow = 0;
        int subtrahendRow = 1;
        double multiple = 2.0;
        tableau.subtractRow(minuendRow, subtrahendRow, multiple);

        double[][] expectedData = new double[][]{
                {-1.0, 1.0, 1.0, 1.0, 1.0, 3.0, 1.5, 1.5, 1.5, 1.5, 0.5, 0.5},
                {-3.0, -3.0, 0.0, -3.0, 0.0, 0.0, -4.5, -4.5, 0.0, 0.0, -1.0, -1.0},
                {1.0, 1.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}
        };

        double[][] tableauData = tableau.getData();
        assertArrayEquals(expectedData, tableauData, 1e-12);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 2.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new double[]{0.5, 1.5}, Relationship.EQ, 2.5);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MAXIMIZE, false, 1e-12, 10);

        assertTrue(tableau.isOptimal());

        tableau.setEntry(1, 10, 1.0);
        assertFalse(tableau.isOptimal());
    }
}
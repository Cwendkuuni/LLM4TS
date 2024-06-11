package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.linear.*;
import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testInitialization() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        assertEquals(3, tableau.getOriginalNumDecisionVariables());
        assertEquals(2, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(10, tableau.getMaxUlps());
        assertEquals(1.0E-6, tableau.getEpsilon(), 1.0E-8);
        assertEquals(false, tableau.isRestrictToNonNegative());

        assertEquals(3, tableau.getWidth());
        assertEquals(5, tableau.getHeight());
        assertNotNull(tableau.getData());
    }

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        List<String> expectedColumnLabels = Arrays.asList("x0", "x1", "x2", "x-", "s0", "s1", "a0", "a1", "RHS");
        assertEquals(expectedColumnLabels, tableau.getColumnLabels());
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, -1.0, 2.0, -1.0, 1.0, 1.0, 1.0, 1.0, 0.0},
                {0.0, -1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0},
                {1.0, 0.0, 0.0, 1.0, 0.0, 0.0, -1.0, -1.0, 4.0},
                {1.0, 0.0, 0.0, 1.0, 0.0, 0.0, -1.0, -1.0, 5.0}
        });

        assertEquals(expectedTableau, tableau.getData());
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        originalConstraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, originalConstraints, GoalType.MAXIMIZE, false, 1.0E-6);

        List<LinearConstraint> expectedNormalizedConstraints = new ArrayList<>();
        expectedNormalizedConstraints.add(new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -2.0}), Relationship.GEQ, 0.0));
        expectedNormalizedConstraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 2.0));

        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);
        assertEquals(expectedNormalizedConstraints, normalizedConstraints);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        int col = 1;
        Integer expectedBasicRow = 2;
        Integer actualBasicRow = tableau.getBasicRow(col);
        assertEquals(expectedBasicRow, actualBasicRow);

        col = 3;
        expectedBasicRow = null;
        actualBasicRow = tableau.getBasicRow(col);
        assertNull(actualBasicRow);

        col = 4;
        expectedBasicRow = 3;
        actualBasicRow = tableau.getBasicRow(col);
        assertEquals(expectedBasicRow, actualBasicRow);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        assertTrue(tableau.isOptimal());

        constraints.get(0).setValue(0.1);
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        PointValuePair solution = tableau.getSolution();
        RealVector coefficients = solution.getPoint();
        assertEquals(3.0, coefficients.getEntry(0), 1.0E-8);
        assertEquals(2.5, coefficients.getEntry(1), 1.0E-8);
        assertEquals(1.5, coefficients.getEntry(2), 1.0E-8);
        assertEquals(4.0, tableau.f.getValue(coefficients), 1.0E-8);
    }

    @Test
    public void testDivideRow() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        int dividendRow = 1;
        double divisor = 2.0;
        tableau.divideRow(dividendRow, divisor);

        double entry = tableau.getEntry(dividendRow, 0);
        assertEquals(0.5, entry, 1.0E-8);

        for (int i = 1; i < tableau.getWidth(); ++i) {
            entry = tableau.getEntry(dividendRow, i);
            assertEquals(entry / divisor, tableau.getEntry(dividendRow, i), 1.0E-8);
        }
    }

    @Test
    public void testSubtractRow() {
        LinearObjectiveFunction f = new LinProbabilisticObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), new RealVector(new double[]{4.0, 5.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        int minuendRow = 1;
        int subtrahendRow = 2;
        double multiple = 2.0;
        tableau.subtractRow(minuendRow, subtrahendRow, multiple);

        for (int i = 0; i < tableau.getWidth(); ++i) {
            double entry = tableau.getEntry(minuendRow, i) - tableau.getEntry(subtrahendRow, i) * multiple;
            assertEquals(entry, tableau.getEntry(minuendRow, i), 1.0E-8);
        }
    }
}
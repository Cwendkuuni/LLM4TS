package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.*;
import java.util.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;

public class SimplexTableauTest {

    private SimplexTableau tableau;

    @Before
    public void setUp() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.LEQ, 5.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 2.0}), Relationship.GEQ, 3.0));
        tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
    }

    @Test
    public void testConstructor() {
        assertEquals(1.0, tableau.getEpsilon(), 1e-6);
        assertEquals(10, tableau.getMaxUlps());
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(2, tableau.getNumArtificialVariables());
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testInitializeColumnLabels() {
        assertEquals(11, tableau.getWidth());
        List<String> expectedColumnLabels = Arrays.asList("Z", "x0", "x1", "x2", "x-", "s0", "s1", "a0", "a1", "a2", "RHS");
        Collections.sort(expectedColumnLabels);
        Collections.sort(tableau.getColumnLabels());
        assertArrayEquals(expectedColumnLabels.toArray(), tableau.getColumnLabels().toArray());
    }

    @Test
    public void testCreateTableau() {
        assertEquals(5, tableau.getHeight());
        assertArrayEquals(new double[]{-1.0, 1.0, 2.0, 1.0, 1.0, 1.0, -1.0, -1.0, -1.0, 5.0, 5.0, 3.0, -1.0, 3.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0}, tableau.getData()[0]);
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[1]);
        assertArrayEquals(new double[]{-1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0}, tableau.getData()[2]);
        assertArrayEquals(new double[]{-1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0}, tableau.getData()[3]);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[4]);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearConstraint originalConstraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.GEQ, 5.0);
        LinearConstraint expectedNormalizedConstraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, -2.0, -3.0}), Relationship.LEQ, -5.0);
        LinearConstraint originalConstraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.LEQ, 3.0);
        LinearConstraint expectedNormalizedConstraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.LEQ, 3.0);

        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(Arrays.asList(originalConstraint1, originalConstraint2));
        assertEquals(expectedNormalizedConstraint1, normalizedConstraints.get(0));
        assertEquals(expectedNormalizedConstraint2, normalizedConstraints.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBasicRowWithInvalidColumn() {
        int invalidColumn = -1;
        assertNull(tableau.getBasicRow(invalidColumn));
    }

    @Test
    public void testDropPhase1Objective() {
        tableau.dropPhase1Objective();
        assertEquals(4, tableau.getHeight());
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testIsOptimal() {
        tableau.setEntry(2, 10, 1.0);
        assertFalse(tableau.isOptimal());
        tableau.setEntry(2, 10, 0.0);
        tableau.dropPhase1Objective();
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        PointValuePair solution = tableau.getSolution();
        assertEquals(3.0, solution.getPoint()[0], 1e-6);
        assertEquals(10.0, solution.getValue(), 1e-6);
    }
}
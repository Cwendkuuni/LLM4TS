package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.io.*;

public class SimplexTableauTest {

    private SimplexTableau simplexTableau;

    @Before
    public void setUp() {
        LinearObjectiveFunction f = new LinearOptimizationProblem("Test problem", new Array2DRowRealMatrix(new double[][]{{2.0, 3.0}, {1.0, 1.0}}), new RealVector(new double[]{4.0, 6.0}), GoalType.MAXIMIZE).getObjectiveFunction();
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, new RealVector(new double[]{0.0, 1.0})));
        simplexTableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
    }

    @Test
    public void testSimplexTableauConstruction() {
        assertEquals(2, simplexTableau.getNumObjectiveFunctions());
        assertEquals(4, simplexTableau.getOriginalNumDecisionVariables());
        assertEquals(1, simplexTableau.getNumSlackVariables());
        assertEquals(1, simplexTableau.getNumArtificialVariables());
        assertEquals(1e-12, simplexTableau.epsilon, 0.0);
        assertEquals(10, simplexTableau.maxUlps);
        assertNotNull(simplexTableau.columnLabels);
        assertEquals(9, simplexTableau.columnLabels.size());
        assertTrue(simplexTableau.columnLabels.containsAll(Arrays.asList("W", "Z", "x0", "x1", "x-", "s0", "a0", "RHS")));
    }

    @Test
    public void testCreateTableau() {
        assertEquals(5, simplexTableau.getHeight());
        assertEquals(9, simplexTableau.getWidth());
        assertTrue(simplexTableau.isQuadraticMatrix());
    }

    @Test
    public void testNormalizeConstraints() {
        List<LinearConstraint> normalizedConstraints = simplexTableau.normalizeConstraints(simplexTableau.constraints);
        assertEquals(normalizedConstraints.size(), simplexTableau.constraints.size());
        for (int i = 0; i < normalizedConstraints.size(); ++i) {
            assertEquals(normalizedConstraints.get(i).getValue(), Math.abs(simplexTableau.constraints.get(i).getValue()));
            assertEquals(normalizedConstraints.get(i).getCoefficients().getDimension(), simplexTableau.constraints.get(i).getCoefficients().getDimension());
            assertEquals(normalizedConstraints.get(i).getRelationship(), simplexTableau.constraints.get(i).getRelationship());
        }
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        assertEquals(simplexTableau.getNumObjectiveFunctions(), 2);
    }

    @Test
    public void testGetConstraintTypeCounts() {
        assertEquals(simplexTableau.getConstraintTypeCounts(Relationship.LEQ), 1);
        assertEquals(simplexTableau.getConstraintTypeCounts(Relationship.GEQ), 0);
        assertEquals(simplexTableau.getConstraintTypeCounts(Relationship.EQ), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBasicRowWithInvalidColumnIndex() {
        simplexTableau.getBasicRow(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBasicRowWithInvalidHeight() {
        simplexTableau.setHeight(simplexTableau.getHeight() + 1);
        simplexTableau.getBasicRow(simplexTableau.getHeight());
    }

    @Test
    public void testDropPhase1Objective() {
        simplexTableau.dropPhase1Objective();
        assertEquals(1, simplexTableau.getNumObjectiveFunctions());
    }

    @Test
    public void testIsOptimal() {
        assertTrue(simplexTableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        PointValuePair solution = simplexTableau.getSolution();
        assertEquals(simplexTableau.getOriginalNumDecisionVariables(), solution.getPoint().length);
        assertEquals(Double.POSITIVE_INFINITY, solution.getValue(), 0.0);
    }

    @Test
    public void testDivideRow() {
        int dividendRow = 0;
        double divisor = 2.0;
        simplexTableau.divideRow(dividendRow, divisor);
        double entry = simplexTableau.getEntry(dividendRow, 0);
        assertTrue(Math.abs(entry - 0.5) < 1e-12);
    }

    @Test
    public void testSubtractRow() {
        int minuendRow = 0;
        int subtrahendRow = 1;
        double multiple = 2.0;
        simplexTableau.subtractRow(minuendRow, subtrahendRow, multiple);
        double entry = simplexTableau.getEntry(minuendRow, 1);
        assertTrue(Math.abs(entry + multiple) < 1e-12);
    }

    @Test
    public void testEquals() {
        SimplexTableau simplexTableau2 = new SimplexTableau(simplexTableau.f, simplexTableau.constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertTrue(simplexTableau.equals(simplexTableau2));
    }

    @Test
    public void testHashCode() {
        SimplexTableau simplexTableau2 = new SimplexTableau(simplexTableau.f, simplexTableau.constraints, GoalType.MAXIMIZE, false, 1e-12);
        int hashCode = simplexTableau.hashCode();
        SimplexTableau simplexTableau3 = new SimplexTableau(simplexTableau.f, simplexTableau.constraints, GoalType.MAXIMIZE, true, 1e-12);
        int hashCode2 = simplexTableau3.hashCode();
        assertTrue(hashCode == simplexTableau2.hashCode());
        assertFalse(hashCode == hashCode2);
    }
}
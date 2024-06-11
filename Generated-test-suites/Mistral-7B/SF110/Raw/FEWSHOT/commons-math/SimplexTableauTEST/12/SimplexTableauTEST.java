package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.linear.*;
import java.util.*;
import java.io.*;

public class SimplexTableauTest {

    @Test
    public void testInitialization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, -1.0}, {-1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(5, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(1.0E-6, tableau.epsilon, 1.0E-8);
        assertEquals(10, tableau.maxUlps);
        assertNotNull(tableau.tableau);
    }

    @Test
    public void testTableauCreation() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, -1.0}, {-1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(6, tableau.getWidth());
        assertEquals(3, tableau.getHeight());
        assertEquals(-1.0, tableau.getEntry(0, 0), 1.0E-8);
        assertEquals(5.0, tableau.getEntry(0, 5), 1.0E-8);
        assertEquals(1.0, tableau.getEntry(0, 1), 1.0E-8);
        assertEquals(2.0, tableau.getEntry(0, 2), 1.0E-8);
        assertEquals(3.0, tableau.getEntry(1, 1), 1.0E-8);
        assertEquals(4.0, tableau.getEntry(1, 2), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(1, 5), 1.0E-8);
        assertEquals(-1.0 * 5.0, tableau.getEntry(1, 6), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 1), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 2), 1.0E-8);
        assertEquals(-1.0, tableau.getEntry(2, 5), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 6), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(0, 4), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(1, 4), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 4), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(0, 3), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(1, 3), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 3), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(0, 0), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(1, 0), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 0), 1.0E-8);
        assertEquals(1.0, tableau.getEntry(0, 6), 1.0E-8);
        assertEquals(-1.0, tableau.getEntry(1, 6), 1.0E-8);
        assertEquals(1.0, tableau.getEntry(2, 6), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(0, 5), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(1, 5), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 5), 1.0E-8);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, -1.0}, {-1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}}), Relationship.GEQ, 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
        assertEquals(2, normalizedConstraints.size());
        LinearConstraint normalizedConstraint1 = normalizedConstraints.get(0);
        LinearConstraint normalizedConstraint2 = normalizedConstraints.get(1);
        assertEquals(Relationship.GEQ, normalizedConstraint1.getRelationship());
        assertEquals(Relationship.LEQ, normalizedConstraint2.getRelationship());
        assertEquals(Arrays.asList(new double[]{-1.0, 1.0, 1.0}, new double[]{1.0, 1.0, -1.0}), Arrays.asList(normalizedConstraint1.getCoefficients().toArray(), normalizedConstraint2.getCoefficients().toArray()));
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(1, tableau.getNumObjectiveFunctions());
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}), 5.0);
        List<LinearConstraint> constraints2 = new ArrayList<>();
        tableau = new SimplexTableau(f2, constraints2, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(2, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testGetColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        List<String> columnLabels = tableau.columnLabels;
        assertEquals(7, columnLabels.size());
        assertEquals("W", columnLabels.get(0));
        assertEquals("Z", columnLabels.get(1));
        assertEquals("x0", columnLabels.get(2));
        assertEquals("x1", columnLabels.get(3));
        assertEquals("x-", columnLabels.get(4));
        assertEquals("s0", columnLabels.get(5));
        assertEquals("s1", columnLabels.get(6));
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertFalse(tableau.isOptimal());
        tableau.dropPhase1Objective();
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, -1.0}, {-1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}}), Relationship.GEQ, 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        PointValuePair solution = tableau.getSolution();
        assertEquals(new double[]{0.0, 2.0, 1.0, 1.0, 0.0, 0.0}, solution.getPoint(), 1.0E-8);
        assertEquals(5.0, solution.getValue(), 1.0E-8);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, -1.0}, {-1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}}), Relationship.GEQ, 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        tableau.dropPhase1Objective();
        assertEquals(4, tableau.getWidth());
        assertEquals(3, tableau.getHeight());
        assertEquals(1.0, tableau.getEntry(0, 1), 1.0E-8);
        assertEquals(1.0, tableau.getEntry(0, 2), 1.0E-8);
        assertEquals(1.0, tableau.getEntry(0, 3), 1.0E-8);
        assertEquals(-1.0, tableau.getEntry(1, 1), 1.0E-8);
        assertEquals(-1.0, tableau.getEntry(1, 2), 1.0E-8);
        assertEquals(-1.0, tableau.getEntry(1, 3), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 1), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 2), 1.0E-8);
        assertEquals(0.0, tableau.getEntry(2, 3), 1.0E-8);
    }
}
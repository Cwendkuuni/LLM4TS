package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.linear.*;

public class SimplexTableauTest {

    @Test
    public void testInitialization() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, -1.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(4, tableau.getOriginalNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(11, tableau.getWidth());
        assertEquals(6, tableau.getHeight());
        assertEquals("W", tableau.columnLabels.get(0));
        assertEquals("Z", tableau.columnLabels.get(1));
        assertEquals("x0", tableau.columnLabels.get(2));
        assertEquals("x1", tableau.columnLabels.get(3));
        assertEquals("s0", tableau.columnLabels.get(4));
        assertEquals("s1", tableau.columnLabels.get(5));
        assertEquals("a0", tableau.columnLabels.get(6));
        assertEquals("RHS", tableau.columnLabels.get(7));
        assertTrue(Arrays.equals(new double[]{1.0, 2.0, 5.0}, tableau.getData()[0]));
        assertTrue(Arrays.equals(new double[]{-1.0, 1.0, -1.0}, tableau.getData()[1]));
        assertTrue(Arrays.equals(new double[]{2.0, -1.0, 2.0}, tableau.getData()[2]));
        assertTrue(Arrays.equals(new double[]{-1.0E-6, -1.0E-6, -1.0E-6}, tableau.getData()[3]));
        assertTrue(Arrays.equals(new double[]{1.0, 0.0, 0.0}, tableau.getData()[4]));
        assertTrue(Arrays.equals(new double[]{0.0, 1.0, 0.0}, tableau.getData()[5]));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, 1.0}, tableau.getData()[6]));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, 0.0}, tableau.getData()[7]));
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, -1.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(0, tableau.getBasicRow(0));
        assertEquals(2, tableau.getBasicRow(2));
        assertEquals(null, tableau.getBasicRow(1));
        assertEquals(null, tableau.getBasicRow(3));
        assertEquals(null, tableau.getBasicRow(5));
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, -1.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertFalse(tableau.isOptimal());
        tableau.setEntry(2, 5, 1.0);
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, -1.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        tableau.setEntry(2, 5, 1.0);
        PointValuePair solution = tableau.getSolution();
        assertEquals(new double[]{0.0, 1.0}, solution.getPoint());
        assertEquals(5.0, solution.getValue(), 1.0E-6);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, -1.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        tableau.dropPhase1Objective();
        assertEquals(0, tableau.getNumObjectiveFunctions());
        assertEquals("x0", tableau.columnLabels.get(0));
        assertEquals("x1", tableau.columnLabels.get(1));
        assertEquals("s0", tableau.columnLabels.get(2));
        assertEquals("s1", tableau.columnLabels.get(3));
        assertEquals("RHS", tableau.columnLabels.get(4));
        assertTrue(Arrays.equals(new double[]{1.0, 2.0}, tableau.getData()[0]));
        assertTrue(Arrays.equals(new double[]{-1.0, 1.0}, tableau.getData()[1]));
        assertTrue(Arrays.equals(new double[]{2.0, -1.0}, tableau.getData()[2]));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0}, tableau.getData()[3]));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0}, tableau.getData()[4]));
    }
}
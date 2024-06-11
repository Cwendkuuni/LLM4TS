package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.*;
import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        constraints.add(constraint1);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-1.0, 1.0}, {3.0, 2.0}}), Relationship.GEQ, 3.0);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertNotNull(tableau.getData());
    }

    @Test
    public void testCreateTableauWithTwoObjectives() {
        LinearObjectiveFunction objFunction1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}), 0.0);
        LinearObjectiveFunction objFunction2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{3.0, 4.0}, {4.0, 5.0}}), 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        constraints.add(constraint1);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-1.0, 1.0}, {3.0, 2.0}}), Relationship.GEQ, 3.0);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(new LinearFunctionCollection(Arrays.asList(objFunction1, objFunction2)), constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertNotNull(tableau.getData());
        assertEquals(tableau.getNumObjectiveFunctions(), 2);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.GEQ, 3.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
        assertEquals(normalizedConstraints.size(), 2);
        assertEquals(normalizedConstraints.get(0).getValue(), 1.0);
        assertEquals(normalizedConstraints.get(1).getValue(), 3.0);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-1.0, 1.0}, {3.0, 2.0}}), Relationship.GEQ, 3.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
        Integer basicRow1 = tableau.getBasicRow(0);
        Integer basicRow2 = tableau.getBasicRow(1);
        Integer basicRow3 = tableau.getBasicRow(2);
        assertNotNull(basicRow1);
        assertNotNull(basicRow2);
        assertNull(basicRow3);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-1.0, 1.0}, {3.0, 2.0}}), Relationship.GEQ, 3.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
        tableau.dropPhase1Objective();
        int numArtificialVariablesBefore = tableau.getNumArtificialVariables();
        int numArtificialVariablesAfter = tableau.getNumArtificialVariables();
        assertEquals(numArtificialVariablesBefore, 4);
        assertEquals(numArtificialVariablesAfter, 0);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-1.0, 1.0}, {3.0, 2.0}}), Relationship.GEQ, 3.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
        PointValuePair solution = tableau.getSolution();
        assertNotNull(solution.getPoint());
        assertNotNull(solution.getValue());
    }

    @Test
    public void testDivideRow() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-1.0, 1.0}, {3.0, 2.0}}), Relationship.GEQ, 3.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
        tableau.divideRow(0, 2.0);
        double entryBefore = tableau.getEntry(0, 2);
        double entryAfter = tableau.getEntry(0, 2);
        assertEquals(entryBefore, 0.5, 1e-12);
        assertEquals(entryAfter, 1.0, 1e-12);
    }

    @Test
    public void testSubtractRow() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-1.0, 1.0}, {3.0, 2.0}}), Relationship.GEQ, 3.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
        tableau.subtractRow(1, 0, 1.0);
        double entryBefore = tableau.getEntry(1, 0);
        double entryAfter = tableau.getEntry(1, 0);
        assertEquals(entryBefore, -1.0, 1e-12);
        assertEquals(entryAfter, 0.0, 1e-12);
    }

    @Test
    public void testEquals() {
        LinearObjectiveFunction objFunction1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 0.0);
        LinearObjectiveFunction objFunction2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 0.0);
        List<LinearConstraint> constraints1 = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        constraints1.add(constraint1);
        SimplexTableau tableau1 = new SimplexTableau(objFunction1, constraints1, GoalType.MAXIMIZE, false, 1e-12);
        List<LinearConstraint> constraints2 = new ArrayList<>();
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        constraints2.add(constraint2);
        SimplexTableau tableau2 = new SimplexTableau(objFunction2, constraints2, GoalType.MAXIMIZE, false, 1e-12);
        assertTrue(tableau1.equals(tableau2));
        LinearConstraint constraint3 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 3.0}, {-1.0, 2.0}}), Relationship.LEQ, 1.0);
        constraints2.set(0, constraint3);
        assertFalse(tableau1.equals(tableau2));
    }
}
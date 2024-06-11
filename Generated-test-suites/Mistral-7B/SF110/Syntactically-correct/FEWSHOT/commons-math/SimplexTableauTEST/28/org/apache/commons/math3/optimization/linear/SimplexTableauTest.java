package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import java.io.*;

public class SimplexTableauTest {

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 2.0, 3.0, 5.0, 5.0, 0.0, 2.0},
                {1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 2.0, 3.0, 5.0, 5.0, 0.0, 3.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, -1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0}});

        RealMatrix tableauMatrix = tableau.getTableau();
        assertEquals(expectedTableau, tableauMatrix);
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        int numObjectiveFunctions = tableau.getNumObjectiveFunctions();
        assertEquals(2, numObjectiveFunctions);
    }

    @Test
    public void testGetConstraintTypeCounts() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}), Relationship.GEQ, 3.0);
        LinearConstraint constraint3 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}, {-1.0, 1.0}}), Relationship.EQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2, constraint3), GoalType.MINIMIZE, false, 1e-12);

        int leqCount = tableau.getConstraintTypeCounts(Relationship.LEQ);
        int geqCount = tableau.getConstraintTypeCounts(Relationship.GEQ);
        int eqCount = tableau.getConstraintTypeCounts(Relationship.EQ);

        assertEquals(2, leqCount);
        assertEquals(1, geqCount);
        assertEquals(1, eqCount);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        int col = 0;
        Integer basicRow = tableau.getBasicRow(col);
        assertEquals(0, basicRow);

        col = 1;
        basicRow = tableau.getBasicRow(col);
        assertEquals(1, basicRow);

        col = 2;
        basicRow = tableau.getBasicRow(col);
        assertEquals(null, basicRow);

        col = 3;
        basicRow = tableau.getBasicRow(col);
        assertEquals(null, basicRow);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        assertTrue(tableau.isOptimal());

        LinearConstraint constraint3 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, 1.0}}), Relationship.EQ, 1.0);
        tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2, constraint3), GoalType.MINIMIZE, false, 1e-12);

        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, -1.0}}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        PointValuePair solution = tableau.getSolution();
        assertEquals(new double[]{0.0, 2.0}, solution.getPoint(), 1e-12);
        assertEquals(-1.0, solution.getValue(), 1e-12);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 10.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 0.0}, {1.0, -1.0, 1.0}, {0.0, 0.0, 1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, {0.0, 0.0, 1.0}}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        tableau.dropPhase1Objective();

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{0.0, 1.0, 0.0, 2.0, 5.0, 3.0, 4.0, 6.0},
                {1.0, -1.0, 1.0, 0.0, 5.0, 2.0, 4.0, 6.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, -1.0, -3.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}});

        RealMatrix tableauMatrix = tableau.getTableau();
        assertEquals(expectedTableau, tableauMatrix);
    }
}
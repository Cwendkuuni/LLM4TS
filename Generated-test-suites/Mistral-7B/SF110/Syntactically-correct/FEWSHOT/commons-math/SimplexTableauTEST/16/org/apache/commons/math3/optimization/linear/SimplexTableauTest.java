package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        List<String> expectedColumnLabels = Arrays.asList("x1", "x2", "x-", "s0", "s1", "a0", "a1", "RHS");
        List<String> columnLabels = tableau.getColumnLabels();

        assertEquals(expectedColumnLabels, columnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 3.0},
                {1.0, -1.0, 1.0, -1.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0}});

        RealMatrix tableauMatrix = tableau.getData();

        assertEquals(expectedTableau, tableauMatrix);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        Integer expectedBasicRow = 1;
        Integer actualBasicRow = tableau.getBasicRow(2);

        assertEquals(expectedBasicRow, actualBasicRow);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0, 1.0}, {-1.0, 1.0, -1.0}, {1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        tableau.dropPhase1Objective();

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{4.0, 5.0, 6.0, 2.0, 3.0, 0.0, 0.0, 0.0},
                {1.0, 1.0, 1.0, -1.0, -2.0, 1.0, 1.0, -1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0}});

        RealMatrix tableauMatrix = tableau.getData();

        assertEquals(expectedTableau, tableauMatrix);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        PointValuePair solution = tableau.getSolution();

        double[] expectedSolutionCoefficients = {1.0 / 3.0, 2.0 / 3.0};
        double expectedSolutionValue = 12.0 / 3.0;

        assertArrayEquals(expectedSolutionCoefficients, solution.getPoint(), 1.0E-6);
        assertEquals(expectedSolutionValue, solution.getValue(), 1.0E-6);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        assertTrue(tableau.isOptimal());

        constraints.set(0, new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 1.0));
        tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        assertFalse(tableau.isOptimal());
    }
}
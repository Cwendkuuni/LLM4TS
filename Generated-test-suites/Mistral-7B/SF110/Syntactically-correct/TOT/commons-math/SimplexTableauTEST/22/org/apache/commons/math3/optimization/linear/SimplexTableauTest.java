package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}}), Relationship.EQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        assertNotNull(tableau);
        assertEquals(4, tableau.getWidth());
        assertEquals(3, tableau.getHeight());
        assertEquals(2, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauConstructionWithInvalidGoalType() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}}), Relationship.EQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.INVALID, false, 1e-6, 10);
    }

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}}), Relationship.EQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        List<String> expectedColumnLabels = Arrays.asList("W", "Z", "x0", "x1", "x-");
        List<String> actualColumnLabels = tableau.columnLabels;
        assertEquals(expectedColumnLabels, actualColumnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}}), Relationship.EQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0, 1.0, -5.0, 1.0, 1.0, 1.0, 3.0, 3.0, 1.0, 0.0, 0.0, 3.0},
                {1.0, -1.0, -1.0, 1.0, -5.0, 0.0, -1.0, -1.0, 0.0, 0.0, 1.0, -1.0, 1.0, 0.0},
                {1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}});
        RealMatrix actualTableau = tableau.tableau;
        assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}}), Relationship.EQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0);
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint1);
        originalConstraints.add(constraint2);
        List<LinearConstraint> normalizedConstraints = new SimplexTableau(f, originalConstraints, GoalType.MINIMIZE, false, 1e-6, 10).constraints;
        LinearConstraint expectedConstraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -2.0}}), Relationship.LEQ, 3.0);
        LinearConstraint expectedConstraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.GEQ, 0.0);
        assertEquals(2, normalizedConstraints.size());
        assertTrue(normalizedConstraints.contains(expectedConstraint1));
        assertTrue(normalizedConstraints.contains(expectedConstraint2));
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}}), Relationship.EQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        Integer expectedBasicRow = 2;
        Integer actualBasicRow = tableau.getBasicRow(2);
        assertEquals(expectedBasicRow, actualBasicRow);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}}), Relationship.EQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, -1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        tableau.dropPhase1Objective();
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{1.0, -1.0, 1.0, 1.0, 3.0, 3.0, 1.0, 1.0},
                {1.0, 1.0, -1.0, -1.0, 0.0, 0.0, -1.0, -1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}});
        RealMatrix actualTableau = tableau.tableau;
        assertEquals(expectedTableau, actualTableau);
    }
}
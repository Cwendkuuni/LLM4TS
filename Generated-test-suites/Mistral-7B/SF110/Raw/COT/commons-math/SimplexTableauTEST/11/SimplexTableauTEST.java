package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, -1.0}}), 2.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(3, tableau.getHeight());
        assertEquals(5, tableau.getWidth());
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, -1.0}}), 2.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0 / 3.0, 2.0 / 3.0, 1.0, 1.0, -1.0 / 3.0, 2.0 / 3.0, 1.0, 1.0, -2.0 / 3.0, 3.0 / 3.0, -1.0, 0.0, 2.0},
                {-1.0, 1.0 / 3.0, 2.0 / 3.0, 1.0, 1.0, -1.0 / 3.0, 2.0 / 3.0, 1.0, 0.0, -1.0, 1.0, 0.0, 1.0, 2.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0},
                {2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}});
        RealMatrix actualTableau = tableau.getTableau();
        MatrixUtils.assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, -1.0}}), 2.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
        assertEquals(1, normalizedConstraints.size());
        LinearConstraint normalizedConstraint = normalizedConstraints.get(0);
        assertEquals(Relationship.GEQ, normalizedConstraint.getRelationship());
        assertEquals(-2.0, normalizedConstraint.getValue(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauConstructorWithInvalidConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, -1.0}}), 2.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, -2.0));
        new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, -1.0}}), 2.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        tableau.dropPhase1Objective();
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0, 3.0}, {-1.0, 1.0, -1.0}}), 2.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        PointValuePair solution = tableau.getSolution();
        assertEquals(new double[]{3.0 / 3.0, -1.0 / 3.0, 1.0}, solution.getPoint(), 0.01);
        assertEquals(7.0 / 3.0, solution.getValue(), 0.01);
    }
}
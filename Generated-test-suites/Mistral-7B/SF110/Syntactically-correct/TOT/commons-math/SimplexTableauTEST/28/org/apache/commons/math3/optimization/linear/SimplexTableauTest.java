package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {2.0, -1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), Relationship.GEQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-12);

        // Test tableau properties
        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());
        Assert.assertEquals(12, tableau.getWidth());
        Assert.assertEquals(3, tableau.getHeight());
        Assert.assertEquals(1e-12, tableau.epsilon, 0);
        Assert.assertEquals(10, tableau.maxUlps);

        // Test method calls
        tableau.initializeColumnLabels();
        tableau.createTableau(true);
        tableau.normalizeConstraints(constraints);
        tableau.dropPhase1Objective();
        Assert.assertTrue(tableau.isOptimal());
    }

    @Test
    public void testSimplexTableauConstructionWithNonNegativeConstraint() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {2.0, -1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, -1.0}, {2.0, -1.0}}), Relationship.GEQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, true, 1e-12);

        // Test tableau properties
        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());
        Assert.assertEquals(12, tableau.getWidth());
        Assert.assertEquals(3, tableau.getHeight());
        Assert.assertEquals(1e-12, tableau.epsilon, 0);
        Assert.assertEquals(10, tableau.maxUlps);

        // Test method calls
        tableau.initializeColumnLabels();
        tableau.createTableau(true);
        tableau.normalizeConstraints(constraints);
        tableau.dropPhase1Objective();
        Assert.assertTrue(tableau.isOptimal());
    }

    @Test
    public void testSimplexTableauInvalidInput() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{}), new ArrayRealVector(new double[]{}));
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {2.0, -1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), Relationship.GEQ, 0.0)
        );
        SimplexTableau tableau = null;
        try {
            tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-12);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test tableau properties
        Assert.assertNull(tableau);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {2.0, -1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), Relationship.GEQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-12);
        tableau.dropPhase1Objective();

        // Test getting solution
        PointValuePair solution = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{0.0, 2.5, 0.0, 0.0, 0.0}, solution.getPoint(), 0);
        Assert.assertEquals(12.5, solution.getValue(), 0);
    }
}
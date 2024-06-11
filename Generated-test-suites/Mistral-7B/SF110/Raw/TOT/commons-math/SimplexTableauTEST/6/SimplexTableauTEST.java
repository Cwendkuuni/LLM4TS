package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 2.0}}), Relationship.LEQ, 4.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {-3.0, 2.0}}), Relationship.GEQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1.0E-8);
        // Typical use case: Check if the tableau is created correctly
        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(4, tableau.getOriginalNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());
        Assert.assertEquals(1.0E-8, tableau.getEpsilon(), 1.0E-12);
        Assert.assertEquals(10, tableau.getMaxUlps());
        // Edge case: Empty list of constraints
        List<LinearConstraint> emptyConstraints = Collections.emptyList();
        SimplexTableau emptyTableau = new SimplexTableau(f, emptyConstraints, GoalType.MAXIMIZE, true, 1.0E-8);
        Assert.assertEquals(2, emptyTableau.getNumObjectiveFunctions());
        Assert.assertEquals(5, emptyTableau.getOriginalNumDecisionVariables());
        Assert.assertEquals(0, emptyTableau.getNumSlackVariables());
        Assert.assertEquals(0, emptyTableau.getNumArtificialVariables());
        Assert.assertEquals(1.0E-8, emptyTableau.getEpsilon(), 1.0E-12);
        Assert.assertEquals(10, emptyTableau.getMaxUlps());
        // Error scenario: Invalid goal type
        Assert.assertThrows(IllegalArgumentException.class, () -> new SimplexTableau(f, constraints, null, false, 1.0E-8));
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 2.0}}), Relationship.LEQ, 4.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {-3.0, 2.0}}), Relationship.GEQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1.0E-8);
        // Typical use case: Check if the tableau is created correctly
        Assert.assertNotNull(tableau.getData());
        Assert.assertEquals(7, tableau.getData().length);
        Assert.assertEquals(7, tableau.getData()[0].length);
        // Edge case: Verify no objective functions
        List<LinearConstraint> emptyConstraints2 = Collections.emptyList();
        SimplexTableau emptyTableau2 = new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0}}), 5.0), emptyConstraints2, GoalType.MAXIMIZE, true, 1.0E-8);
        Assert.assertNotNull(emptyTableau2.getData());
        Assert.assertEquals(5, emptyTableau2.getData().length);
        Assert.assertEquals(6, emptyTableau2.getData()[0].length);
        // Error scenario: Invalid goal type
        Assert.assertThrows(IllegalArgumentException.class, () -> new SimplexTableau(f, constraints, null, false, 1.0E-8));
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 2.0}}), Relationship.LEQ, 4.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {-3.0, 2.0}}), Relationship.GEQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1.0E-8);
        PointValuePair solution = tableau.getSolution();
        // Typical use case: Check if the solution is obtained correctly
        Assert.assertArrayEquals(new double[]{0.0, 2.0}, solution.getPoint(), 1.0E-8);
        Assert.assertEquals(3.0, solution.getValue(), 1.0E-8);
        // Edge case: Check if no solution is obtained
        List<LinearConstraint> emptyConstraints = Collections.emptyList();
        SimplexTableau emptyTableau = new SimplexTableau(f, emptyConstraints, GoalType.MAXIMIZE, true, 1.0E-8);
        Assert.assertNull(emptyTableau.getSolution());
        // Error scenario: Invalid goal type
        Assert.assertThrows(IllegalArgumentException.class, () -> new SimplexTableau(f, constraints, null, false, 1.0E-8).getSolution());
    }
}
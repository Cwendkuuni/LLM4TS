package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0, 0.0}, {-1.0, 1.0, 0.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);

        // Check tableau dimensions and content
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 5.0, 1.0, -1.0, -1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0},
                {3.0, 4.0, 0.0, -1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0},
                {0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {1.0, -1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, 0.0, 0.0},
                {-1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}});
        Assert.assertArrayEquals("Unexpected tableau", expectedTableau.getData(), tableau.getData(), 1e-12);

        // Check column labels
        List<String> expectedColumnLabels = Arrays.asList("f", "x0", "x1", "s0", "s1", "a0", "a1", "a2", "RHS");
        Assert.assertEquals("Unexpected column labels", expectedColumnLabels, tableau.getColumnLabels());

        // Check number of decision variables, slack variables, and artificial variables
        Assert.assertEquals("Unexpected number of decision variables", 2, tableau.getOriginalNumDecisionVariables());
        Assert.assertEquals("Unexpected number of slack variables", 2, tableau.getNumSlackVariables());
        Assert.assertEquals("Unexpected number of artificial variables", 3, tableau.getNumArtificialVariables());
    }

    @Test
    public void testSimplexTableauOptimality() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0, 0.0}, {-1.0, 1.0, 0.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        tableau.dropPhase1Objective();

        // Check optimality
        Assert.assertTrue("Tableau is not optimal", tableau.isOptimal());

        // Check solution
        PointValuePair solution = tableau.getSolution();
        double[] expectedSolution = new double[]{3.0 / 2.0, 1.5};
        Assert.assertArrayEquals("Unexpected solution", expectedSolution, solution.getPoint(), 1e-12);
        Assert.assertEquals("Unexpected objective function value", 7.0, solution.getValue(), 1e-12);
    }

    @Test
    public void testSimplexTableauError() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0, 0.0}, {-1.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}), Relationship.LEQ, 0.0));

        try {
            new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
            Assert.fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unexpected exception message", "There are not enough variables to represent all the constraints", e.getMessage());
        }
    }
}
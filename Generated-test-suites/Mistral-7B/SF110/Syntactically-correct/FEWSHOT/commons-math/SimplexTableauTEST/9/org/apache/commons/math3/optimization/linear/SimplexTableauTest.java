package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        SimplexTableau tableau = new SimplexTableau(f, Collections.emptyList(), GoalType.MAXIMIZE, false, 0.001);
        List<String> expectedColumnLabels = Arrays.asList("Z", "x0", "x1", "x2", "x-", "s0", "s1", "a0", "a1", "RHS");
        List<String> actualColumnLabels = tableau.columnLabels;
        assertEquals(expectedColumnLabels, actualColumnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 1.0}), Relationship.LEQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 1.0}), Relationship.GEQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.EQ, 1.0)),
                GoalType.MAXIMIZE, false, 10);

        int width = tableau.getWidth();
        int height = tableau.getHeight();
        double[][] data = tableau.getData();

        // Check the dimensions of the tableau
        assertEquals(height, 4);
        assertEquals(width, 11);

        // Check the entries of the tableau for the objective function
        assertEquals(data[0][0], -1.0);
        assertEquals(data[0][1], 1.0);
        assertEquals(data[0][2], 1.0);
        assertEquals(data[0][3], 1.0);
        assertEquals(data[0][10], 3.0);

        // Check the entries of the tableau for the constraints
        assertEquals(data[1][0], -1.0);
        assertEquals(data[1][1], 2.0);
        assertEquals(data[1][2], -1.0);
        assertEquals(data[1][4], 1.0);
        assertEquals(data[1][7], 0.0);

        assertEquals(data[2][0], -1.0);
        assertEquals(data[2][1], -1.0);
        assertEquals(data[2][2], 1.0);
        assertEquals(data[2][5], 1.0);
        assertEquals(data[2][8], 0.0);

        assertEquals(data[3][0], 0.0);
        assertEquals(data[3][3], 1.0);
        assertEquals(data[3][9], 1.0);
        assertEquals(data[3][10], 1.0);
        assertEquals(data[3][10], 1.0);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 1.0}), Relationship.LEQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 1.0}), Relationship.GEQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.EQ, 1.0)),
                GoalType.MAXIMIZE, false, 10);

        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();

        // Check the coefficients
        assertEquals(coefficients.length, 3);
        assertEquals(coefficients[0], 0.0, 1e-9);
        assertEquals(coefficients[1], 2.0 / 3.0, 1e-9);
        assertEquals(coefficients[2], -1.0 / 3.0, 1e-9);

        // Check the objective function value
        assertEquals(solution.getValue(), 7.0 / 3.0, 1e-9);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 1.0}), Relationship.LEQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 1.0}), Relationship.GEQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.EQ, 1.0)),
                GoalType.MAXIMIZE, false, 10);

        tableau.dropPhase1Objective();

        int width = tableau.getWidth();
        int height = tableau.getHeight();
        double[][] data = tableau.getData();

        // Check that the number of columns has decreased by 1
        assertEquals(width, 10);

        // Check that the first row has been removed
        assertEquals(data[0][0], 0.0);
        for (int i = 0; i < width; i++) {
            assertEquals(data[0][i], 0.0);
        }

        // Check that the number of artificial variables has decreased by 1
        assertEquals(tableau.getNumArtificialVariables(), 0);
    }
}
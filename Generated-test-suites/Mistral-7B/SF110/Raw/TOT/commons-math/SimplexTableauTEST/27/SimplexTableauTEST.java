package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testEmptyConstructor() {
        try {
            new SimplexTableau(null, Collections.emptyList(), GoalType.MINIMIZE, false, 0.0);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new SimplexTableau(null, Collections.emptyList(), GoalType.MINIMIZE, false, 0.0, 0);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOneObjectiveAndOneConstraint() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
        LinearConstraint c = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), Relationship.LEQ, 0.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(c), GoalType.MINIMIZE, false, 1e-6);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(2, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());

        assertTrue(Arrays.asList(tableau.columnLabels).contains("x0"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("x1"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("s0"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("a0"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("Z"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("RHS"));

        // Check tableau
        RealMatrix tableauMatrix = tableau.getData();
        assertEquals(7, tableauMatrix.getRowDimension());
        assertEquals(5, tableauMatrix.getColumnDimension());
        assertTrue(Arrays.equals(new double[]{-1.0, 1.0, 1.0, 1.0, -3.0, 5.0, 2.0, 6.0, 0.0, 1.0, 1.0, 2.0, 1.0, 3.0, 1.0, 1.0, -1.0, -1.0, -1.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(0).toArray()));
        assertTrue(Arrays.equals(new double[]{1.0, -1.0, 0.0, 1.0, -2.0, 0.0, -1.0, 0.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0}, tableauMatrix.getRowVector(1).toArray()));
        assertTrue(Arrays.equals(new double[]{-1.0, -1.0, 0.0, -2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(2).toArray()));
        assertTrue(Arrays.equals(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(3).toArray()));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}, tableauMatrix.getRowVector(4).toArray()));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(5).toArray()));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(6).toArray()));
        assertTrue(Arrays.equals(new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(7).toArray()));

        // Check solution
        PointValuePair solution = tableau.getSolution();
        assertTrue(Arrays.equals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint()));
        assertEquals(-1.0 / 3.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testTwoObjectivesAndTwoConstraints() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new ArrayRealVector(new double[]{5.0, 6.0}));
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {4.0, 3.0}}), new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint c1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint c2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(f1, Arrays.asList(c1, c2), GoalType.MAXIMIZE, false, 1e-6);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(2, tableau.getNumArtificialVariables());

        assertTrue(Arrays.asList(tableau.columnLabels).contains("x0"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("x1"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("x2"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("s0"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("s1"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("a0"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("a1"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("W"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("Z"));
        assertTrue(Arrays.asList(tableau.columnLabels).contains("RHS"));

        // Check tableau
        RealMatrix tableauMatrix = tableau.getData();
        assertEquals(9, tableauMatrix.getRowDimension());
        assertEquals(6, tableauMatrix.getColumnDimension());
        assertTrue(Arrays.equals(new double[]{-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -3.0, 5.0, 2.0, 4.0, 1.0, 2.0, 1.0, 3.0, 1.0, 2.0, 3.0, 4.0, 1.0, 1.0, 1.0, 1.0, 1.0}, tableauMatrix.getRowVector(0).toArray()));
        assertTrue(Arrays.equals(new double[]{1.0, -1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -2.0, 5.0, 2.0, 4.0, 1.0, 2.0, 1.0, 3.0, 1.0, 2.0, 3.0, 4.0, 1.0, 1.0, 1.0, 1.0}, tableauMatrix.getRowVector(1).toArray()));
        assertTrue(Arrays.equals(new double[]{1.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 5.0, 2.0, 4.0, 1.0, 2.0, 1.0, 3.0, 1.0, 2.0, 3.0, 4.0, 1.0, 1.0, 1.0, 1.0}, tableauMatrix.getRowVector(2).toArray()));
        assertTrue(Arrays.equals(new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 5.0, 2.0, 4.0, 1.0, 2.0, 1.0, 3.0, 1.0, 2.0, 3.0, 4.0, 1.0, 1.0, 1.0, 1.0}, tableauMatrix.getRowVector(3).toArray()));
        assertTrue(Arrays.equals(new double[]{-1.0, -1.0, 0.0, -2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(4).toArray()));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(5).toArray()));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(6).toArray()));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableauMatrix.getRowVector(7).toArray()));
        assertTrue(Arrays.equals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}, tableauMatrix.getRowVector(8).toArray()));

        // Check solution
        PointValuePair solution = tableau.getSolution();
        assertTrue(Arrays.equals(new double[]{1.0 / 3.0, 1.0 / 3.0}, solution.getPoint()));
        assertEquals(3.0 / 3.0, solution.getValue(), 1e-6);
    }
}
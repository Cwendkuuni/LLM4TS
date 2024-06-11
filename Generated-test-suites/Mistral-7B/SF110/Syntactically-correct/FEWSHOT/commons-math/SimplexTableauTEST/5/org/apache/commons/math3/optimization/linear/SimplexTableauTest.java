package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.*;
import java.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.PointValuePair;

public class SimplexTableauTest {

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 0.01);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0},
                {1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, -1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, -1.0, -1.0, -1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        });
        RealMatrix actualTableau = tableau.getTableau();
        assertEquals(expectedTableau.getRowDimension(), actualTableau.getRowDimension());
        assertEquals(expectedTableau.getColumnDimension(), actualTableau.getColumnDimension());
        assertArrayEquals(expectedTableau.getData(), actualTableau.getData(), 0.001);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 0.01);
        int col = 3;
        int row = tableau.getBasicRow(col);
        int expectedRow = (col == 0) ? 0 : (col == 1) ? 5 : 1;
        assertEquals(expectedRow, row);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, -2.0, -3.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f1, constraints, GoalType.MINIMIZE, false, 0.01);
        tableau.dropPhase1Objective();
        LinearObjectiveFunction expectedF = new LinearObjectiveFunction(new RealVector(new double[]{-2.0, -1.0, -1.0}));
        assertEquals(expectedF, tableau.getObjectiveFunction());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 0.01);
        PointValuePair solution = tableau.getSolution();
        RealVector expectedCoefficients = new ArrayRealVector(new double[]{0.5, 1.5});
        RealVector expectedValues = new ArrayRealVector(new double[]{-3.5});
        assertEquals(expectedCoefficients, solution.getCoefficients());
        assertEquals(expectedValues, solution.getValue());
    }
}
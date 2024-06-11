package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.*;
import java.util.*;
import org.apache.commons.math3.util.*;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 2.0}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);
        List<String> expectedColumnLabels = Arrays.asList(
                "W", "Z", "x0", "x1", "x2", "x-", "s0", "s1", "a0", "a1", "RHS"
        );
        assertEquals(expectedColumnLabels, tableau.columnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 2.0}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        Array2DRowRealMatrix expectedTableau = new Array2DRowRealMatrix(
                new double[][]{
                        {-1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 0.0, 1.0, 0.0, 2.0},
                        {1.0, -1.0, 2.0, 1.0, -1.0, 0.0, 0.0, 1.0, 0.0, -1.0, 2.0},
                        {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
                }
        );

        RealMatrix actualTableau = tableau.tableau;
        assertArrayEquals(expectedTableau.getDataRef(), actualTableau.getDataRef(), 0.0);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 2.0}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        // Basic row for the first decision variable (x0)
        int x0Index = tableau.columnLabels.indexOf("x0");
        int expectedX0BasicRow = 2;
        Integer actualX0BasicRow = tableau.getBasicRow(x0Index);
        assertEquals(expectedX0BasicRow, actualX0BasicRow);

        // Basic row for the slack variable s0
        int s0Index = tableau.columnLabels.indexOf("s0");
        int expectedS0BasicRow = 5;
        Integer actualS0BasicRow = tableau.getBasicRow(s0Index);
        assertEquals(expectedS0BasicRow, actualS0BasicRow);

        // Non-basic row for the objective function Z
        int zIndex = tableau.columnLabels.indexOf("Z");
        int expectedZNonBasicRow = 0;
        Integer actualZNonBasicRow = tableau.getBasicRow(zIndex);
        assertNull(actualZNonBasicRow);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 2.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 3.0, 0.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        tableau.dropPhase1Objective();

        Array2DRowRealMatrix expectedTableau = new Array2DRowRealMatrix(
                new double[][]{
                        {0.0, 1.0, 1.0, 1.0, 3.0, 1.0, 1.0, 0.0, 0.0, 0.0, 2.0},
                        {1.0, -1.0, 2.0, 1.0, -1.0, 0.0, 0.0, 1.0, 0.0, -1.0, 2.0},
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
                }
        );

        RealMatrix actualTableau = tableau.tableau;
        assertArrayEquals(expectedTableau.getDataRef(), actualTableau.getDataRef(), 0.0);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 2.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 3.0, 0.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        // Not optimal because the objective function has negative entries below the main diagonal
        assertFalse(tableau.isOptimal());

        // Make it optimal by adding a slack variable to constraint2
        tableau.dropPhase1Objective();
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 2.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, 3.0, 0.0}), Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new RealVector(new double[]{0.0, 1.0, -1.0}), Relationship.EQ, 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6);

        PointValuePair solution = tableau.getSolution();
        RealVector coefficients = solution.getPoint();
        assertEquals(new double[]{3.0 / 4.0, 1.0 / 2.0, 0.0}, coefficients.toArray(), 1.0E-6);
        assertEquals(5.0 / 4.0, tableau.f.getValue(coefficients), 1.0E-6);
    }
}
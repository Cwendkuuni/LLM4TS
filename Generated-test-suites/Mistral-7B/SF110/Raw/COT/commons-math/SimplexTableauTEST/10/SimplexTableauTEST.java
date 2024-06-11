package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{3.0, 4.0}), Relationship.LEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        assertNotNull(tableau);
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(12, tableau.getWidth());
        assertEquals(3, tableau.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauConstructionWithInvalidGoalType() {
        GoalType goalType = new GoalType("INVALID");
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        try {
            new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateTableau() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{3.0, 4.0}), Relationship.LEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 3.0, 4.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 3.0, 4.0, 5.0, 1.0, 1.0, 0.0, 0.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0}
        });
        RealMatrix tableauMatrix = tableau.tableau;
        assertEquals(expectedTableau.getRowDimension(), tableauMatrix.getRowDimension());
        assertEquals(expectedTableau.getColumnDimension(), tableauMatrix.getColumnDimension());
        assertArrayEquals(expectedTableau.getData(), tableauMatrix.getDataRef(), 1.0E-12);
    }

    @Test
    public void testNormalizeConstraints() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{3.0, 4.0}), Relationship.LEQ, 5.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-3.0, 4.0}), Relationship.GEQ, 5.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
        assertEquals(2, normalizedConstraints.size());
        LinearConstraint expectedConstraint1 = new LinearConstraint(new RealVector(new double[]{-3.0, 4.0}), Relationship.LEQ, -5.0);
        LinearConstraint expectedConstraint2 = new LinearConstraint(new RealVector(new double[]{3.0, 4.0}), Relationship.GEQ, 5.0);
        for (int i = 0; i < normalizedConstraints.size(); ++i) {
            LinearConstraint normalizedConstraint = normalizedConstraints.get(i);
            if (i == 0) {
                assertEquals(expectedConstraint1, normalizedConstraint);
            } else {
                assertEquals(expectedConstraint2, normalizedConstraint);
            }
        }
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        assertEquals(1, tableau.getNumObjectiveFunctions());
        LinearObjectiveFunction f2 = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0, 3.0}));
        SimplexTableau tableau2 = new SimplexTableau(f2, constraints, goalType, false, 1.0E-12);
        assertEquals(2, tableau2.getNumObjectiveFunctions());
    }

    @Test
    public void testDropPhase1Objective() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        tableau.dropPhase1Objective();
        assertEquals(1, tableau.getNumObjectiveFunctions());
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0, 1.0, 3.0, 4.0, 3.0, 4.0, 5.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0}
        });
        RealMatrix tableauMatrix = tableau.tableau;
        assertEquals(expectedTableau.getRowDimension(), tableauMatrix.getRowDimension());
        assertEquals(expectedTableau.getColumnDimension(), tableauMatrix.getColumnDimension());
        assertArrayEquals(expectedTableau.getData(), tableauMatrix.getDataRef(), 1.0E-12);
    }

    @Test
    public void testIsOptimal() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        PointValuePair solution = tableau.getSolution();
        double optimalSolutionValue = solution.getValue();
        tableau.setEntry(0, 0, optimalSolutionValue);
        assertTrue(tableau.isOptimal());
        tableau.setEntry(0, 0, optimalSolutionValue + 1.0E-12);
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        PointValuePair solution = tableau.getSolution();
        assertArrayEquals(new double[]{0.0, 0.0}, solution.getCoefficients(), 1.0E-12);
        assertEquals(1.0, tableau.f.getValue(solution.getCoefficients()), 1.0E-12);
    }

    @Test
    public void testDivideRow() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        int dividendRow = 0;
        double divisor = 2.0;
        tableau.divideRow(dividendRow, divisor);
        assertEquals(divisor, tableau.getEntry(dividendRow, dividendRow), 1.0E-12);
        for (int j = 0; j < tableau.getWidth(); ++j) {
            if (j != dividendRow) {
                assertEquals(tableau.getEntry(dividendRow, j) / divisor, tableau.getEntry(dividendRow, j), 1.0E-12);
            }
        }
    }

    @Test
    public void testSubtractRow() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new QuadrantFunction(), new RealVector(new double[]{1.0, 2.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, false, 1.0E-12);
        int minuendRow = 0;
        int subtrahendRow = 1;
        double multiple = 2.0;
        tableau.subtractRow(minuendRow, subtrahendRow, multiple);
        assertEquals(-multiple, tableau.getEntry(minuendRow, minuendRow), 1.0E-12);
        for (int i = 0; i < tableau.getWidth(); ++i) {
            if (i != minuendRow && i != subtrahendRow) {
                assertEquals(tableau.getEntry(minuendRow, i), tableau.getEntry(minuendRow, i) - tableau.getEntry(subtrahendRow, i) * multiple, 1.0E-12);
            }
        }
    }
}
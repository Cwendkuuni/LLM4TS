package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.linear.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    private SimplexTableau tableau;
    private LinearObjectiveFunction function;
    private LinearConstraint constraint1;
    private LinearConstraint constraint2;
    private LinearConstraint constraint3;

    @Before
    public void setUp() {
        RealVector coefficients1 = new ArrayRealVector(new double[]{1.0, -2.0, 3.0});
        RealVector coefficients2 = new ArrayRealVector(new double[]{-2.0, 1.0, 1.0});
        function = new LinearObjectiveFunction(coefficients1, true);
        constraint1 = new LinearConstraint(coefficients2, Relationship.LEQ, 5.0);
        constraint2 = new LinearConstraint(coefficients2, Relationship.GEQ, 2.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-8;
        int maxUlps = 10;
        tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
    }

    @Test
    public void testCreateTableau() {
        // Check tableau dimensions and initial values
        int height = tableau.getHeight();
        int width = tableau.getWidth();
        assertEquals("Unexpected tableau height", 3, height);
        assertEquals("Unexpected tableau width", 11, width);

        // Check initial objective function values and coefficients
        double[][] data = tableau.getData();
        double objectiveFunctionValue = tableau.f.getValue(new ArrayRealVector(new double[0]));
        assertEquals("Unexpected initial objective function value", Double.NEGATIVE_INFINITY, objectiveFunctionValue);
        assertArrayEquals("Unexpected initial objective function coefficients", new double[]{}, data[0]);

        // Check initial constraint values and coefficients
        for (int i = 1; i < height; ++i) {
            RealVector coefficients = new ArrayRealVector(data[i]);
            LinearConstraint constraint = (i == 1) ? constraint1 : constraint2;
            assertEquals("Unexpected constraint " + i + " coefficients", coefficients.toArray(), constraint.getCoefficients().toArray());
            assertEquals("Unexpected constraint " + i + " value", constraint.getValue(), data[i][width - 1]);
        }
    }

    @Test
    public void testNormalizeConstraints() {
        // Check normalized constraints
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(Arrays.asList(constraint1, constraint2));
        assertEquals("Unexpected number of normalized constraints", 2, normalizedConstraints.size());
        LinearConstraint normalizedConstraint1 = normalizedConstraints.get(0);
        LinearConstraint normalizedConstraint2 = normalizedConstraints.get(1);
        assertEquals("Unexpected normalized constraint 1 coefficients", new ArrayRealVector(new double[]{-2.0, 1.0, 1.0}), normalizedConstraint1.getCoefficients());
        assertEquals("Unexpected normalized constraint 1 relationship", Relationship.GEQ, normalizedConstraint1.getRelationship());
        assertEquals("Unexpected normalized constraint 1 value", 5.0, normalizedConstraint1.getValue());
        assertEquals("Unexpected normalized constraint 2 coefficients", new ArrayRealVector(new double[]{1.0, -2.0, 3.0}), normalizedConstraint2.getCoefficients());
        assertEquals("Unexpected normalized constraint 2 relationship", Relationship.LEQ, normalizedConstraint2.getRelationship());
        assertEquals("Unexpected normalized constraint 2 value", 2.0, normalizedConstraint2.getValue());
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        assertEquals("Unexpected number of objective functions", 1, tableau.getNumObjectiveFunctions());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNumObjectiveFunctionsWithTwoFunctions() {
        LinearObjectiveFunction function2 = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, -2.0, 3.0}), true);
        tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-8, 10);
        tableau.setEntry(1, 10, 1.0); // Set an entry for the second objective function
        tableau.getNumObjectiveFunctions();
    }

    @Test
    public void testDropPhase1Objective() {
        tableau.dropPhase1Objective();
        int height = tableau.getHeight();
        int width = tableau.getWidth();
        assertEquals("Unexpected tableau height after dropping phase 1 objective", 2, height);
        assertEquals("Unexpected tableau width after dropping phase 1 objective", 8, width);
    }

    @Test
    public void testIsOptimal() {
        // Test optimal solution
        assertEquals("Unexpected optimal solution", true, tableau.isOptimal());

        // Test non-optimal solution with negative variable
        tableau.setEntry(1, 2, -1.0);
        tableau.setEntry(1, 11, 1.0);
        assertFalse("Unexpected non-optimal solution", tableau.isOptimal());

        // Test non-optimal solution with non-basic variable
        tableau.setEntry(2, 0, -1.0);
        assertFalse("Unexpected non-optimal solution", tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();
        assertEquals("Unexpected number of solution coefficients", 3, coefficients.length);
        assertEquals("Unexpected solution x1", 1.0 / 3.0, coefficients[0], 1.0E-8);
        assertEquals("Unexpected solution x2", -1.0 / 3.0, coefficients[1], 1.0E-8);
        assertEquals("Unexpected solution x3", 1.0, coefficients[2], 1.0E-8);
        assertEquals("Unexpected solution objective function value", 13.0 / 3.0, solution.getValue(), 1.0E-8);
    }

    @Test
    public void testDivideRow() {
        int dividendRow = 1;
        double divisor = 2.0;
        tableau.divideRow(dividendRow, divisor);
        double entry = tableau.getEntry(dividendRow, 1);
        assertEquals("Unexpected divided entry", 0.5, entry, 1.0E-8);
    }

    @Test
    public void testSubtractRow() {
        int minuendRow = 1;
        int subtrahendRow = 2;
        double multiple = 2.0;
        tableau.subtractRow(minuendRow, subtrahendRow, multiple);
        double entry = tableau.getEntry(minuendRow, 1);
        assertEquals("Unexpected subtracted entry", -3.0, entry, 1.0E-8);
    }
}
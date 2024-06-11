package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    private SimplexTableau tableau;
    private LinearObjectiveFunction objectiveFunction;
    private List<LinearConstraint> constraints;

    @Before
    public void setup() {
        double[] coefficients = new double[]{-2.0, 3.0, -1.5};
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(coefficients), Relationship.LEQ, 5.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(coefficients), Relationship.GEQ, 3.0);
        constraints = Arrays.asList(constraint1, constraint2);
        objectiveFunction = new LinearObjectiveFunction(new ArrayRealVector(coefficients), 1.0);
        tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
    }

    @Test
    public void testConstructor() {
        assertEquals(LinearObjectiveFunction.class, tableau.getF().getClass());
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getOriginalNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(2, tableau.getNumArtificialVariables());
    }

    @Test
    public void testCreateTableau() {
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 3.0, -1.5, 0.0, -2.0, 3.0, -1.5, 5.0, 3.0, 5.0, 0.0, -1.0, 0.0, 1.0, 1.0},
                                                                            {0.0, -1.0, 0.0, 1.0, 3.0, -3.0, 1.5, -2.0, 0.0, -1.0, 1.0, 0.0, -1.0, 0.0, 0.0}});
        RealMatrix actualTableau = tableau.getTableau();
        assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testInitializeColumnLabels() {
        List<String> expectedColumnLabels = Arrays.asList("Z", "W", "x0", "x1", "x2", "s0", "s1", "a0", "a1", "RHS");
        List<String> actualColumnLabels = tableau.getColumnLabels();
        assertEquals(expectedColumnLabels, actualColumnLabels);
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        assertEquals(2, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testGetConstraintTypeCounts() {
        assertEquals(2, tableau.getConstraintTypeCounts(Relationship.LEQ));
        assertEquals(1, tableau.getConstraintTypeCounts(Relationship.GEQ));
        assertEquals(0, tableau.getConstraintTypeCounts(Relationship.EQ));
    }

    @Test
    public void testGetInvertedCoefficientSum() {
        double[] coefficients = new double[]{-2.0, 3.0, -1.5};
        double expectedResult = -6.0;
        double actualResult = SimplexTableau.getInvertedCoefficientSum(new ArrayRealVector(coefficients));
        assertEquals(expectedResult, actualResult, 1e-12);
    }

    @Test
    public void testGetBasicRow() {
        int expectedRow = 0;
        int col = 1;
        Integer actualRow = tableau.getBasicRow(col);
        assertEquals(expectedRow, actualRow);
    }

    @Test
    public void testDropPhase1Objective() {
        tableau.dropPhase1Objective();
        assertEquals(1, tableau.getNumObjectiveFunctions());
    }

    @Test(expected = AssertionError.class)
    public void testIsOptimal_NotOptimal() {
        tableau.setEntry(1, 2, 0.1);
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testIsOptimal_Optimal() {
        tableau.setEntry(1, 2, 0.0);
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        PointValuePair expectedSolution = new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0, 5.0 / 3.0}, 11.0 / 3.0);
        PointValuePair actualSolution = tableau.getSolution();
        assertEquals(expectedSolution.getPoint(), actualSolution.getPoint(), 1e-12);
        assertEquals(expectedSolution.getValue(), actualSolution.getValue(), 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideRow_InvalidRow() {
        int dividendRow = -1;
        tableau.divideRow(dividendRow, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractRow_InvalidRows() {
        int minuendRow = 0;
        int subtrahendRow = -1;
        double multiple = 2.0;
        tableau.subtractRow(minuendRow, subtrahendRow, multiple);
    }
}
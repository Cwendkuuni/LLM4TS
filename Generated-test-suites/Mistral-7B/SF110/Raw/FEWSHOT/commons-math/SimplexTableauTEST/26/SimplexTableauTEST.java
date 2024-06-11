package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new DummyLinearObjectiveFunction(3);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.GEQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);

        List<String> expectedColumnLabels = Arrays.asList(
                "W",
                "Z",
                "x0",
                "x1",
                "x2",
                "s0",
                "s1",
                "a0",
                "a1",
                "RHS"
        );

        List<String> actualColumnLabels = new ArrayList<>(tableau.columnLabels);

        assertEquals(expectedColumnLabels, actualColumnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new DummyLinearObjectiveFunction(2);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        List<LinearConstraint> constraints = Collections.emptyList();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, -1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 0.0, 0.0, f.getCoefficients().getEntry(0)}
        });

        RealMatrix actualTableau = tableau.tableau;

        assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new DummyLinearObjectiveFunction(3);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        List<LinearConstraint> originalConstraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.LEQ, -1.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.GEQ, 1.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, originalConstraints, goalType, restrictToNonNegative, epsilon, maxUlps);

        List<LinearConstraint> expectedNormalizedConstraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.GEQ, 1.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.LEQ, -1.0)
        );

        List<LinearConstraint> actualNormalizedConstraints = new ArrayList<>(tableau.constraints);

        assertEquals(expectedNormalizedConstraints, actualNormalizedConstraints);
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        LinearObjectiveFunction f = new DummyLinearObjectiveFunction(1);
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        List<LinearConstraint> constraints = Collections.emptyList();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, restrictToNonNegative, epsilon, maxUlps);

        int expectedNumObjectiveFunctions = 1;

        int actualNumObjectiveFunctions = tableau.getNumObjectiveFunctions();

        assertEquals(expectedNumObjectiveFunctions, actualNumObjectiveFunctions);
    }

    @Test
    public void testGetConstraintTypeCounts() {
        LinearObjectiveFunction f = new DummyLinearObjectiveFunction(3);
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.GEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.EQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, restrictToNonNegative, epsilon, maxUlps);

        int expectedLEQCount = 2;
        int expectedGEQCount = 1;
        int expectedEQCount = 1;

        int actualLEQCount = tableau.getConstraintTypeCounts(Relationship.LEQ);
        int actualGEQCount = tableau.getConstraintTypeCounts(Relationship.GEQ);
        int actualEQCount = tableau.getConstraintTypeCounts(Relationship.EQ);

        assertEquals(expectedLEQCount, actualLEQCount);
        assertEquals(expectedGEQCount, actualGEQCount);
        assertEquals(expectedEQCount, actualEQCount);
    }

    @Test
    public void testGetInvertedCoefficientSum() {
        RealVector coefficients = new ArrayRealVector(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});

        double expectedInvertedCoefficientSum = -11.0;

        double actualInvertedCoefficientSum = SimplexTableau.getInvertedCoefficientSum(coefficients);

        assertEquals(expectedInvertedCoefficientSum, actualInvertedCoefficientSum, 1.0E-6);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new DummyLinearObjectiveFunction(3);
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.GEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.EQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, restrictToNonNegative, epsilon, maxUlps);

        int expectedColumnIndex = 1;
        int expectedBasicRow = 2;

        double entry = tableau.getEntry(expectedBasicRow, expectedColumnIndex);

        Integer actualBasicRow = tableau.getBasicRow(expectedColumnIndex);

        assertEquals(expectedBasicRow, actualBasicRow);
        assertEquals(entry, 1.0, 1.0E-6);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new DummyLinearObjectiveFunction(3);
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.GEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.EQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, restrictToNonNegative, epsilon, maxUlps);

        tableau.dropPhase1Objective();

        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new DummyLinearObjectiveFunction(3);
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.LEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.GEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}}), Relationship.EQ, 0.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, restrictToNonNegative, epsilon, maxUlps);

        double[] expectedSolution = new double[]{5.0, 3.0, 1.0};
        PointValuePair actualSolution = tableau.getSolution();

        assertArrayEquals(expectedSolution, actualSolution.getPoint(), 1.0E-6);
    }
}

class DummyLinearObjectiveFunction implements LinearObjectiveFunction {
    private final double[] coefficients;

    public DummyLinearObjectiveFunction(int size) {
        this.coefficients = new double[size];
    }

    @Override
    public int getDimension() {
        return coefficients.length;
    }

    @Override
    public RealVector getCoefficients() {
        return new ArrayRealVector(coefficients);
    }

    @Override
    public double value(RealVector x) {
        double sum = 0.0;
        for (int i = 0; i < coefficients.length; ++i) {
            sum += coefficients[i] * x.getEntry(i);
        }
        return sum;
    }
}
package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.*;
import java.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;

public class SimplexTableauTest {

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        assertEquals(11, tableau.getHeight());
        assertEquals(11, tableau.getWidth());
    }

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new DoubleArray(new double[]{1.0, 2.0, 3.0}), new DoubleArray(new double[]{1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        List<String> labels = tableau.columnLabels;
        assertEquals(12, labels.size());
        assertEquals("x0", labels.get(0));
        assertEquals("x1", labels.get(1));
        assertEquals("x2", labels.get(2));
        assertEquals("x-", labels.get(3));
        assertEquals("s0", labels.get(4));
        assertEquals("s1", labels.get(5));
        assertEquals("a0", labels.get(6));
        assertEquals("a1", labels.get(7));
        assertEquals("RHS", labels.get(10));
        assertEquals("f", labels.get(11));
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        SimplexTableau tableau = new SimplexTableau(f, Collections.emptyList(), GoalType.MINIMIZE, true, 1e-6, 10);
        assertEquals(1, tableau.getNumObjectiveFunctions());

        LinearObjectiveFunction f1 = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new DoubleArray(new double[]{1.0, 1.0, 1.0}), new DoubleArray(new double[]{1.0, 1.0, 1.0}));
        SimplexTableau tableau2 = new SimplexTableau(new LinearFunction[] {f1, f2}, Collections.emptyList(), GoalType.MINIMIZE, true, 1e-6, 10);
        assertEquals(2, tableau2.getNumObjectiveFunctions());
    }

    @Test
    public void testGetConstraintTypeCounts() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        assertEquals(2, tableau.getConstraintTypeCounts(Relationship.LEQ));
        assertEquals(2, tableau.getConstraintTypeCounts(Relationship.GEQ));
    }

    @Test
    public void testGetInvertedCoefficientSum() {
        RealVector coefficients = new ArrayRealVector(new double[]{-1.0, 2.0, 3.0, 4.0});
        double sum = SimplexTableau.getInvertedCoefficientSum(coefficients);
        assertEquals(-10.0, sum, 1e-6);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        Integer row = tableau.getBasicRow(0);
        assertEquals(0, row);
        row = tableau.getBasicRow(10);
        assertNull(row);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        tableau.dropPhase1Objective();
        assertEquals(10, tableau.getHeight());
        assertEquals(10, tableau.getWidth());
        List<String> labels = tableau.columnLabels;
        assertEquals(11, labels.size());
        assertFalse(labels.contains("f"));
    }

    @Test
    public void testDivideRow() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        tableau.divideRow(0, 2.0);
        double[][] data = tableau.getData();
        assertEquals(-0.5, data[0][0], 1e-6);
        assertEquals(-1.0, data[0][1], 1e-6);
        assertEquals(-1.5, data[0][2], 1e-6);
    }

    @Test
    public void testSubtractRow() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        tableau.subtractRow(0, 1, 1.0);
        double[][] data = tableau.getData();
        assertEquals(0.0, data[0][0], 1e-6);
        assertEquals(-2.0, data[0][1], 1e-6);
        assertEquals(-3.0, data[0][2], 1e-6);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        assertTrue(tableau.isOptimal());

        LinearObjectiveFunction f1 = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1_1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2_1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 1.0);
        SimplexTableau tableau1 = new SimplexTableau(f1, Arrays.asList(constraint1_1, constraint2_1), GoalType.MINIMIZE, true, 1e-6, 10);
        assertFalse(tableau1.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0, -3.0}, {2.0, 1.0, -2.0}, {-3.0, -2.0, 4.0}}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new DoubleArrayRealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new DoubleArrayRealVector(new double[]{2.0, -1.0, 1.0}), Relationship.GEQ, 2.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, true, 1e-6, 10);
        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();
        assertEquals(3, coefficients.length);
        assertEquals(1.0, coefficients[0], 1e-6);
        assertEquals(-0.5, coefficients[1], 1e-6);
        assertEquals(-0.5, coefficients[2], 1e-6);
        assertEquals(1.5, tableau.f.getValue(coefficients), 1e-6);
    }
}
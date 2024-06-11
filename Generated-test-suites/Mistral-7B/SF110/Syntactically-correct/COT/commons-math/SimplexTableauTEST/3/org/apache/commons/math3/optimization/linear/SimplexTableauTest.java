package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    private SimplexTableau simplexTableau;

    @Before
    public void setUp() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {2.0, 1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.GEQ, 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 0.001, 10);
    }

    

    // Test constructor
    @Test
    public void testSimplexTableauConstructor() {
        Assert.assertNotNull(simplexTableau);
    }

    // Test initializeColumnLabels method
    @Test
    public void testInitializeColumnLabels() {
        Assert.assertEquals(13, simplexTableau.getWidth());
        Assert.assertEquals("W", simplexTableau.columnLabels.get(0));
        Assert.assertEquals("Z", simplexTableau.columnLabels.get(1));
        Assert.assertEquals("x0", simplexTableau.columnLabels.get(2));
        Assert.assertEquals("x1", simplexTableau.columnLabels.get(3));
        Assert.assertEquals("x-", simplexTableau.columnLabels.get(4));
        Assert.assertEquals("s0", simplexTableau.columnLabels.get(5));
        Assert.assertEquals("s1", simplexTableau.columnLabels.get(6));
        Assert.assertEquals("a0", simplexTableau.columnLabels.get(7));
        Assert.assertEquals("a1", simplexTableau.columnLabels.get(8));
        Assert.assertEquals("RHS", simplexTableau.columnLabels.get(12));
    }

    // Test getNumObjectiveFunctions method
    @Test
    public void testGetNumObjectiveFunctions() {
        Assert.assertEquals(1, simplexTableau.getNumObjectiveFunctions());
    }

    // Test normalizeConstraints method
    @Test
    public void testNormalizeConstraints() {
        List<LinearConstraint> normalizedConstraints = simplexTableau.normalizeConstraints(simplexTableau.constraints);
        Assert.assertEquals(2, normalizedConstraints.size());
        LinearConstraint normalizedConstraint1 = normalizedConstraints.get(0);
        LinearConstraint normalizedConstraint2 = normalizedConstraints.get(1);
        Assert.assertEquals(2, normalizedConstraint1.getCoefficients().getRowDimension());
        Assert.assertEquals(Relationship.LEQ, normalizedConstraint1.getRelationship());
        Assert.assertEquals(-2.0, normalizedConstraint1.getValue(), 0.0);
        Assert.assertEquals(2, normalizedConstraint2.getCoefficients().getRowDimension());
        Assert.assertEquals(Relationship.GEQ, normalizedConstraint2.getRelationship());
        Assert.assertEquals(3.0, normalizedConstraint2.getValue(), 0.0);
    }

    // Test getEntry method
    @Test
    public void testGetEntry() {
        Assert.assertEquals(1.0, simplexTableau.getEntry(0, 0), 0.0);
        Assert.assertEquals(-2.0, simplexTableau.getEntry(0, 1), 0.0);
        Assert.assertEquals(-1.0, simplexTableau.getEntry(0, 2), 0.0);
        Assert.assertEquals(1.0, simplexTableau.getEntry(0, 4), 0.0);
        Assert.assertEquals(5.0, simplexTableau.getEntry(0, 12), 0.0);
        Assert.assertEquals(1.0, simplexTableau.getEntry(1, 2), 0.0);
        Assert.assertEquals(1.0, simplexTableau.getEntry(1, 3), 0.0);
        Assert.assertEquals(1.0, simplexTableau.getEntry(1, 5), 0.0);
        Assert.assertEquals(-3.0, simplexTableau.getEntry(1, 6), 0.0);
        Assert.assertEquals(-1.0, simplexTableau.getEntry(1, 7), 0.0);
        Assert.assertEquals(1.0, simplexTableau.getEntry(1, 8), 0.0);
        Assert.assertEquals(3.0, simplexTableau.getEntry(1, 12), 0.0);
    }

    // Test isOptimal method
    @Test
    public void testIsOptimal() {
        Assert.assertTrue(simplexTableau.isOptimal());
    }

    // Test getSolution method
    @Test
    public void testGetSolution() {
        PointValuePair solution = simplexTableau.getSolution();
        double[] coefficients = solution.getPoint();
        Assert.assertEquals(2, coefficients.length);
        Assert.assertEquals(2.0 / 3.0, coefficients[0], 0.01);
        Assert.assertEquals(-1.0 / 3.0, coefficients[1], 0.01);
        Assert.assertEquals(7.0 / 3.0, simplexTableau.f.getValue(coefficients), 0.01);
    }

    // Test dropPhase1Objective method
    @Test
    public void testDropPhase1Objective() {
        simplexTableau.dropPhase1Objective();
        Assert.assertEquals(11, simplexTableau.getWidth());
        Assert.assertFalse(simplexTableau.isOptimal());
    }

    // Test getNumDecisionVariables method
    @Test
    public void testGetNumDecisionVariables() {
        Assert.assertEquals(2, simplexTableau.getNumDecisionVariables());
    }

    // Test getOriginalNumDecisionVariables method
    @Test
    public void testGetOriginalNumDecisionVariables() {
        Assert.assertEquals(2, simplexTableau.getOriginalNumDecisionVariables());
    }

    // Test getNumSlackVariables method
    @Test
    public void testGetNumSlackVariables() {
        Assert.assertEquals(2, simplexTableau.getNumSlackVariables());
    }

    // Test getNumArtificialVariables method
    @Test
    public void testGetNumArtificialVariables() {
        Assert.assertEquals(2, simplexTableau.getNumArtificialVariables());
    }

    // Test equals method
    @Test
    public void testEquals() {
        SimplexTableau simplexTableau2 = new SimplexTableau(simplexTableau.f, simplexTableau.constraints, simplexTableau.goalType, simplexTableau.restrictToNonNegative, simplexTableau.epsilon, simplexTableau.maxUlps);
        Assert.assertEquals(simplexTableau, simplexTableau2);
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        SimplexTableau simplexTableau2 = new SimplexTableau(simplexTableau.f, simplexTableau.constraints, simplexTableau.goalType, simplexTableau.restrictToNonNegative, simplexTableau.epsilon, simplexTableau.maxUlps);
        Assert.assertEquals(simplexTableau.hashCode(), simplexTableau2.hashCode());
    }

    // Test writeObject and readObject methods
    @Test
    public void testWriteAndReadObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(simplexTableau);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        SimplexTableau simplexTableau2 = (SimplexTableau)objectInputStream.readObject();
        Assert.assertEquals(simplexTableau, simplexTableau2);
    }

    
}
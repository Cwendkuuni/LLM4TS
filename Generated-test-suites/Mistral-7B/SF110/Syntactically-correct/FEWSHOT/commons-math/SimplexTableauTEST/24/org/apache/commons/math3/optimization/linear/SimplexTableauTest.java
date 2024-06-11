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
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 3.0}), Relationship.LEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        List<String> expectedColumnLabels = Arrays.asList("W", "Z", "x0", "x1", "x2", "x-", "s0", "s1", "a0", "a1", "RHS");
        List<String> actualColumnLabels = tableau.columnLabels;
        assertEquals(expectedColumnLabels, actualColumnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 3.0}), Relationship.LEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 2.0, 3.0, 1.0, 2.0, 3.0, -1.0, 0.0, 1.0, 1.0, 5.0},
                {1.0, -1.0, -2.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, -1.0, 0.0, 1.0}
        });
        RealMatrix actualTableau = tableau.tableau;
        assertArrayEquals(expectedTableau.getData(), actualTableau.getData(), 0.0);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 3.0}), Relationship.LEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        tableau.dropPhase1Objective();
        int expectedWidth = 7;
        int expectedHeight = 3;
        int actualWidth = tableau.getWidth();
        int actualHeight = tableau.getHeight();
        assertEquals(expectedWidth, actualWidth);
        assertEquals(expectedHeight, actualHeight);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 3.0}), Relationship.LEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 3.0}), Relationship.LEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        PointValuePair solution = tableau.getSolution();
        double[] expectedCoefficients = new double[]{1.0 / 3.0, 2.0 / 3.0};
        double expectedValue = 11.0 / 3.0;
        assertArrayEquals(expectedCoefficients, solution.getPoint(), 0.0);
        assertEquals(expectedValue, solution.getValue(), 0.0);
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}));
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 3.0}), Relationship.LEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(tableau);
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        SimplexTableau deserializedTableau = (SimplexTableau)objIn.readObject();
        assertEquals(tableau.hashCode(), deserializedTableau.hashCode());
    }
}
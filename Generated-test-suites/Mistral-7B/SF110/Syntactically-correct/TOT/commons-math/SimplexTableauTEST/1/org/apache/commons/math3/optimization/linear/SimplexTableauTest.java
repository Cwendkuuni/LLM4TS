package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testEmptyTableau() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[0][0]), 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
        Assert.assertNull(tableau.tableau);
        Assert.assertEquals(0, tableau.numDecisionVariables);
        Assert.assertEquals(0, tableau.numSlackVariables);
        Assert.assertEquals(0, tableau.numArtificialVariables);
        Assert.assertEquals(0, tableau.getHeight());
        Assert.assertEquals(0, tableau.getWidth());
    }

    @Test
    public void testTableauWithOneConstraintAndOneDecisionVariable() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -3.0}}), 1.0);
        LinearConstraint constraint = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {0.0, 0.0, 1.0}}), Relationship.LEQ, 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);

        Assert.assertEquals(4, tableau.getWidth());
        Assert.assertEquals(2, tableau.getHeight());

        PointValuePair solution = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-12);
        Assert.assertEquals(1.0, solution.getValue(), 1e-12);

        tableau.dropPhase1Objective();
        Assert.assertEquals(2, tableau.getHeight());
        Assert.assertEquals(-1.0 / 2.0, tableau.getEntry(1, 1), 1e-12);
        Assert.assertEquals(0.5, tableau.getEntry(1, 2), 1e-12);

        tableau.subtractRow(0, 1, 1);
        Assert.assertEquals(2, tableau.getHeight());
        Assert.assertEquals(0.0, tableau.getEntry(0, 0), 1e-12);
        Assert.assertEquals(1.0, tableau.getEntry(0, 1), 1e-12);
        Assert.assertEquals(-1.0, tableau.getEntry(1, 1), 1e-12);
        Assert.assertEquals(0.5, tableau.getEntry(1, 2), 1e-12);
    }

    @Test
    public void testTableauWithMultipleConstraintsAndDecisionVariables() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {-2.0, 5.0, -3.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {0.0, 0.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {0.0, 0.0, 1.0}}), Relationship.LEQ, 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);

        Assert.assertEquals(6, tableau.getWidth());
        Assert.assertEquals(3, tableau.getHeight());

        tableau.dropPhase1Objective();
        Assert.assertEquals(5, tableau.getHeight());
        Assert.assertEquals(3, tableau.getWidth());

        tableau.subtractRow(0, 1, 1);
        tableau.subtractRow(1, 0, 1);
        tableau.divideRow(0, 2);
        tableau.divideRow(1, 2);

        Assert.assertEquals(3, tableau.getHeight());
        Assert.assertEquals(-1.0 / 2.0, tableau.getEntry(1, 1), 1e-12);
        Assert.assertEquals(-1.0 / 3.0, tableau.getEntry(1, 2), 1e-12);
        Assert.assertEquals(1.0 / 6.0, tableau.getEntry(1, 3), 1e-12);

        PointValuePair solution = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{0.3333333333333333, 0.3333333333333333, 0.3333333333333333}, solution.getPoint(), 1e-12);
        Assert.assertEquals(2.0 / 3.0, solution.getValue(), 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstraint() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[0][0]), 0.0);
        LinearConstraint constraint = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {0.0, 0.0, 1.0}}), Relationship.UNKNOWN, 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {-2.0, 5.0, -3.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0}, {0.0, 0.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0, 1.0}, {0.0, 0.0, 1.0}}), Relationship.LEQ, 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MAXIMIZE, false, 1e-12);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(tableau);
        objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        SimplexTableau deserializedTableau = (SimplexTableau) objectInputStream.readObject();

        Assert.assertEquals(deserializedTableau.tableau, tableau.tableau);
    }
}
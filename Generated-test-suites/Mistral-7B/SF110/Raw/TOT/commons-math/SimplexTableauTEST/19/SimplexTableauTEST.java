package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredErrorFunction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplexTableauTest {

    @Test
    public void testCreation() {
        final LinearObjectiveFunction f = new SquaredErrorFunction(new double[]{-1.0, 1.0}, new double[]{2.0, 3.0});
        final List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 0.0}), Relationship.EQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 1.0),
                new LinearConstraint(new RealVector(new double[]{2.0, 1.0}), Relationship.GEQ, 2.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-10);
        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(3, tableau.getNumSlackVariables());
        Assert.assertEquals(0, tableau.getNumArtificialVariables());
        Assert.assertEquals(1e-10, tableau.epsilon, 1e-12);
        Assert.assertEquals(10, tableau.maxUlps);
        Assert.assertNotNull(tableau.tableau);
    }

    @Test
    public void testSerialization() throws Exception {
        final LinearObjectiveFunction f = new SquaredErrorFunction(new double[]{-1.0, 1.0}, new double[]{2.0, 3.0});
        final List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 0.0}), Relationship.EQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 1.0),
                new LinearConstraint(new RealVector(new double[]{2.0, 1.0}), Relationship.GEQ, 2.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-10);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(tableau);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        SimplexTableau tableau2 = (SimplexTableau) ois.readObject();
        ois.close();

        Assert.assertEquals(tableau, tableau2);
    }

    @Test
    public void testIsOptimal() {
        final LinearObjectiveFunction f = new SquaredErrorFunction(new double[]{-1.0, 1.0}, new double[]{2.0, 3.0});
        final List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 0.0}), Relationship.EQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 1.0),
                new LinearConstraint(new RealVector(new double[]{2.0, 1.0}), Relationship.GEQ, 2.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-10);
        tableau.dropPhase1Objective();
        Assert.assertTrue(tableau.isOptimal());

        // Test error case: non-optimal tableau
        double[][] data = new double[][]{{1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, {1.0, -1.0, 2.0, 3.0, 4.0, 7.0}};
        tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-10) {
            {
                this.tableau = new Array2DRowRealMatrix(data);
            }
        };
        Assert.assertFalse(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        final LinearObjectiveFunction f = new SquaredErrorFunction(new double[]{-1.0, 1.0}, new double[]{2.0, 3.0});
        final List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 0.0}), Relationship.EQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 1.0),
                new LinearConstraint(new RealVector(new double[]{2.0, 1.0}), Relationship.GEQ, 2.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-10);
        tableau.dropPhase1Objective();
        PointValuePair solution = tableau.getSolution();
        Assert.assertEquals(new double[]{0.0, 1.0}, solution.getPoint(), 1e-12);
        Assert.assertEquals(1.0, solution.getValue(), 1e-12);
    }

    @Test
    public void testDivideRow() {
        final LinearObjectiveFunction f = new SquaredErrorFunction(new double[]{-1.0, 1.0}, new double[]{2.0, 3.0});
        final List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 0.0}), Relationship.EQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 1.0),
                new LinearConstraint(new RealVector(new double[]{2.0, 1.0}), Relationship.GEQ, 2.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-10);
        int dividendRow = 0;
        double divisor = 2.0;
        tableau.divideRow(dividendRow, divisor);
        double[][] data = tableau.getData();
        Assert.assertEquals(1.0 / divisor, data[dividendRow][0], 1e-12);
        Assert.assertEquals(1.0 / divisor, data[dividendRow][1], 1e-12);
        Assert.assertEquals(1.0 / divisor, data[dividendRow][2], 1e-12);
        Assert.assertEquals(1.0 / divisor, data[dividendRow][3], 1e-12);
        Assert.assertEquals(1.0 / divisor, data[dividendRow][4], 1e-12);
        Assert.assertEquals(1.0 / divisor, data[dividendRow][5], 1e-12);
    }

    @Test
    public void testSubtractRow() {
        final LinearObjectiveFunction f = new SquaredErrorFunction(new double[]{-1.0, 1.0}, new double[]{2.0, 3.0});
        final List<LinearConstraint> constraints = Arrays.asList(
                new LinearConstraint(new RealVector(new double[]{1.0, 0.0}), Relationship.EQ, 0.0),
                new LinearConstraint(new RealVector(new double[]{-1.0, 1.0}), Relationship.LEQ, 1.0),
                new LinearConstraint(new RealVector(new double[]{2.0, 1.0}), Relationship.GEQ, 2.0)
        );
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-10);
        int minuendRow = 0;
        int subtrahendRow = 1;
        double multiple = 2.0;
        tableau.subtractRow(minuendRow, subtrahendRow, multiple);
        double[][] data = tableau.getData();
        for (int i = 0; i < data[0].length; ++i) {
            Assert.assertEquals(data[minuendRow][i] - 2.0 * data[subtrahendRow][i], data[minuendRow][i], 1e-12);
        }
    }
}
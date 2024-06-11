package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-5.0, 4.0, 2.0}), 1.0);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0, -1.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -2.0, 1.0}), Relationship.GEQ, 0.0));

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6);

        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(0, tableau.getNumArtificialVariables());
        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-5.0, 4.0, 2.0, 0.0, 0.0, 1.0, -1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0},
                {1.0, 2.0, -1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0},
                {1.0, -2.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0}
        });

        Assert.assertArrayEquals(expectedTableau.getData(), tableau.getData(), 1e-6);
    }

    @Test
    public void testSimplexTableauConstructionWithArtificialVariables() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-5.0, 4.0, 2.0}), 1.0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-3.0, 3.0, 1.0}), -1.0);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0, -1.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -2.0, 1.0}), Relationship.GEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0, 2.0}), Relationship.EQ, 3.0));

        SimplexTableau tableau = new SimplexTableau(f1, constraints, GoalType.MINIMIZE, false, 1e-6);
        tableau.addObjectiveFunction(f2);

        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(2, tableau.getNumArtificialVariables());
        Assert.assertEquals(3, tableau.getNumObjectiveFunctions());

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-5.0, 4.0, 2.0, 0.0, 1.0, 1.0, -1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0},
                {1.0, 2.0, -1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0},
                {1.0, -2.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 1.0, 0.0},
                {-3.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0}
        });

        Assert.assertArrayEquals(expectedTableau.getData(), tableau.getData(), 1e-6);
    }

    @Test
    public void testSimplexTableauDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-5.0, 4.0, 2.0}), 1.0);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0, -1.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -2.0, 1.0}), Relationship.GEQ, 0.0));

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6);
        tableau.dropPhase1Objective();

        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testSimplexTableauIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{5.0, 4.0, 2.0}), 1.0);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0, -1.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -2.0, 1.0}), Relationship.GEQ, 0.0));

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6);

        Assert.assertFalse(tableau.isOptimal());
    }

    @Test
    public void testSimplexTableauSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{5.0, 4.0, 2.0}), 1.0);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0, -1.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -2.0, 1.0}), Relationship.GEQ, 0.0));

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6);
        tableau.dropPhase1Objective();

        PointValuePair solution = tableau.getSolution();

        RealVector coefficients = solution.getPoint();
        Assert.assertArrayEquals(new double[]{2.0 / 3.0, 1.0 / 3.0}, coefficients.toArray(), 1e-6);
        Assert.assertEquals(1.0 / 3.0, tableau.getSolution().getValue(), 1e-6);
    }
}
package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimplexTableauTest {

    @Test
    public void testEmptyConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 0.0);
        SimplexTableau tableau = new SimplexTableau(f, Collections.emptyList(), GoalType.MAXIMIZE, false, 1.0E-12);
        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(0, tableau.getNumDecisionVariables());
        Assert.assertEquals(0, tableau.getNumSlackVariables());
        Assert.assertEquals(0, tableau.getNumArtificialVariables());
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, f.getCoefficients().toArray());
        Assert.assertEquals(0.0, tableau.f.getConstantTerm(), 1.0E-12);
        Assert.assertEquals(1, tableau.numArtificialVariables);
        Assert.assertArrayEquals(new double[]{}, tableau.getData()[0]);
        Assert.assertTrue(tableau.isOptimal());
        PointValuePair solution = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{0.0, 0.0, 0.0}, solution.getPoint());
        Assert.assertEquals(0.0, solution.getValue(), 1.0E-12);
    }

    @Test
    public void testSingleConstraint() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 4.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 0.0}), Relationship.LEQ, 0.0);
        constraints.add(constraint);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(1, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, f.getCoefficients().toArray());
        Assert.assertEquals(4.0, tableau.f.getConstantTerm(), 1.0E-12);
        Assert.assertArrayEquals(new double[]{-1.0, 1.0, 0.0}, tableau.getData()[1]);
        Assert.assertEquals(1.0, tableau.getData()[1][tableau.getSlackVariableOffset()], 1.0E-12);
        Assert.assertTrue(tableau.isOptimal());
        PointValuePair solution = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{3.0 / 4.0, 1.0 - 3.0 / 4.0, 3.0 / 4.0}, solution.getPoint());
        Assert.assertEquals(4.0 / 4.0, solution.getValue(), 1.0E-12);
    }

    @Test
    public void testMultipleConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0, 4.0}), 9.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 0.0, 0.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{0.0, 1.0, 1.0, -1.0}), Relationship.LEQ, 2.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(4, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0}, f.getCoefficients().toArray());
        Assert.assertEquals(9.0, tableau.f.getConstantTerm(), 1.0E-12);
        Assert.assertArrayEquals(new double[]{-1.0, 1.0, 0.0, 0.0}, tableau.getData()[1]);
        Assert.assertArrayEquals(new double[]{0.0, 1.0, 1.0, -1.0}, tableau.getData()[2]);
        Assert.assertTrue(tableau.isOptimal());
        PointValuePair solution = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{2.0 / 3.0, 1.0 - 1.0 / 3.0, 1.0, 1.0 / 3.0}, solution.getPoint());
        Assert.assertEquals(3.0 / 3.0, solution.getValue(), 1.0E-12);
    }

    @Test
    public void testErrorScenario() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0}), 4.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 0.0}), Relationship.EQ, 0.0);
        constraints.add(constraint);
        SimplexTableau tableau = null;
        try {
            tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);
        } catch (IllegalArgumentException e) {
            // expected
        }
        Assert.assertNull(tableau);
    }
}
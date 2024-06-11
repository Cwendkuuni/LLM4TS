package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;
import org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getOriginalNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(1.0E-6, tableau.epsilon, 1e-8);
        assertEquals(10, tableau.maxUlps);
        assertEquals(LinearObjectiveFunction.class, tableau.f.getClass());
        assertEquals(ArrayList.class, tableau.constraints.getClass());

        PointValuePair solution = tableau.getSolution();
        assertEquals(2.0, solution.getPoint()[0], 1e-8);
        assertEquals(3.0, solution.getPoint()[1], 1e-8);
        assertEquals(6.0, solution.getValue(), 1e-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauConstructionWithInvalidConstraint() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-2.0}), 1.0);
        LinearConstraint constraint = new LinearConstraint(null, Relationship.GEQ, 2.0);
        new SimplexTableau(f, Arrays.asList(constraint), GoalType.MAXIMIZE, false, 1e-6);
    }

    @Test
    public void testSimplexTableauOptimality() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        assertTrue(tableau.isOptimal());

        LinearObjectiveFunction newF = new LinearObjectiveFunction(new ArrayRealVector(new double[]{3.0, 1.0}), 1.0);
        tableau.f = newF;
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testSimplexTableauDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{-2.0, 3.0}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 2.0}), Relationship.GEQ, 3.0);
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        tableau.dropPhase1Objective();

        assertEquals(1, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getOriginalNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());

        PointValuePair solution = tableau.getSolution();
        assertEquals(1.5, solution.getPoint()[0], 1e-8);
        assertEquals(2.5, solution.getPoint()[1], 1e-8);
        assertEquals(4.5, solution.getValue(), 1e-8);
    }
}
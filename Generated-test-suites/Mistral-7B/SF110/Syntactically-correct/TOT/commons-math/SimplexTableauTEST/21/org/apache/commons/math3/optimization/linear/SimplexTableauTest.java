package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimpleTableauCreation() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction simpleFunction = new LinearObjectiveFunction(new RealVector(new double[] {-1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.EQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[] {-1.0, 1.0}), Relationship.LEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(simpleFunction, constraints, goalType, false, 1e-10);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(1e-10, tableau.epsilon);
        assertEquals(10, tableau.maxUlps);
        assertNotNull(tableau.tableau);
    }

    @Test
    public void testTableauCreationWithNonNegativeConstraint() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction simpleFunction = new LinearObjectiveFunction(new RealVector(new double[] {-1.0, 2.0}));
        LinearConstraint constraint = new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.LEQ, 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint);
        SimplexTableau tableau = new SimplexTableau(simpleFunction, constraints, goalType, true, 1e-10);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(1e-10, tableau.epsilon);
        assertEquals(10, tableau.maxUlps);
        assertNotNull(tableau.tableau);
    }

    @Test
    public void testTableauCreationWithMultipleConstraints() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction simpleFunction = new LinearObjectiveFunction(new RealVector(new double[] {-1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.EQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[] {-1.0, 1.0}), Relationship.LEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new RealVector(new double[] {-1.0, -1.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);
        SimplexTableau tableau = new SimplexTableau(simpleFunction, constraints, goalType, false, 1e-10);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(5, tableau.getNumSlackVariables());
        assertEquals(2, tableau.getNumArtificialVariables());
        assertEquals(1e-10, tableau.epsilon);
        assertEquals(10, tableau.maxUlps);
        assertNotNull(tableau.tableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new RealVector(new double[] {-1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.EQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[] {-1.0, 1.0}), Relationship.LEQ, 1.0);
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint1);
        originalConstraints.add(constraint2);
        List<LinearConstraint> normalizedConstraints = new SimplexTableau(function, originalConstraints, GoalType.MAXIMIZE, false, 1e-10).normalizeConstraints(originalConstraints);

        assertEquals(2, normalizedConstraints.size());
        LinearConstraint normalizedConstraint1 = normalizedConstraints.get(0);
        LinearConstraint originalConstraint1 = originalConstraints.get(0);
        assertEquals(originalConstraint1.getCoefficients(), normalizedConstraint1.getCoefficients());
        assertEquals(originalConstraint1.getRelationship(), normalizedConstraint1.getRelationship());
        assertEquals(originalConstraint1.getValue(), normalizedConstraint1.getValue());

        LinearConstraint normalizedConstraint2 = normalizedConstraints.get(1);
        LinearConstraint originalConstraint2 = originalConstraints.get(1);
        assertEquals(originalConstraint2.getCoefficients().mapMultiply(-1.0), normalizedConstraint2.getCoefficients());
        assertEquals(originalConstraint2.getRelationship().oppositeRelationship(), normalizedConstraint2.getRelationship());
        assertEquals(-1.0 * originalConstraint2.getValue(), normalizedConstraint2.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableauCreationWithInvalidConstraint() {
        GoalType goalType = GoalType.MAXIMIZE;
        LinearObjectiveFunction simpleFunction = new LinearObjectiveFunction(new RealVector(new double[] {-1.0, 2.0}));
        LinearConstraint constraint = new LinearConstraint(new RealVector(new double[] {1.0, 1.0}), Relationship.EQ, -1.0); // Invalid constraint value
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint);
        new SimplexTableau(simpleFunction, constraints, goalType, false, 1e-10);
    }
}
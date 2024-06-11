package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.*;
import java.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauCreation() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.GEQ, 1.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);

        assertEquals(5, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(10, tableau.getMaxUlps());
        assertEquals(1e-10, tableau.getEpsilon());
        assertEquals(false, tableau.isRestrictToNonNegative());

        assertEquals(3, tableau.getNumObjectiveFunctions());
        assertEquals(1, tableau.getOriginalNumDecisionVariables());
        assertEquals(6, tableau.getWidth());
        assertEquals(7, tableau.getHeight());
        assertNotNull(tableau.getData());

        assertEquals(5, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(10, tableau.getMaxUlps());
        assertEquals(1e-10, tableau.getEpsilon());
        assertEquals(false, tableau.isRestrictToNonNegative());
    }

    @Test
    public void testSimplexTableauNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);

        assertEquals(constraint1.getCoefficients(), normalizedConstraints.get(0).getCoefficients());
        assertEquals(constraint1.getRelationship(), normalizedConstraints.get(0).getRelationship());
        assertEquals(constraint1.getValue(), normalizedConstraints.get(0).getValue());

        assertEquals(constraint2.getCoefficients().mapMultiply(-1.0), normalizedConstraints.get(1).getCoefficients());
        assertEquals(constraint2.getRelationship().oppositeRelationship(), normalizedConstraints.get(1).getRelationship());
        assertEquals(-1.0 * constraint2.getValue(), normalizedConstraints.get(1).getValue());
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);

        int col = 0;
        Integer row = tableau.getBasicRow(col);
        assertEquals(row, 0);

        col = 1;
        row = tableau.getBasicRow(col);
        assertEquals(row, 1);

        col = 2;
        row = tableau.getBasicRow(col);
        assertEquals(row, 1);

        col = 3;
        row = tableau.getBasicRow(col);
        assertEquals(row, 2);

        col = 4;
        row = tableau.getBasicRow(col);
        assertEquals(row, null);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);

        assertFalse(tableau.isOptimal());

        constraint1.setValue(0.0);
        tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);

        PointValuePair solution = tableau.getSolution();
        assertArrayEquals(new double[]{0.0, 1.0, 0.0}, solution.getPoint(), 1e-10);
        assertEquals(5.0, solution.getValue(), 1e-10);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 2.0}), Relationship.EQ, 0.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);
        tableau.dropPhase1Objective();

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(5, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertNotNull(tableau.getData());
    }

    @Test
    public void testDivideRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);
        int dividendRow = 0;
        double divisor = 2.0;
        tableau.divideRow(dividendRow, divisor);

        for (int i = 0; i < tableau.getWidth(); ++i) {
            double entry = tableau.getEntry(dividendRow, i);
            assertEquals(entry / divisor, tableau.getEntry(dividendRow, i), 1e-10);
        }
    }

    @Test
    public void testSubtractRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}), 5.0);
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, 1.0}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-1.0, -1.0, 2.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);
        int minuendRow = 0;
        int subtrahendRow = 1;
        double multiple = 2.0;
        tableau.subtractRow(minuendRow, subtrahendRow, multiple);

        for (int i = 0; i < tableau.getWidth(); ++i) {
            double entry = tableau.getEntry(minuendRow, i);
            double expectedEntry = tableau.getEntry(minuendRow, i) - tableau.getEntry(subtrahendRow, i) * multiple;
            assertEquals(expectedEntry, tableau.getEntry(minuendRow, i), 1e-10);
        }
    }
}
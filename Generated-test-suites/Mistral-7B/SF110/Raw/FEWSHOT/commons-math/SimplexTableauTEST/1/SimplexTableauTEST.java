package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, -3.0}, {-1.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(2, tableau.getNumObjectiveFunctions());
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, -3.0, 0.0, 3.0, 1.0}, {-3.0, -1.0, 4.0, 3.0, -1.0, 1.0}, {1.0, 2.0, 0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 1.0, -3.0, 1.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {-1.0, -1.0, 0.0, 0.0, 0.0, 1.0}});
        assertEquals(expectedTableau, tableau.getData());
    }

    @Test
    public void testCreateTableauWithConstraintsPersist() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, -3.0}, {-1.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
        assertEquals(1, normalizedConstraints.size());
        LinearConstraint normalizedConstraint = normalizedConstraints.get(0);
        assertEquals(Relationship.LEQ, normalizedConstraint.getRelationship());
        assertEquals(2.0, normalizedConstraint.getCoefficients().getEntry(0), 0.0);
        assertEquals(1.0, normalizedConstraint.getCoefficients().getEntry(1), 0.0);
        assertEquals(-3.0, normalizedConstraint.getValue(), 0.0);

        tableau.dropPhase1Objective();
        RealMatrix expectedTableauAfterDropPhase1Objective = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, -3.0}, {-3.0, -1.0, 4.0}, {1.0, 2.0, 0.0}, {0.0, 1.0, -3.0}, {0.0, 0.0, 0.0}, {-1.0, -1.0, 0.0}});
        assertEquals(expectedTableauAfterDropPhase1Objective, tableau.getData());
    }

    @Test
    public void testSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, -3.0}, {-1.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        tableau.dropPhase1Objective();

        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();
        assertEquals(3.0, coefficients[0], 0.0);
        assertEquals(-1.5, coefficients[1], 0.0);
        assertEquals(-1.5, coefficients[2], 0.0);

        assertEquals(6.0, tableau.f.getValue(coefficients), 0.0);
    }

    @Test
    public void testGetHeightAndWidth() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, -3.0}, {-1.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        assertEquals(6, tableau.getHeight());
        assertEquals(6, tableau.getWidth());
    }

    @Test
    public void testGetEntry() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, -3.0}, {-1.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

        assertEquals(2.0, tableau.getEntry(0, 0), 0.0);
        assertEquals(-3.0, tableau.getEntry(0, 2), 0.0);
        assertEquals(3.0, tableau.getEntry(0, 5), 0.0);
        assertEquals(1.0, tableau.getEntry(1, 0), 0.0);
        assertEquals(-1.0, tableau.getEntry(1, 1), 0.0);
        assertEquals(4.0, tableau.getEntry(1, 4), 0.0);
        assertEquals(3.0, tableau.getEntry(1, 5), 0.0);
        assertEquals(-3.0, tableau.getEntry(2, 2), 0.0);
        assertEquals(1.0, tableau.getEntry(2, 5), 0.0);
        assertEquals(3.0, tableau.getEntry(3, 5), 0.0);
    }

    @Test
    public void testSetEntry() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, -3.0}, {-1.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

        tableau.setEntry(0, 2, 2.5);
        assertEquals(2.5, tableau.getEntry(0, 2), 0.0);

        tableau.setEntry(1, 1, -2.5);
        assertEquals(-2.5, tableau.getEntry(1, 1), 0.0);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, -3.0}, {-1.0, 4.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

        tableau.dropPhase1Objective();

        assertTrue(tableau.isOptimal());

        tableau.setEntry(0, 2, 0.5);
        assertFalse(tableau.isOptimal());
    }
}